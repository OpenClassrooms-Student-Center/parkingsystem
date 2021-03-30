package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.when;

import java.sql.Date;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

	private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
	private static ParkingSpotDAO parkingSpotDAO;
	private static TicketDAO ticketDAO;
	private static DataBasePrepareService dataBasePrepareService;

	@Mock
	private static InputReaderUtil inputReaderUtil;
	private static FareCalculatorService fareCalculatorService;
	private static ParkingSpot parkingSpot;

	@BeforeAll
	private static void setUp() throws Exception {
		fareCalculatorService = new FareCalculatorService();
		parkingSpotDAO = new ParkingSpotDAO();
		parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
		ticketDAO = new TicketDAO();
		ticketDAO.dataBaseConfig = dataBaseTestConfig;
		dataBasePrepareService = new DataBasePrepareService();
	}

	@BeforeEach
	private void setUpPerTest() throws Exception {
		when(inputReaderUtil.readSelection()).thenReturn(1);
		Mockito.lenient().when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
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
		//
		Ticket ticket = ticketDAO.getTicket(inputReaderUtil.readVehicleRegistrationNumber());
		assertThat(ticket.getVehicleRegNumber()).isEqualTo("ABCDEF");
		assertThat(ticket.getInTime()).isNotNull();
		assertThat(ticket.getOutTime()).isNull();
		assertThat(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).isEqualTo(2);

	}

	@Test
	public void testParkingLotExit() throws Exception {
		testParkingACar();
		// TODO: check that the fare generated and out time are populated correctly in
		// the database
		Ticket ticket = ticketDAO.getTicket(inputReaderUtil.readVehicleRegistrationNumber());
		Date outTime = new Date(0);
        outTime.setTime(System.currentTimeMillis() + 31 * 60 * 1000);
        ticket.setOutTime(outTime);
		ticketDAO.saveTicket(ticket);
		parkingSpotDAO.updateParking(parkingSpot);
		ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		parkingService.processExitingVehicle();
		assertThat(ticket.getOutTime()).isAfter(ticket.getInTime());
		assertThat(ticket.getPrice()).isGreaterThan(0);
	}

}
