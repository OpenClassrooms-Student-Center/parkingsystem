package com.parkit.parkingsystem.config;

import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import org.junit.jupiter.api.Test;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

public class DataBaseConfigTest {

    DataBaseConfig dataBaseConfig = new DataBaseConfig();



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
    public void closePreparedStatement() throws SQLException, ClassNotFoundException {
        Connection connection = dataBaseConfig.getConnection();
        PreparedStatement ps = connection.prepareStatement(DBConstants.GET_NEXT_PARKING_SPOT);
        assertNotNull(ps);
        dataBaseConfig.closePreparedStatement(ps);
        assertTrue(ps.isClosed());
        dataBaseConfig.closeConnection(connection);
    }

    @Test
    public void closeResultSet() throws SQLException, ClassNotFoundException {
        Connection connection = dataBaseConfig.getConnection();
        PreparedStatement ps = connection.prepareStatement(DBConstants.GET_NEXT_PARKING_SPOT);
        ps.setString(1, ParkingType.valueOf("CAR").toString());
        ResultSet rs = ps.executeQuery();
        assertNotNull(ps);
        dataBaseConfig.closeResultSet(rs);
        assertTrue(rs.isClosed());
        dataBaseConfig.closePreparedStatement(ps);
        dataBaseConfig.closeConnection(connection);
    }

}
