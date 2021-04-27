package com.parkit.parkingsystem.model;

import com.parkit.parkingsystem.constants.ParkingType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ParkingSpotTest {

    @Test
    public void equalsFalse() {
        ParkingType parkingType = ParkingType.BIKE;
        ParkingSpot parkingSpot1 = new ParkingSpot(1,parkingType, true);
        ParkingSpot parkingSpot2 = new ParkingSpot(2,parkingType, true);
        assertEquals(false, parkingSpot1.equals(parkingSpot2));
    }

    @Test
    public void equalsFalseBecauseNull() {
        ParkingType parkingType = ParkingType.BIKE;
        ParkingSpot parkingSpot1 = new ParkingSpot(1,parkingType, true);
        assertEquals(false, parkingSpot1.equals(null));
    }

    @Test
    public void equalsFalseBecauseAnotherClass() {
        ParkingType parkingType1 = ParkingType.BIKE;
        ParkingType parkingType2 = ParkingType.CAR;
        ParkingSpot parkingSpot1 = new ParkingSpot(1,parkingType1, true);
        ParkingSpot parkingSpot2 = new ParkingSpot(2,parkingType2, true);
        assertEquals(false, parkingSpot1.equals(parkingSpot2));
    }

    @Test
    public void equalsTrue() {
        ParkingType parkingType = ParkingType.BIKE;
        ParkingSpot parkingSpot1 = new ParkingSpot(1,parkingType, true);
        ParkingSpot parkingSpot2 = new ParkingSpot(1,parkingType, true);
        assertEquals(true, parkingSpot1.equals(parkingSpot2));
    }

}
