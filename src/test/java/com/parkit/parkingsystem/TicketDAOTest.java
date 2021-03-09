package com.parkit.parkingsystem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;

import org.junit.jupiter.api.Test;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;

public class TicketDAOTest {

	public static final String vehicleReg = "TOTO";
	private static TicketDAO ticketDAO;

	@Test
	public void saveTicketTest() {

		Ticket ticket = new Ticket();
		// ticket properties.
		ticket.setAReccuringUser(false);
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
		ticket.setInTime(inTime);
		ticket.setParkingSpot(new ParkingSpot(2, ParkingType.CAR, false));
		ticket.setVehicleRegNumber(vehicleReg);
		ticket.setPrice(0);

		TicketDAO ticketDAO = new TicketDAO();
		boolean result = ticketDAO.saveTicket(ticket);

		assertEquals(false, result);
	}

	@Test
	public void getTicketTest() {

		TicketDAO ticketDAO = new TicketDAO();
		Ticket ticketReturned = ticketDAO.getTicket(vehicleReg);
		assertEquals(ticketReturned.getClass(), Ticket.class);
	}

	@Test
	public void updateTicketTest() {

		TicketDAO ticketDAO = new TicketDAO();
		Ticket ticketReturned = ticketDAO.getTicket(vehicleReg);
		ticketReturned.setOutTime(new Date(System.currentTimeMillis()));
		assertEquals(true, ticketDAO.updateTicket(ticketReturned));
	}

	@Test
	public void isReccurentUser() throws SQLException, ClassNotFoundException, IOException {
		TicketDAO ticketDAO = new TicketDAO();
		Ticket ticket = new Ticket();
		ticket.setVehicleRegNumber("ABCDEF");
		boolean isTrue = ticketDAO.isReccurentUser(ticket.getVehicleRegNumber());
		assertTrue(isTrue);

	}

}
