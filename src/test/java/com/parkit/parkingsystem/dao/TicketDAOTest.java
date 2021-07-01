package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class TicketDAOTest {
    private static TicketDAO ticketDAO;
    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingSpotDAO parkingSpotDAO;
    private static DataBasePrepareService dataBasePrepareService;
    Ticket ticket = new Ticket();

    @Mock
    private static ParkingSpot parkingSpot;
    @Mock
    Connection con = null;

    @BeforeEach
    public void setUp() {
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
    }
    @AfterEach
    public void tearDown(){
        dataBaseTestConfig.closeConnection(con);

    }


    @Test
    public void saveTicketTest() throws Exception {
        Date date = new Date();

        try{

            ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,true);
        Timestamp timeStamp = new Timestamp(System.currentTimeMillis());
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("ABCDEF");
        ticket.setInTime(new Date(System.currentTimeMillis() - (60*60*1000)));
        ticket.setOutTime(date);
        ticket.setPrice(2.5);
    } catch (Exception e) {
        e.printStackTrace();
        throw  new RuntimeException("Failed to set up test mock objects");
    }
       boolean saved = ticketDAO.saveTicket(ticket);
        assertFalse(saved);

    }

    @Test
    public void getTicketTest() {
        Ticket ticket = new Ticket();
        Timestamp timeStamp = new Timestamp(System.currentTimeMillis());
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        ticket.setParkingSpot(parkingSpot);
        ticket.setId(1);
        ticket.setVehicleRegNumber("ABCDEF");
        ticket.setPrice(2.3);
        ticket.setInTime(timeStamp);
        ticket.setOutTime(timeStamp);
        assertThat(ticketDAO.getTicket("ABCDEF"));
    }
    @Test
    void updateTicket() {
        Ticket ticket = new Ticket();
        Timestamp timeStamp = new Timestamp(System.currentTimeMillis());
        ticket.setVehicleRegNumber("ABCDEF");
        ticket.setOutTime(timeStamp);
        ticket.setPrice(2.5);
        assertTrue(ticketDAO.updateTicket(ticket));
    }

    @Test
    void isRecurring() {
        boolean isRecurring = ticketDAO.isRecurring("ABCDEF");
        assertTrue(isRecurring);
    }

    @Test
   public void isSavedTest() {
        boolean isSaved = ticketDAO.isSaved("ABCDEF");
        assertTrue(isSaved);

    }
}
