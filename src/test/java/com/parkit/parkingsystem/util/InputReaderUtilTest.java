package com.parkit.parkingsystem.util;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Scanner;


import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class InputReaderUtilTest {

    private static InputReaderUtil inputReaderUtil;

    private static final InputStream stdin = System.in;


    public void provideInput(String inputString) {
        InputStream inputStream = new ByteArrayInputStream(inputString.getBytes());
        Scanner scan = new Scanner(inputStream);
        inputReaderUtil = new InputReaderUtil(scan);
    }

    @AfterEach
    public void restoreSystemInput() {
        System.setIn(stdin);
    }

    @Test
    public void readSelectionTest1() {
        final String inputString = "1";
        provideInput(inputString);

        assertEquals(1, inputReaderUtil.readSelection());
    }

    @Test
    public void readSelectionTestException() {
        final String inputString = "test";
        provideInput(inputString);

        assertEquals(-1, inputReaderUtil.readSelection());
    }

    @Test
    public void readVehicleRegistrationNumberTestInvalid() {
        final String vehicleRegNumber = " ";
        provideInput(vehicleRegNumber);

        assertThrows(IllegalArgumentException.class, ()-> inputReaderUtil.readVehicleRegistrationNumber());
    }

    @Test
    public void readVehicleRegistrationNumberTestValid() {
        final String vehicleRegNumber = "123 ABC 456";
        provideInput(vehicleRegNumber);

        assertEquals(vehicleRegNumber, inputReaderUtil.readVehicleRegistrationNumber());
    }
}
