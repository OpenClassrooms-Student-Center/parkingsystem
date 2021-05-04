package com.parkit.parkingsystem.config;

import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.TicketDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

public class DataBaseConfigTest {

    DataBaseConfig dataBaseConfig = new DataBaseConfig();

    private static final Logger logger = LogManager.getLogger(DataBaseConfigTest.class);

    @Test
    public void getConnection() throws SQLException, ClassNotFoundException {
    Connection connection = dataBaseConfig.getConnection();
    assertNotNull(connection);
    dataBaseConfig.closeConnection(connection);
    }


    @Test
    public void closeConnection() throws SQLException, ClassNotFoundException {
        Connection connection = dataBaseConfig.getConnection();
        dataBaseConfig.closeConnection(connection);
        assertTrue(connection.isClosed());
    }

    @Test
    public void closePreparedStatement() {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = dataBaseConfig.getConnection();
            ps = connection.prepareStatement(DBConstants.GET_NEXT_PARKING_SPOT);
            assertNotNull(ps);
            dataBaseConfig.closePreparedStatement(ps);
            assertTrue(ps.isClosed());
        } catch (SQLException | ClassNotFoundException ex) {
            logger.error("Error in DataBaseConfigTest.",ex);
        } finally {
            dataBaseConfig.closeConnection(connection);
        }


    }

    @Test
    public void closeResultSet()  {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = dataBaseConfig.getConnection();
            ps = connection.prepareStatement(DBConstants.GET_NEXT_PARKING_SPOT);
            ps.setString(1, ParkingType.valueOf("CAR").toString());
            ResultSet rs = ps.executeQuery();
            assertNotNull(ps);
            dataBaseConfig.closeResultSet(rs);
            assertTrue(rs.isClosed());
        } catch (SQLException | ClassNotFoundException ex) {
            logger.error("Error in DataBaseConfigTest.",ex);
        } finally {
            dataBaseConfig.closePreparedStatement(ps);
            dataBaseConfig.closeConnection(connection);
        }
    }

}
