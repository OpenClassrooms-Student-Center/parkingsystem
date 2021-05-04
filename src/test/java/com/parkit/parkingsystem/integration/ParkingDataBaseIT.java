package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.apache.tomcat.jni.Time;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes=ParkingSpotDAO.class)
@AutoConfigureMockMvc
public class ParkingDataBaseIT {

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingSpotDAO parkingSpotDAO;
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;
    public Ticket ticket = new Ticket();

    @MockBean
    private InputReaderUtil inputReaderUtil;

    @BeforeAll
    private static void setUp() throws Exception{
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
    }

    @AfterEach
    private  void tearDown(){
        dataBasePrepareService.clearDataBaseEntries();
    }

    @Test
    public void testParkingACar() throws Exception {
        when(inputReaderUtil.readSelection()).thenReturn(2);
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        this.ticket.setVehicleRegNumber("1111");
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("1111");
        parkingService.processIncomingVehicle();
        Ticket ticketSavedInDB = ticketDAO.getTicket(ticket.getVehicleRegNumber());
        assertNotNull(ticketSavedInDB);
        assertNotNull(ticketSavedInDB.getInTime());
        assertNotNull(ticketSavedInDB.getParkingSpot());
        assertFalse(ticketSavedInDB.getParkingSpot().isAvailable());
    }

    @Test
    public void testCarExit() throws Exception {
        when(inputReaderUtil.readSelection()).thenReturn(2);
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        this.ticket.setVehicleRegNumber("1111");
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("1111");
        parkingService.processIncomingVehicle();
        TimeUnit.MILLISECONDS.sleep(200);
        parkingService.processExitingVehicle();
        Ticket ticketSavedInDB = ticketDAO.getTicket(ticket.getVehicleRegNumber());
        assertNotNull(ticketSavedInDB);
        assertNotNull(ticketSavedInDB.getOutTime());
        assertNotNull(ticketSavedInDB.getParkingSpot());
        assertNotNull(ticketSavedInDB.getPrice());
    }

}
