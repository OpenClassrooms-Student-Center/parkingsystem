package com.parkit.parkingsystem.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class TicketTest {
    @Test
    public void ticketSpotFalseEquals() {
        Ticket ticketIn = new Ticket();
        ticketIn.getInTime();
        Ticket ticketOut = new Ticket();
        ticketOut.getOutTime();

        assertNotEquals(ticketIn, ticketOut);

    }

    @Test
    public void ticketSpotTrueEquals() {
        Ticket ticket = new Ticket();
        ticket.setVehicleRegNumber("ABCDEF");

        assertEquals("ABCDEF", ticket.getVehicleRegNumber());
    }

}
