package com.parkit.parkingsystem.integration.dao;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.model.ParkingSpot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Date;

import static org.mockito.Mockito.*;

public class TicketDAOTest {

    private TicketDAO ticketDAO;

    @Mock
    private DataBaseConfig mockDataBaseConfig;

    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockPreparedStatement;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = mockDataBaseConfig;

        when(mockDataBaseConfig.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(any(String.class))).thenReturn(mockPreparedStatement);
    }

    @Test
    public void testSaveTicket() throws Exception {
        Ticket ticket = new Ticket();
        ticket.setParkingSpot(new ParkingSpot(1, null, true));
        ticket.setVehicleRegNumber("ABC123");
        ticket.setPrice(2.0);
        ticket.setInTime(new Date());
        ticket.setOutTime(new Date());

        when(mockPreparedStatement.execute()).thenReturn(true);

        ticketDAO.saveTicket(ticket);

        verify(mockPreparedStatement, times(1)).execute();
    }
}





