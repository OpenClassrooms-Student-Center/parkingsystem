package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;


import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ParkingServiceTest {

    private static ParkingService parkingService;

    @Mock
    private static InputReaderUtil inputReaderUtil;
    @Mock
    private static ParkingSpotDAO parkingSpotDAO;
    @Mock
    private static TicketDAO ticketDAO;

    String vehicleRegNumber;

    @BeforeEach
    public void setUpPerTest() {
        vehicleRegNumber = "ABCDEF";

        try {


            parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);

        } catch (Exception e) {
            e.printStackTrace();
            throw  new RuntimeException("Failed to set up test mock objects");
        }

    }

    @Test
    public void processExitingVehicleTest() throws Exception {

        Ticket ticket = new Ticket();

        when(ticketDAO.getTicket(anyString())).thenReturn(ticket);

        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");

        when(ticketDAO.getNBTicket(vehicleRegNumber)).thenReturn(2);

        parkingService.processExitingVehicle();

        verify(ticketDAO, times(1)).getNBTicket(vehicleRegNumber);

    }

    // Test 01
    @Test
    public void testProcessIncomingVehicle() {

        when(inputReaderUtil.readSelection()).thenReturn(1);

        parkingService.processIncomingVehicle();


        verify(inputReaderUtil, times(1)).readSelection();

    }

    // Test 02
    @Test
    public void processExitingVehicleTestUnableUpdate() throws Exception {

        Ticket ticket = new Ticket();
        ticket.setParkingSpot(new ParkingSpot(1, ParkingType.CAR, false));
        ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
        ticket.setVehicleRegNumber("ABCDEF");

        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn(vehicleRegNumber);
        when(ticketDAO.getTicket(vehicleRegNumber)).thenReturn(ticket);
        when(ticketDAO.updateTicket(ticket)).thenReturn(false);

        parkingService.processExitingVehicle();

        verify(ticketDAO, times(1)).updateTicket(ticket);
        verify(parkingSpotDAO, never()).updateParking(any(ParkingSpot.class));

    }

    // Test 03
    @Test
    public void testGetNextParkingNumberIfAvailable() {

        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).thenReturn(1);

        ParkingSpot parkingSpot = parkingService.getNextParkingNumberIfAvailable();

        verify(parkingSpotDAO, times(1)).getNextAvailableSlot(ParkingType.CAR);
        assertNotNull(parkingSpot);
        assertEquals(1,parkingSpot.getId());
        assertEquals(ParkingType.CAR,parkingSpot.getParkingType());

    }

    // Test 04
    @Test
    public void testGetNextParkingNumberIfAvailableParkingNumberNotFound() {

        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).thenReturn(-1);

        ParkingSpot  parkingSpot = parkingService.getNextParkingNumberIfAvailable();

        verify(parkingSpotDAO, times(1)).getNextAvailableSlot(ParkingType.CAR);
        assertNull(parkingSpot);
    }

    // Test 05
    @Test
    public void testGetNextParkingNumberIfAvailableParkingNumberWrongArgument() {

        when(inputReaderUtil.readSelection()).thenReturn(3);

        ParkingSpot  parkingSpot = parkingService.getNextParkingNumberIfAvailable();

        verify(inputReaderUtil, times(1)).readSelection();
        assertNull(parkingSpot);
    }

    // Test N 01 T 07
    @Test
    void testProcessIncomingVehicleNoSpot() throws Exception {
        // Simuler une saisie utilisateur correcte

        when(inputReaderUtil.readSelection()).thenReturn(1); // 1 = CAR, 2 = BIKE

        // Simuler qu'il n'y a pas de place disponible
        when(parkingSpotDAO.getNextAvailableSlot(any())).thenReturn(-1);

        parkingService.processIncomingVehicle();

        // Vérifier que le ticket N'EST PAS enregistré car pas de place dispo
        verify(ticketDAO, never()).saveTicket(any());
    }

    // Test N 02 T 08
    @Test
    public void testProcessExitingVehicleFailure() throws Exception {
        when(ticketDAO.getTicket(vehicleRegNumber)).thenReturn(null);

        parkingService.processExitingVehicle();

        verify(ticketDAO, never()).updateTicket(any());
    }
    // Test N 03 T 09
    @Test
    public void testCalculateFareForRecurringUser() throws Exception {
        Ticket ticket = new Ticket();
        ticket.setInTime(new Date(System.currentTimeMillis() - 3600000));
        ticket.setVehicleRegNumber(vehicleRegNumber);
        ticket.setParkingSpot(new ParkingSpot(1, ParkingType.CAR, false));

        when(ticketDAO.getTicket(vehicleRegNumber)).thenReturn(ticket);
        when(ticketDAO.updateTicket(ticket)).thenReturn(true);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn(vehicleRegNumber);

        parkingService.processExitingVehicle();
        assertTrue(ticket.getPrice() > 0, "Price should be calculated with discount");
    }
    // Test N 04 T 10
    @Test
    public void testProcessIncomingVehicleInvalidSelection() {
        when(inputReaderUtil.readSelection()).thenReturn(999);
        ParkingSpot parkingSpot = parkingService.getNextParkingNumberIfAvailable();
        assertNull(parkingSpot);
    }
    // Test N 05 T 11
    @Test
    public void testProcessIncomingVehicleBike() {
        when(inputReaderUtil.readSelection()).thenReturn(2);
        when(parkingSpotDAO.getNextAvailableSlot(ParkingType.BIKE)).thenReturn(2);

        parkingService.processIncomingVehicle();

        verify(ticketDAO).saveTicket(any());
    }
    // Test N 06 T 12
    @Test
    public void testProcessIncomingVehicleCar() {
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).thenReturn(1);

        parkingService.processIncomingVehicle();

        verify(ticketDAO).saveTicket(any());
    }

}

