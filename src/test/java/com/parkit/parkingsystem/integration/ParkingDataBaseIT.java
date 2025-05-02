package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingSpotDAO parkingSpotDAO;
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;

    @Mock
    private InputReaderUtil inputReaderUtil;

    @BeforeAll
    public static void setUp() throws Exception {
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
    }

    @BeforeEach
    public void setUpPerTest() throws Exception {
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        dataBasePrepareService.clearDataBaseEntries();
    }

    @AfterAll
    public static void tearDown() {

    }

    @Test
    void testParkingACar() throws Exception {
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");

        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processIncomingVehicle();

        Ticket ticket = ticketDAO.getTicket("ABCDEF");

        assertThat(ticket).isNotNull();
        assertThat(ticket.getVehicleRegNumber()).isEqualTo("ABCDEF");

        int nextAvailableSlot = parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);
        assertNotEquals(nextAvailableSlot, ticket.getParkingSpot().getId(), "The parking space should be marked as not available.");
    }

    @Test
    public void testParkingLotExit() throws Exception {
        testParkingACar();
        Thread.sleep(1000);
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processExitingVehicle();

        Ticket ticket = ticketDAO.getTicket("ABCDEF");
        assertThat(ticket).isNotNull();
        assertThat(ticket.getOutTime()).isNotNull();

    }


    @Test
    public void testParkingLotExitRecurringUser() throws Exception {
        testParkingACar();

        // İlk çıkış işlemi
        Thread.sleep(1000);
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processExitingVehicle();

        // Ticket bilgilerini al
        Ticket ticket = ticketDAO.getTicket("ABCDEF");
        assertThat(ticket).isNotNull();
        assertThat(ticket.getOutTime()).isNotNull();

        // Şimdi aynı araç ile tekrar giriş yap
        // 1. araç tekrar gelir
        testParkingACar();

        // 2. çıkış işlemi yapılır
        Thread.sleep(1000);
        parkingService.processExitingVehicle();

        // Yeniden ticket bilgilerini al
        Ticket updatedTicket = ticketDAO.getTicket("ABCDEF");
        assertThat(updatedTicket).isNotNull();
        assertThat(updatedTicket.getOutTime()).isNotNull();

        // İkinci kez çıkışta %5 indirim uygulanıp uygulanmadığını kontrol et
        assertThat(updatedTicket.getPrice()).isLessThan(ticket.getPrice()); // İkinci çıkış fiyatı daha düşük olmalı
        assertThat(updatedTicket.getPrice()).isEqualTo(ticket.getPrice() * 0.95); // Fiyatın %5 indirimli olduğunu doğrulamak
    }
}