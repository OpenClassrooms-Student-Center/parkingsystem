package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.constants.ParkingType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class TicketDAOTest {

    private TicketDAO ticketDAO;

    @BeforeEach
    void setUp() {
        ticketDAO = new TicketDAO();

    }

    @Test
    void testSaveTicket() {
        Ticket ticket = new Ticket();
        ticket.setVehicleRegNumber("TEST123");
        ticket.setPrice(5.0);
        ticket.setInTime(new Date());
        ticket.setParkingSpot(new ParkingSpot(1, ParkingType.CAR, false));

        boolean result = ticketDAO.saveTicket(ticket);
        assertTrue(result, "Le ticket doit être sauvegardé en base.");
    }

    @Test
    void testGetTicket() {
        Ticket ticket = ticketDAO.getTicket("TEST123");
        assertNotNull(ticket, "Le ticket doit exister en base");
    }

    @Test
    void testUpdateTicket() {
        Ticket ticket = ticketDAO.getTicket("TEST123");
        ticket.setOutTime(new Date());
        ticket.setPrice(10.0);

        boolean result = ticketDAO.updateTicket(ticket);
        assertTrue(result, "La mise à jour du ticket doit réussir.");
    }

    @Test
    void testGetNbTicket() {

        ticketDAO.clearAllTickets();
        // Simule un premier ticket enregistré
        Ticket ticket = new Ticket();
        ticket.setVehicleRegNumber("TEST123");
        ticket.setParkingSpot(new ParkingSpot(1, ParkingType.CAR, false));
        ticket.setInTime(new Date());
        ticketDAO.saveTicket(ticket);

        int nbTicketsAfterFirstEntry = ticketDAO.getNBTicket("TEST123");
        assertEquals(1, nbTicketsAfterFirstEntry, "Après une première entrée, l'utilisateur doit avoir 1 ticket.");

        // Simule une deuxième entrée du même utilisateur
        Ticket ticket2 = new Ticket();
        ticket2.setVehicleRegNumber("TEST123");
        ticket2.setParkingSpot(new ParkingSpot(1, ParkingType.CAR, false));
        ticket2.setInTime(new Date());
        ticketDAO.saveTicket(ticket2);

        int nbTicketsAfterSecondEntry = ticketDAO.getNBTicket("TEST123");
        assertTrue(nbTicketsAfterSecondEntry > 1, "Après plusieurs entrées, l'utilisateur doit être considéré comme récurrent.");
    }



}
