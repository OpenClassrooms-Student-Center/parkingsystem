package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.test.TestAppender;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ParkingSpotDAOTest {

    private static ParkingSpotDAO parkingSpotDAO;

    private  static TestAppender appender;

    private static Logger logger = LogManager.getLogger(ParkingSpotDAO.class);

    @Mock
    public DataBaseConfig dataBaseConfig;

    @BeforeAll
    public static void setUpBeforeAll() {
        appender = new TestAppender();
        ((org.apache.logging.log4j.core.Logger)logger).addAppender(appender);
    }

    @AfterEach
    public void cleanUp() {
        appender.reset();
    }

    @Disabled
    @Test
    public void getNextAvailableSlotForBike() throws Exception {

        //when(dataBaseConfig.getConnection()).thenReturn();

        parkingSpotDAO.getNextAvailableSlot(ParkingType.BIKE);
    }

    @Disabled
    @Test
    public void getNextAvailableSlotForCar(){

        parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);
    }

    @Test
    public void getNextAvailableSlotException(){
        assertThrows(Exception.class, () -> parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR));
    }


}
