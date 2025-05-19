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
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Date;

import static junit.framework.Assert.assertEquals;
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

    @Test
    public void testGetTicket() throws Exception {
        // Arrange
        TicketDAO ticketDAO = new TicketDAO();
        DataBaseConfig mockConfig = mock(DataBaseConfig.class);
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockStmt = mock(PreparedStatement.class);
        ResultSet mockResult = mock(ResultSet.class);

        when(mockConfig.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(any())).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockResult);
        when(mockResult.next()).thenReturn(true);
        when(mockResult.getInt(1)).thenReturn(1); // ParkingSpot ID
        when(mockResult.getInt(6)).thenReturn(0); // ParkingType ordinal
        when(mockResult.getInt(2)).thenReturn(42); // Ticket ID
        when(mockResult.getDouble(3)).thenReturn(10.0);
        when(mockResult.getTimestamp(4)).thenReturn(new Timestamp(System.currentTimeMillis()));
        when(mockResult.getTimestamp(5)).thenReturn(new Timestamp(System.currentTimeMillis()));
        when(mockResult.getBoolean(6)).thenReturn(false);

        ticketDAO.dataBaseConfig = mockConfig;

        Ticket result = ticketDAO.getTicket("ABC123");

        assertEquals("ABC123", result.getVehicleRegNumber());
        assertEquals(10.0, result.getPrice());
    }

}





