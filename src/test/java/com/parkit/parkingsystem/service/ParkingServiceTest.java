package com.parkit.parkingsystem.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.util.InputReaderUtil;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test for the parking Service")
public class ParkingServiceTest {

	/** The parking service. */
	private static ParkingService parkingService;
	private ParkingSpot parkingSpot;
	private Ticket ticket;

	/** The input reader util. */
	@Mock
	private static InputReaderUtil inputReaderUtil;

	/** The parking spot DAO. */
	@Mock
	private static ParkingSpotDAO parkingSpotDAO;

	/** The ticket DAO. */
	@Mock
	private static TicketDAO ticketDAO;

	/**
	 * Sets the up per test.
	 */
	@BeforeEach
	private void setUpPerTest() {
		try {
			// when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
			ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
			Ticket ticket = new Ticket();
			ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
			ticket.setParkingSpot(parkingSpot);
			ticket.setVehicleRegNumber("ABCDEF");

			// when(parkingSpotDAO.updateParking(ArgumentMatchers.any(ParkingSpot.class))).thenReturn(true);

			parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to set up test mock objects");
		}
	}

	/**
	 * Process exiting vehicle test.
	 *
	 * @throws Exception the exception
	 */
	@DisplayName("Test la sortie d'une vehicule")
	@Test
	public void processExitingVehicleTest() throws Exception {
		/*
		 * GIVEN: an user want to exit his vehicle from the parking
		 */
		when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
		when(parkingSpotDAO.updateParking(ArgumentMatchers.any(ParkingSpot.class))).thenReturn(true);
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		/*
		 * WHEN a ticket is set
		 */
		Ticket ticket = new Ticket();
		ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
		ticket.setParkingSpot(parkingSpot);
		ticket.setVehicleRegNumber("ABCDEF");
		when(ticketDAO.getTicket(ArgumentMatchers.anyString())).thenReturn(ticket);
		when(ticketDAO.updateTicket(ArgumentMatchers.any(Ticket.class))).thenReturn(true);
		parkingService.processExitingVehicle();

		/*
		 * THEN verify the system update parking spot available
		 */
		Mockito.verify(parkingSpotDAO, Mockito.times(1)).updateParking(ArgumentMatchers.any(ParkingSpot.class));
	}

	/**
	 * Process incoming car test.
	 */
	@DisplayName("Test pour vérifier une entree pour une voitures si le parkingSpot est MAJ et le ticket enregistree")
	@Test
	public void processIncomingCarTest() {
		/*
		 * Given: an user want to park his car in the parking
		 */
		when(inputReaderUtil.readSelection()).thenReturn(1);

		/*
		 * When the same user get any spot and the parking process for incoming car is
		 * done
		 */
		when(parkingSpotDAO.getNextAvailableSlot((ParkingType.CAR))).thenReturn(1);
		when(ticketDAO.saveTicket(ArgumentMatchers.any())).thenReturn(true);
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
		Ticket ticket = new Ticket();
		ticket.setInTime(inTime);
		parkingService.processIncomingVehicle();

		/*
		 * Then verify the parkingSpot is update and the ticket saved in DAO
		 */
		Mockito.verify(parkingSpotDAO, Mockito.times(1)).updateParking(ArgumentMatchers.any(ParkingSpot.class));
		Mockito.verify(ticketDAO, Mockito.times(1)).saveTicket(ArgumentMatchers.any(Ticket.class));
	}

	/**
	 * Process incoming bike test.
	 */
	@DisplayName("Test pour vérifier une entree pour un vélo si le parkingSpot est MAJ et le ticket enregistree")
	@Test
	public void processIncomingBikeTest() {
		/*
		 * Given: an user want to park his bike in the parking
		 */
		when(inputReaderUtil.readSelection()).thenReturn(2);
		when(parkingSpotDAO.getNextAvailableSlot((ParkingType.BIKE))).thenReturn(4);

		/*
		 * When the same user get any spot and the parking process for incoming car is
		 * done
		 */
		parkingService.processIncomingVehicle();

		/*
		 * Then verify the parkingSpot is update and the ticket saved in DAO
		 */
		Mockito.verify(parkingSpotDAO, Mockito.times(1)).updateParking(ArgumentMatchers.any(ParkingSpot.class));
		Mockito.verify(ticketDAO, Mockito.times(1)).saveTicket(ArgumentMatchers.any(Ticket.class));
	}

	/*
	 * Gets the vehichle type with incorrect input should return an exception to get
	 * type.
	 *
	 * @return the vehichle type with incorrect input should return an exception to
	 * get type
	 * 
	 * @throws NoSuchMethodException the no such method exception
	 */
	@Test
	@DisplayName("Test pour vérifier si une exception est levée si l'input est incorrect ")
	public void getVehichleTypeWithIncorrectInputShouldReturnAnExceptionToGetType() throws NoSuchMethodException {

		when(inputReaderUtil.readSelection()).thenReturn(10);
		parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);

		Method m = ParkingService.class.getDeclaredMethod("getVehichleType");
		m.setAccessible(true);

		assertThrows(InvocationTargetException.class, () -> m.invoke(parkingService));

	}

	/**
	 * Test to verify the correct exception if the parking is full.
	 */
	@DisplayName("Test pour vérifier si une exception est levée si le parking est plein correct")
	@Test
	public void testToVerifyTheCorrectExceptionIfTheParkingIsFull() {
		when(inputReaderUtil.readSelection()).thenReturn(2);
		parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		ParkingType parkingType = ParkingType.BIKE;
		when(parkingSpotDAO.getNextAvailableSlot(parkingType)).thenReturn(0);
		try {
			parkingService.getNextParkingNumberIfAvailable();
		} catch (Exception e) {
			String message = e.getMessage();
			assertTrue(message.contains("Error fetching next available parking slot"));
		}
	}

	/*
	 * Gets the vehichle type with incorrect input should return an exception for
	 * getting the next available spot.
	 *
	 * @return the vehichle type with incorrect input should return an exception for
	 * getting the next available spot
	 */
	@Test
	@DisplayName("Test pour vérifier une exception levée si l'input du type du véhicule est incorrect ")
	public void getVehichleTypeWithIncorrectInputShouldReturnAnExceptionForGettingTheNextAvailableSpot() {
		// ARRANGE
		when(inputReaderUtil.readSelection()).thenReturn(3);
		parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		try {
			parkingService.getNextParkingNumberIfAvailable();
		} catch (Exception e) {
			String message = e.getMessage();
			System.out.println(message);
			assertTrue(message.contains("Error parsing user input for type of vehicle"));

		}
	}

}
