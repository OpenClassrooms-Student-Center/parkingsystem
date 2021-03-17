package com.parkit.parkingsystem.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;

public class TicketDAOTest {

	public static final String vehicleReg = "TOTO";

	@DisplayName("Test pour sauvegarder un ticket en BDD")
	@Test
	public void saveTicketTest() {

		// GIVEN
		Ticket ticket = new Ticket();
		ticket.setAReccuringUser(false);
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
		ticket.setInTime(inTime);
		ticket.setParkingSpot(new ParkingSpot(2, ParkingType.CAR, false));
		ticket.setVehicleRegNumber(vehicleReg);
		ticket.setPrice(0);

		// WHEN
		TicketDAO ticketDAO = new TicketDAO();
		boolean result = ticketDAO.saveTicket(ticket);

		// THEN
		assertEquals(false, result);
	}

	@DisplayName("Test pour recuperer un ticket dans une BDD")
	@Test
	public void getTicketTest() {

		// GIVEN
		TicketDAO ticketDAO = new TicketDAO();

		// WHEN
		Ticket ticketReturned = ticketDAO.getTicket(vehicleReg);

		// THEN
		assertEquals(ticketReturned.getClass(), Ticket.class);
	}

	@DisplayName("Test pour mettre à jour un ticket en BDD")
	@Test
	public void updateTicketTest() {

		// GIVEN
		TicketDAO ticketDAO = new TicketDAO();

		// WHEN
		Ticket ticketReturned = ticketDAO.getTicket(vehicleReg);
		ticketReturned.setOutTime(new Date(System.currentTimeMillis()));

		// THEN
		assertEquals(true, ticketDAO.updateTicket(ticketReturned));
	}

	@DisplayName("Test pour vérifier un user reccurent en BDD")
	@Test
	public void isReccurentUser() throws SQLException, ClassNotFoundException, IOException {

		// GIVEN
		TicketDAO ticketDAO = new TicketDAO();
		Ticket ticket = new Ticket();

		// WHEN
		ticket.setVehicleRegNumber("ABCDEF");
		boolean isTrue = ticketDAO.isReccurentUser(ticket.getVehicleRegNumber());

		// THEN
		assertTrue(isTrue);

	}

}
