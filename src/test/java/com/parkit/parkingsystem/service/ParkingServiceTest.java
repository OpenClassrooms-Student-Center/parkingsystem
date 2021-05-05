package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ParkingServiceTest {



    @Mock
    private static InputReaderUtil inputReaderUtil;
    @Mock
    private static ParkingSpotDAO parkingSpotDAO;
    @Mock
    private static TicketDAO ticketDAO;
    @InjectMocks
    private static ParkingService parkingService;

    Ticket ticket;

    @BeforeEach
    private void setUpPerTest() {
        try {
            ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
            ticket = new Ticket();
            ticket.setInTime(new Timestamp(System.currentTimeMillis() - (60*60*1000)));
            ticket.setParkingSpot(parkingSpot);
            ticket.setVehicleRegNumber("ABCDEF");
            ticket.setIsRecurrent(false);
        } catch (Exception e) {
            e.printStackTrace();
            throw  new RuntimeException("Failed to set up test mock objects");
        }
    }

    @Test
    public void processExitingVehicleTest() throws Exception {
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        when(ticketDAO.getLastTicket("ABCDEF")).thenReturn(ticket);
        when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);
        when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);
        parkingService.processExitingVehicle();
        verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
    }

    @Test
    public void getNextCarParkingNumberTest() {
        ParkingType parkingType = ParkingType.CAR;
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(parkingSpotDAO.getNextAvailableSlot(parkingType)).thenReturn(1);
        assertEquals( new ParkingSpot(1,parkingType, true), parkingService.getNextParkingNumberIfAvailable());
    }

    @Test
    public void getNextBikeParkingNumberTest() {
        ParkingType parkingType = ParkingType.BIKE;
        when(inputReaderUtil.readSelection()).thenReturn(2);
        when(parkingSpotDAO.getNextAvailableSlot(parkingType)).thenReturn(5);
        assertEquals( new ParkingSpot(5,parkingType, true), parkingService.getNextParkingNumberIfAvailable());
    }

    @Test
    public void getNextCarParkingNumberParkingIsFullTest() {
        ParkingType parkingType = ParkingType.CAR;
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(parkingSpotDAO.getNextAvailableSlot(parkingType)).thenReturn(0);
        assertThrows(Exception.class, (Executable) parkingService.getNextParkingNumberIfAvailable());
    }

    @Test
    public void getNextBikeParkingNumberFullParkingTest() {
        ParkingType parkingType = ParkingType.BIKE;
        when(inputReaderUtil.readSelection()).thenReturn(2);
        when(parkingSpotDAO.getNextAvailableSlot(parkingType)).thenReturn(0);
        assertThrows(Exception.class, (Executable) parkingService.getNextParkingNumberIfAvailable());
    }

    @Test
    public void getNextParkingNumberWrongVehicleTypeTest() {
        when(inputReaderUtil.readSelection()).thenReturn(5);
        assertThrows(Exception.class, (Executable) parkingService.getNextParkingNumberIfAvailable());
    }

}
