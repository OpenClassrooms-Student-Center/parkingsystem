package com.parkit.parkingsystem.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.util.Date;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

	private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
	private static ParkingSpotDAO parkingSpotDAO;
	private static TicketDAO ticketDAO;
	private static DataBasePrepareService dataBasePrepareService;
	private ParkingService parkingService;
	private ParkingType parkingType;
	private static Ticket ticket;
	private ParkingSpot parkingSpot;

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
		parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);

	}

	@AfterAll
	private static void tearDown() {

	}

	@Test
	public void testParkingACar() throws Exception {
		when(inputReaderUtil.readSelection()).thenReturn(1);

		parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		parkingService.processIncomingVehicle();
		// TODO: check that a ticket is actualy saved in DB and Parking table is updated
		// with availability

		int parkingSuivant = parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);
		boolean parkingSpotForAbcdef = parkingSpotDAO.updateParking(ticketDAO.getTicket("ABCDEF").getParkingSpot());
		int ticketID = ticketDAO.getTicket("ABCDEF").getId();

		assertEquals(2, parkingSuivant);
		assertTrue(parkingSpotForAbcdef);
		assertNotNull(ticketID);
	}

	@Test
	public void testParkingLotExit() throws Exception {

		// TODO: check that the fare generated and out time are populated correctly in
		// the database

		Ticket ticket = new Ticket();
		ParkingSpot parkingSpot = new ParkingSpot(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR), ParkingType.CAR,
				false);
		ticket.setParkingSpot(parkingSpot);
		ticket.setVehicleRegNumber("ABCDEF");
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
		ticket.setInTime(inTime);
		ticket.setPrice(0.0);
		ticketDAO.saveTicket(ticket);

		parkingService.processExitingVehicle();

		assertEquals(1.5, (ticketDAO.getTicket("ABCDEF").getPrice()));
		assertNotNull(ticketDAO.getTicket("ABCDEF").getOutTime());
		assertEquals(1.43, (ticketDAO.getTicket("ABCDEF").getDiscount()));
	}

	@Test

	public void testParkingCarLotExitReduction() throws Exception {
		// GIVEN
		when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
		ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		parkingType = ParkingType.CAR;
		parkingSpotDAO.getNextAvailableSlot(parkingType);
		Timestamp inTime = new Timestamp(System.currentTimeMillis() - 3600000);
		parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		ticket = new Ticket();
		ticket.setParkingSpot(parkingSpot);
		ticket.setVehicleRegNumber("ABCDEF");
		ticket.setInTime(inTime);
		ticketDAO.saveTicket(ticket);

		// WHEN
		parkingService.processExitingVehicle();
		ticket = ticketDAO.getTicket("ABCDEF");

		// THEN
		assertThat(ticket.getPrice()).isEqualTo(1.43);
		assertNotNull(ticket.getOutTime());
	}

}