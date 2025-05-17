package com.parkit.parkingsystem.integration.model;

import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.model.ParkingSpot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class TicketTest {

    private Ticket ticket;

    @BeforeEach
    public void setUp() {
        ticket = new Ticket();
        ticket.setId(1);
        ticket.setVehicleRegNumber("ABC123");
        ticket.setParkingSpot(new ParkingSpot(1, null, false));

        Date outTime = new Date();
        Date inTime = new Date(outTime.getTime() - 60 * 60 * 1000);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setDiscount(true);
        ticket.setPrice(1.5);
    }

    @Test
    public void testGetDurationInMinutes() {
        int minutes = ticket.getDurationInMinutes();
        assertEquals(60, minutes);
    }

    @Test
    public void testGetDurationInHours() {
        int hours = ticket.getDurationInHours();
        assertEquals(1, hours);
    }

    @Test
    public void testHasDiscount() {
        assertTrue(ticket.hasDiscount());
    }

    @Test
    public void testToStringNotNull() {
        assertNotNull(ticket.toString());
        assertTrue(ticket.toString().contains("ABC123"));
    }
}
