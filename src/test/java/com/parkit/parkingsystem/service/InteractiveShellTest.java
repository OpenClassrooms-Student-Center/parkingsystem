package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.util.InputReaderUtil;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InteractiveShellTest {

    private static InteractiveShell interactiveShell;

    private Logger logger;

    @Mock
    private static ParkingService parkingService;

    @Mock
    private static InputReaderUtil inputReaderUtil;

    @BeforeEach
    public void setUpBeforeEach() {
        InteractiveShell.inputReaderUtil = inputReaderUtil;
        InteractiveShell.parkingService = parkingService;
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

    }

    @Disabled
    @Test
    public void loadInterfaceTestDefault() {
//TODO
        when(inputReaderUtil.readSelection()).thenReturn(5);

    }

}
