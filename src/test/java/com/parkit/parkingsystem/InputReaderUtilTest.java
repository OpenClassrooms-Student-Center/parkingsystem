package com.parkit.parkingsystem;

import com.parkit.parkingsystem.util.InputReaderUtil;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Scanner;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class InputReaderUtilTest {

    private static InputReaderUtil inputReaderUtil;

    private static final InputStream stdin = System.in;

    private InputStream inputStream;

    public void provideInput(String inputString) {
        inputStream = new ByteArrayInputStream(inputString.getBytes());
        System.setIn(inputStream);
    }

    @AfterEach
    public void restoreSystemInput() {
        System.setIn(stdin);
    }

    @Test
    public void readSelectionTest1() {
        final String inputString = "1";
        provideInput(inputString);

        inputReaderUtil = new InputReaderUtil();

        assertEquals(1, inputReaderUtil.readSelection());
    }

    @Test
    public void readSelectionTestException() {
        final String inputString = "test";
        provideInput(inputString);

        inputReaderUtil = new InputReaderUtil();

        assertEquals(-1, inputReaderUtil.readSelection());
    }

    @Test
    public void readVehicleRegistrationNumberTestNull() {
        final String vehicleRegNumber = " ";
        provideInput(vehicleRegNumber);

        inputReaderUtil = new InputReaderUtil();

        assertThrows(IllegalArgumentException.class, ()-> inputReaderUtil.readVehicleRegistrationNumber());
    }

    @Test
    public void readVehicleRegistrationNumberTestValid() {
        final String vehicleRegNumber = "123 ABC 456";
        provideInput(vehicleRegNumber);

        inputReaderUtil = new InputReaderUtil();

        assertEquals(vehicleRegNumber, inputReaderUtil.readVehicleRegistrationNumber());
    }
}
