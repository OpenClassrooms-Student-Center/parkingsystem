package com.parkit.parkingsystem.model;

import com.parkit.parkingsystem.constants.ParkingType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ParkingSpotTest {

    @Test
    public void ParkingSpotTrueEquals() {
        ParkingSpot parkingSpotCar = new ParkingSpot(1, ParkingType.CAR, true);
        ParkingSpot parkingSpotCar2 = new ParkingSpot(1, ParkingType.CAR, true);

        assertTrue(parkingSpotCar.equals(parkingSpotCar2));
    }

    @Test
    public void ParkingSpotFalseEquals() {
        ParkingSpot parkingSpotCar = new ParkingSpot(1, ParkingType.CAR, true);
        ParkingSpot parkingSpotBike = new ParkingSpot(2, ParkingType.BIKE, true);

        assertFalse(parkingSpotCar.equals(parkingSpotBike));
    }

}
