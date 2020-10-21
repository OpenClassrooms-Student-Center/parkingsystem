package com.parkit.parkingsystem.units.service;

import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.service.InteractiveShell;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.InputStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InteractiveShellTest {

    private static InteractiveShell interactiveShell;

    @Mock
    private static ParkingService parkingService;

    @Mock
    private static InputReaderUtil inputReaderUtil;

    private Scanner scan;

    @BeforeEach
    public void setUpBeforeEach() {
//        interactiveShell = new InteractiveShell();

    }


    @Test
    public void loadInterfaceTest1() throws Exception {
        when(inputReaderUtil.readSelection()).thenReturn(1);


        InteractiveShell.loadInterface(scan);
        verify(parkingService, Mockito.times(1)).processIncomingVehicle();
        //assertThrows(Exception.class, InteractiveShell::loadInterface);
    }

    @Disabled
    @Test
    public void loadInterfaceTest2() throws Exception {
//TODO
        when(inputReaderUtil.readSelection()).thenReturn(2);

        InteractiveShell.loadInterface(scan);
        verify(parkingService, Mockito.times(1)).processExitingVehicle();
    }

    @Disabled
    @Test
    public void loadInterfaceTest3() {
//TODO
        when(inputReaderUtil.readSelection()).thenReturn(3);

    }

    @Disabled
    @Test
    public void loadInterfaceTestDefault() {
//TODO
        when(inputReaderUtil.readSelection()).thenReturn(5);

    }

}
