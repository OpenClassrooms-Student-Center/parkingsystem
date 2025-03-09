package com.parkit.parkingsystem.model;

import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.Test;
import java.util.Date;

class TicketTest {

    @Test
    void testTicketGettersAndSetters() {
        Ticket ticket = new Ticket();
        ticket.setId(1);
        ticket.setVehicleRegNumber("ABC123");
        ticket.setPrice(10.5);
        ticket.setInTime(new Date());
        ticket.setOutTime(new Date());

        assertEquals(1, ticket.getId());
        assertEquals("ABC123", ticket.getVehicleRegNumber());
        assertEquals(10.5, ticket.getPrice(), 0.01);
        assertNotNull(ticket.getInTime());
        assertNotNull(ticket.getOutTime());
    }
}
