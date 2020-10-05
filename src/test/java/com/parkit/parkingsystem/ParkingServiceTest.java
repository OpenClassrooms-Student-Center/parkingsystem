package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.internal.matchers.text.ValuePrinter.print;

@ExtendWith(MockitoExtension.class)
public class ParkingServiceTest {

    private static ParkingService parkingService;

    @Mock
    private static InputReaderUtil inputReaderUtil;
    @Mock
    private static ParkingSpotDAO parkingSpotDAO;
    @Mock
    private static TicketDAO ticketDAO;

    @Test
    public void processIncomingVehicleCarTest() throws Exception {
        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        Ticket ticket = new Ticket();
        ticket.setParkingSpot(new ParkingSpot(1, ParkingType.CAR,false));
        ticket.setVehicleRegNumber("ABCDEF");
        ticket.setPrice(0);
        ticket.setInTime(LocalDateTime.now());
        ticket.setOutTime(null);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).thenReturn(1);
        when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);
        when(ticketDAO.saveTicket(any(Ticket.class))).thenReturn(true);

        parkingService.processIncomingVehicle();
        verify(ticketDAO, Mockito.times(1)).saveTicket(any(Ticket.class));
    }

    @Test
    public void processIncomingVehicleExceptionTest() {
        try {
            parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
            parkingService.processIncomingVehicle();
        } catch (Exception e){
            assertThrows(Exception.class, ()-> parkingService.processIncomingVehicle());
        }
    }

    @Test
    public void getNextParkingNumberIfAvailableCarTest() throws Exception{
        ParkingType parkingType = ParkingType.CAR;
        ParkingSpot parkingSpot =  new ParkingSpot(1, parkingType, true);
        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);

        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(parkingSpotDAO.getNextAvailableSlot(parkingType)).thenReturn(1);

        assertEquals(parkingSpot, parkingService.getNextParkingNumberIfAvailable());
    }

    @Test
    public void getNextParkingNumberIfAvailableBikeTest() throws Exception {
        ParkingType parkingType = ParkingType.BIKE;
        ParkingSpot parkingSpot =  new ParkingSpot(1, parkingType, true);
        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);

        when(inputReaderUtil.readSelection()).thenReturn(2);
        when(parkingSpotDAO.getNextAvailableSlot(parkingType)).thenReturn(1);

        assertEquals(parkingSpot, parkingService.getNextParkingNumberIfAvailable());
    }

    @Test
    public void getNextParkingNumberIfAvailableDefaultTest(){
        ParkingType parkingType = null;
        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        when(inputReaderUtil.readSelection()).thenReturn(3);

        assertThrows(IllegalArgumentException.class, () -> parkingService.getNextParkingNumberIfAvailable());
    }

    @Test
    public void getNextParkingNumberIfAvailableCarNotAvailableTest() {
        ParkingType parkingType = ParkingType.CAR;
        ParkingSpot parkingSpot =  new ParkingSpot(1, parkingType, true);
        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);

        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(parkingSpotDAO.getNextAvailableSlot(parkingType)).thenReturn(-1);

        assertThrows(Exception.class, () -> parkingService.getNextParkingNumberIfAvailable());
    }


    @Test
    public void processExitingVehicleTest() throws Exception{
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
        Ticket ticket = new Ticket();
        ticket.setInTime(LocalDateTime.now().minusMinutes(60));
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("ABCDEF");
        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);

        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
        when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);
        when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);

        parkingService.processExitingVehicle();
        verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
    }

    @Test
    public void processExitingVehicleExceptionTest() {
        try {
            parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
            parkingService.processExitingVehicle();
        } catch (Exception e){
            assertThrows(Exception.class, ()-> parkingService.processIncomingVehicle());
        }
    }

    @Test
    public void processExitingVehicleElseCaseTest() throws Exception {
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
        Ticket ticket = new Ticket();
        ticket.setInTime(LocalDateTime.now().minusMinutes(60));
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("ABCDEF");
        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);

        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));

        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
        when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(false);

        parkingService.processExitingVehicle();
        assertEquals("Please type the vehicle registration number and press enter key\nUnable to update ticket information. Error occurred",
                outputStreamCaptor.toString().trim());
    }

}
