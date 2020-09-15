package com.parkit.parkingsystem.integration;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

	private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
	private static ParkingSpotDAO parkingSpotDAO;
	private static TicketDAO ticketDAO;
	private static DataBasePrepareService dataBasePrepareService;

	@Mock
	private static InputReaderUtil inputReaderUtil;

	@BeforeAll
	private static void setUp() throws Exception {
		parkingSpotDAO = new ParkingSpotDAO();
		parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
		ticketDAO = new TicketDAO();
		ticketDAO.dataBaseConfig = dataBaseTestConfig;
		dataBasePrepareService = new DataBasePrepareService();
	}

	@BeforeEach
	private void setUpPerTest() throws Exception {
		when(inputReaderUtil.readSelection()).thenReturn(1);
		when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
		dataBasePrepareService.clearDataBaseEntries();
	}

	@AfterAll
	private static void tearDown() {

	}

	@Test
	public void testParkingACar() throws Exception {
		ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		parkingService.processIncomingVehicle();
		// TODO: check that a ticket is actualy saved in DB and Parking table is updated
		// with availability
		// GIVEN
		String vehicleRegNumber = inputReaderUtil.readVehicleRegistrationNumber();
		// WHEN
		DataBaseTestConfig db_test = new DataBaseTestConfig();
		Connection con = db_test.getConnection();
		PreparedStatement ps = con.prepareStatement(DBConstants.FIND_IN_TICKET);
		ps.setString(1, vehicleRegNumber);
		ResultSet rs = ps.executeQuery();
		// THEN
		if (rs.last()) {
			assertNotEquals(null, rs.getString(5));
		}
		db_test.closeResultSet(rs);
		db_test.closePreparedStatement(ps);
	}

	@Test
	public void testParkingLotExit() throws Exception {
		testParkingACar();
		ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		parkingService.processExitingVehicle();
		// TODO: check that the fare generated and out time are populated correctly in
		// the database
		// GIVEN
		String vehicleRegNumber = inputReaderUtil.readVehicleRegistrationNumber();
		// WHEN
		DataBaseTestConfig db_test = new DataBaseTestConfig();
		Connection con = db_test.getConnection();
		PreparedStatement ps = con.prepareStatement(DBConstants.FIND_OUT_TICKET);
		ps.setString(1, vehicleRegNumber);
		ResultSet rs = ps.executeQuery();
		// THEN
		if (rs.last()) {
			assertNotEquals(null, rs.getString(5));
		}
		db_test.closeResultSet(rs);
		db_test.closePreparedStatement(ps);
	}

}
