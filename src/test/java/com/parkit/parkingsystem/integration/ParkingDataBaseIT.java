package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.constants.Fare;
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
import static org.assertj.core.api.AssertionsForClassTypes.offset;
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
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");

        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);

        parkingService.processIncomingVehicle();
        Thread.sleep(2000);
        parkingService.processExitingVehicle();

        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        parkingService.processIncomingVehicle();
        Thread.sleep(2000);
        parkingService.processExitingVehicle();

        Ticket ticket = ticketDAO.getTicket("ABCDEF");

        assertThat(ticket).isNotNull();
        assertThat(ticket.getOutTime()).isNotNull();

        double duration = (ticket.getOutTime().getTime() - ticket.getInTime().getTime()) / (60.0 * 60.0 * 1000.0);
        double expected = duration * Fare.CAR_RATE_PER_HOUR * 0.95;
        assertThat(ticket.getPrice()).isEqualTo(expected, offset(0.01));
    }
}