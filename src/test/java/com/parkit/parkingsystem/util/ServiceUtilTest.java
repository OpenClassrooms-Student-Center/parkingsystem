package com.parkit.parkingsystem.util;


import com.parkit.parkingsystem.service.FareCalculatorService;
import com.parkit.parkingsystem.service.ParkingService;

import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.constants.ParkingType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;



import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ServiceUtilTest {

    @Mock
    private InputReaderUtil inputReaderUtil;

    @Mock
    private TicketDAO ticketDAO;

    @InjectMocks
    private ParkingService parkingService;

    private Ticket ticket;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        ticket = new Ticket();
        ticket.setInTime(new Date(System.currentTimeMillis() - 3600 * 1000));
        ticket.setParkingSpot(new ParkingSpot(1, ParkingType.CAR, false));
    }

    @Test
    public void testInputReaderUtil() throws Exception {
        when(inputReaderUtil.readSelection()).thenReturn(1);
        int result = inputReaderUtil.readSelection();
        assertEquals(1, result);
    }

    @Test
    public void shouldHandleExitingVehicleCorrectly() throws Exception {
        when(ticketDAO.getTicket(any())).thenReturn(ticket);
        when(ticketDAO.updateTicket(any())).thenReturn(true);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABC123");

        assertDoesNotThrow(() -> {
            try {
                parkingService.processExitingVehicle();
            } catch (Exception e) {
                throw new RuntimeException("Exception inattendue : " + e.getMessage(), e);
            }
        });

        verify(ticketDAO).getTicket(any());
        verify(ticketDAO).updateTicket(any());
        assertNotNull(ticket.getOutTime(), "L'heure de sortie ne doit pas être null");
        assertTrue(ticket.getOutTime().after(ticket.getInTime()), "L'heure de sortie doit être postérieure à l'heure d'entrée");
    }



    @Test
    void testFareCalculatorServiceCalculateFare() {
        Ticket ticket = new Ticket();
        ticket.setInTime(new Date(System.currentTimeMillis() - 3600 * 1000));
        ticket.setOutTime(new Date());
        ticket.setParkingSpot(new ParkingSpot(1, ParkingType.CAR, false));
        new FareCalculatorService().calculateFare(ticket);
        assertNotNull(ticket.getPrice(), "Le prix ne doit pas être null");
    }

}
