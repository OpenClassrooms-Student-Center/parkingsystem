package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.test.TestAppender;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import org.apache.logging.log4j.LogManager;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InteractiveShellTest {

    //private static MemoryAppender memoryAppender;
    private static final String LOGGER_NAME = "com.parkit.parkingsystem.service.InteractiveShell";

    private static InteractiveShell interactiveShell;

    private  static TestAppender appender;

    @Mock
    private static ParkingService parkingService;

    @Mock
    private static InputReaderUtil inputReaderUtil;

    private static Logger logger = LogManager.getLogger(InteractiveShell.class);

    @BeforeAll
    public static void setUpBeforeAll() {
        appender = new TestAppender();
        ((org.apache.logging.log4j.core.Logger)logger).addAppender(appender);
    }

    @BeforeEach
    public void setUpBeforeEach() {
        InteractiveShell.inputReaderUtil = inputReaderUtil;
        InteractiveShell.parkingService = parkingService;
    }

    @AfterEach
    public void cleanUp() {
        appender.reset();
    }

    @Test
    public void loadInterfaceTest1() throws Exception {

        when(inputReaderUtil.readSelection()).thenReturn(1);

        doNothing().when(parkingService).processIncomingVehicle();

        InteractiveShell.loadInterface();
        verify(parkingService, Mockito.times(1)).processIncomingVehicle();
    }

    @Test
    public void loadInterfaceTest2() throws Exception {
        when(inputReaderUtil.readSelection()).thenReturn(2);

        doNothing().when(parkingService).processExitingVehicle();

        InteractiveShell.loadInterface();
        verify(parkingService, Mockito.times(1)).processExitingVehicle();
    }

    @Test
    public void loadInterfaceTest3() throws Exception {
        when(inputReaderUtil.readSelection()).thenReturn(3);

        InteractiveShell.loadInterface();
        assertEquals(7, appender.getLogCount());
    }

    @Test
    public void loadInterfaceTestDefault() throws Exception {
        when(inputReaderUtil.readSelection()).thenReturn(5).thenReturn(1);

        doNothing().when(parkingService).processIncomingVehicle();

        InteractiveShell.loadInterface();
        assertEquals(11, appender.getLogCount());
    }

}
