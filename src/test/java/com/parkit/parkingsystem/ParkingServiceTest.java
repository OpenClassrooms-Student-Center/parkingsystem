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
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;

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

    private Ticket carTicket;

    @BeforeEach
    public void setUpPerTest() {
        try {
            ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
            carTicket = new Ticket();
            carTicket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
            carTicket.setParkingSpot(parkingSpot);
            carTicket.setVehicleRegNumber("ABCDEF");

            parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set up test mock objects");
        }
    }

    /**
     * Test pour traiter la sortie d'un véhicule.
     * Vérifie que les méthodes appropriées sont appelées lors de la sortie d'un véhicule enregistré.
     */
    @Test
    public void processExitingVehicleTest() {
        try {
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        when(ticketDAO.getTicket(anyString())).thenReturn(carTicket);
        when(ticketDAO.checkRecurrentCustomer("ABCDEF")).thenReturn(true);
        when(ticketDAO.updateTicket(carTicket)).thenReturn(true);

        parkingService.processExitingVehicle();

        verify(ticketDAO, Mockito.times(1)).getTicket(anyString());
        verify(ticketDAO, Mockito.times(1)).checkRecurrentCustomer("ABCDEF");
        verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
    }

    /**
     * Test pour traiter l'entrée d'un véhicule.
     * Vérifie que les méthodes appropriées sont appelées lors de l'entrée d'un véhicule.
     */
    @Test
    public void processIncomingVehicleTest() {
        try {
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);
        when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);
        when(inputReaderUtil.readSelection()).thenReturn(1);

        parkingService.processIncomingVehicle();

        verify(parkingSpotDAO, times(1)).getNextAvailableSlot(any(ParkingType.class));
        verify(parkingSpotDAO, times(1)).updateParking(any(ParkingSpot.class));
    }

    /**
     * Test pour traiter la sortie d'un véhicule lorsque la mise à jour échoue.
     * Vérifie que la mise à jour du ticket ne se produit pas si la mise à jour échoue.
     */
    @Test
    public void processExitingVehicleTestUnableUpdate() {
        try {
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        when(ticketDAO.getTicket(anyString())).thenReturn(carTicket);
        when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(false);

        parkingService.processExitingVehicle();

        verify(ticketDAO, times(1)).getTicket(anyString());
        verify(ticketDAO, times(1)).updateTicket(any(Ticket.class));
        verify(parkingSpotDAO, never()).updateParking(any(ParkingSpot.class));
    }

    /**
     * Test pour traiter la sortie d'un véhicule avec une date spécifique lorsque la mise à jour échoue.
     * Vérifie que la mise à jour du ticket ne se produit pas si la mise à jour échoue.
     */
    @Test
    public void processExitingVehicleDatedTestUnableUpdate() {
        try {
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        when(ticketDAO.getTicket(anyString())).thenReturn(carTicket);
        when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(false);

        parkingService.processExitingVehicle(new Date(System.currentTimeMillis()));

        verify(ticketDAO, times(1)).getTicket(anyString());
        verify(ticketDAO, times(1)).updateTicket(any(Ticket.class));
        verify(parkingSpotDAO, never()).updateParking(any(ParkingSpot.class));
    }

    /**
     * Test pour obtenir le prochain numéro de parking disponible.
     * Vérifie que le numéro de parking retourné est correct lorsqu'il est disponible.
     */
    @Test
    public void testGetNextParkingNumberIfAvailable() {
        when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);
        when(inputReaderUtil.readSelection()).thenReturn(1);

        ParkingSpot parkingNumber = parkingService.getNextParkingNumberIfAvailable();

        assertThat(parkingNumber.getId()).isEqualTo(1);

        verify(parkingSpotDAO, times(1)).getNextAvailableSlot(ParkingType.CAR);
    }

    /**
     * Test pour obtenir le prochain numéro de parking disponible lorsque le numéro de parking n'est pas trouvé.
     * Vérifie que la méthode retourne null si aucun numéro de parking n'est disponible.
     */
    @Test
    public void testGetNextParkingNumberIfAvailableParkingNumberNotFound() {
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).thenReturn(-1);

        ParkingSpot parkingNumber = parkingService.getNextParkingNumberIfAvailable();

        assertThat(parkingNumber).isNull();

        verify(parkingSpotDAO, times(1)).getNextAvailableSlot(ParkingType.CAR);
    }

    /**
     * Test pour obtenir le prochain numéro de parking disponible avec un argument incorrect.
     * Vérifie que la méthode ne tente pas d'obtenir un numéro de parking si la sélection est incorrecte.
     */
    @Test
    public void testGetNextParkingNumberIfAvailableParkingNumberWrongArgument() {
        when(inputReaderUtil.readSelection()).thenReturn(3);

        ParkingSpot parkingNumber = parkingService.getNextParkingNumberIfAvailable();

        assertThat(parkingNumber).isNull();

        verify(parkingSpotDAO, never()).getNextAvailableSlot(any(ParkingType.class));
    }
}
