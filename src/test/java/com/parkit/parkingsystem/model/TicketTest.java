package com.parkit.parkingsystem.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("TicketModelTest")
public class TicketTest {
	/**
	 * The Ticket.
	 */
	Ticket ticket = null;
	/**
	 * The Parking spot.
	 */
	ParkingSpot parkingSpot;

	/**
	 * Test ticket.
	 */
	@Test
	public void testTicket() {
		/**
		 * GIVEN Initialized Ticket.
		 */
		ticket = new Ticket();
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
		Date outTime = new Date();

		/**
		 * WHEN a ticket is set
		 */
		ticket.setAReccuringUser(false);
		ticket.setId(1234);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setPrice(1.50);
		ticket.setDiscount(1.43);
		ticket.setVehicleRegNumber("abcdef");
		ticket.setParkingSpot(parkingSpot);
		/**
		 * THEN verify ticket has the right information in all the getMethod()
		 */
		assertEquals(ticket.getAReccuringUser(), false);
		assertEquals(ticket.getId(), 1234);
		assertEquals(ticket.getVehicleRegNumber(), "abcdef");
		assertEquals(ticket.getInTime(), inTime);
		assertEquals(ticket.getOutTime(), outTime);
		assertEquals(ticket.getPrice(), 1.50);
		assertEquals(ticket.getDiscount(), 1.43);
		assertEquals(ticket.getParkingSpot(), parkingSpot);

	}
}
