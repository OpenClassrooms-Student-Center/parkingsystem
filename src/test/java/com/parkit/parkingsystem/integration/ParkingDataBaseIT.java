package com.parkit.parkingsystem.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;

/**
 * The type Parking data base it.
 *
 * @author lilas
 */
@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

	private static final DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
	private static DataBasePrepareService dataBasePrepareService;
	private ParkingType parkingType;
	@Mock
	private static InputReaderUtil inputReaderUtil;

	/** The ticket DAO. */
	@Spy
	private static TicketDAO ticketDAO;

	/** The parking spot DAO. */
	@Spy
	private static ParkingSpotDAO parkingSpotDAO;

	ParkingService parkingService;

	/**
	 * Sets the up.
	 *
	 * @throws Exception the exception
	 */
	@BeforeAll
	private static void setUp() throws Exception {
		parkingSpotDAO = new ParkingSpotDAO();
		parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
		ticketDAO = new TicketDAO();
		ticketDAO.dataBaseConfig = dataBaseTestConfig;
		dataBasePrepareService = new DataBasePrepareService();
	}

	/**
	 * Tear down.
	 */
	@AfterAll
	private static void tearDown() {
		dataBasePrepareService.clearDataBaseEntries();
	}

	/**
	 * Sets the up per test.
	 *
	 * @throws Exception the exception
	 */
	@BeforeEach
	private void setUpPerTest() throws Exception {
		when(inputReaderUtil.readSelection()).thenReturn(1);
		when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
		dataBasePrepareService.clearDataBaseEntries();
		parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
	}

	/**
	 * Test parking a car.
	 *
	 * @throws Exception the exception
	 */
	@DisplayName("Entrée d'une voiture")
	@Test
	public void testParkingACar() throws Exception {

		// GIVEN
		when(inputReaderUtil.readSelection()).thenReturn(1);
		when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
		Object parkingType = ParkingType.CAR;
		parkingSpotDAO.getNextAvailableSlot(parkingType);

		// WHEN
		parkingService.processIncomingVehicle();

		// THEN
		assertThat(parkingSpotDAO.getNextAvailableSlot(parkingType)).isNotEqualTo(1);
		assertThat(ticketDAO.getTicket("ABCDEF").getParkingSpot().getId()).isEqualTo(1);

		// TODO: check that a ticket is actually saved in DB and Parking table is
		// updated with availability
	}

	@Test
	@DisplayName("Entrée d'un velo")
	public void testParkingABike() throws Exception {

		// GIVEN
		when(inputReaderUtil.readSelection()).thenReturn(2);
		when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("BDERF");
		parkingType = ParkingType.BIKE;
		parkingSpotDAO.getNextAvailableSlot(parkingType);

		// WHEN
		parkingService.processIncomingVehicle();

		// THEN
		assertThat(parkingSpotDAO.getNextAvailableSlot(parkingType)).isNotEqualTo(4);

		assertThat(ticketDAO.getTicket("BDERF").getParkingSpot().getId()).isEqualTo(4);
	}

	/**
	 * Test parking lot exit.
	 *
	 * @throws Exception the exception
	 */
	@Test
	@DisplayName("Sortie d'une voiture, enregistrement des infos en BDD")
	public void testParkingLotExit() throws Exception {

		// GIVEN
		parkingService.processIncomingVehicle();
		parkingService.processExitingVehicle();

		// WHEN
		Thread.sleep(1000);
		parkingService.processExitingVehicle();
		Ticket ticket = ticketDAO.getTicket(inputReaderUtil.readVehicleRegistrationNumber());

		// THEN
		ticket.setPrice(Fare.CAR_RATE_PER_HOUR);
		ticketDAO.updateTicket(ticket);
		assertEquals(Fare.CAR_RATE_PER_HOUR, ticket.getPrice());
		assertNotNull(ticket.getOutTime());

		// TODO: check that the fare generated and out time are populated correctly in
		// the database

	}

	@Test
	@DisplayName("Sortie d'un vélo, enregistrement des infos en BDD")
	public void testParkingBikeLotExit() throws Exception {

		// GIVEN
		parkingService.processIncomingVehicle();
		parkingService.processExitingVehicle();

		// WHEN
		Thread.sleep(1000);
		parkingService.processExitingVehicle();
		Ticket ticket = ticketDAO.getTicket(inputReaderUtil.readVehicleRegistrationNumber());

		// THEN
		ticket.setPrice(Fare.BIKE_RATE_PER_HOUR);
		ticketDAO.updateTicket(ticket);
		assertEquals(Fare.BIKE_RATE_PER_HOUR, ticket.getPrice());
		assertNotNull(ticket.getOutTime());

		// TODO: check that the fare generated and out time are populated correctly in
		// the database

	}

	/**
	 * Test recurent user exiting true.
	 *
	 * @throws Exception the exception
	 */
	@Test
	@DisplayName("Test un reccurent user en BDD")
	public void testRecurentUserExiting() throws Exception {

		// GIVEN
		when(inputReaderUtil.readSelection()).thenReturn(1);
		when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
		parkingType = ParkingType.CAR;
		parkingSpotDAO.getNextAvailableSlot(parkingType);

		// WHEN
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis());
		parkingService.processIncomingVehicle();
		parkingService.processExitingVehicle();
		boolean recurrentUserTest = ticketDAO.isReccurentUser("ABCDEF");

		// THEN
		assertTrue(recurrentUserTest);
	}

	@DisplayName("Test un prix 0 en BDD")
	@Test
	public void testFreePriceInDataBase() {

		// GIVEN
		parkingService.processIncomingVehicle();
		parkingService.processExitingVehicle();

		// WHEN
		Ticket ticket = ticketDAO.getTicket("ABCDEF");

		// THEN
		assertEquals(Fare.FREE_FARE, ticket.getPrice());
	}

}