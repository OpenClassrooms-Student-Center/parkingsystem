package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.model.ParkingSpot;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.internal.verification.Times;

import java.sql.Connection;
import java.sql.PreparedStatement;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ParkingSpotDAOTest {
    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static final Logger logger = LogManager.getLogger("ParkingSpotDAOTest");
    Connection con = null;

    @Mock
    private static ParkingType parkingType;

    @BeforeEach
    public void setUp() {
        try {
            con = dataBaseTestConfig.getConnection();
        }catch(Exception ex){
            logger.error("Error connecting to data base",ex);

        }
    }

    @AfterEach
    public void tearDown(){
         dataBaseTestConfig.closeConnection(con);
    }

    @Test
    public void getNextAvailableSlotTest() {

        ParkingSpotDAO parkingSpotDAO = new ParkingSpotDAO();
        ParkingSpot parkingSpot = mock(ParkingSpot.class);
       int parkkedId= parkingSpotDAO.getNextAvailableSlot(parkingType.CAR);
       assertEquals(1,parkkedId);



    }

    @Test
   public  void updateParkingTest() {
        ParkingSpotDAO parkingSpotDAO = new ParkingSpotDAO();
        ParkingSpot parkingSpot = mock(ParkingSpot.class);
        parkingSpot.setAvailable(true);
        parkingSpot.setId(1);
        parkingSpotDAO.updateParking(parkingSpot);
        verify(parkingSpot,times(1)).isAvailable();
    }
    @Test
    public void updateParkingTestFailour(){
        ParkingSpotDAO parkingSpotDAO = new ParkingSpotDAO();
        ParkingSpot parkingSpot = mock(ParkingSpot.class);
        assertFalse(parkingSpotDAO.updateParking(parkingSpot));

    }
}