package com.parkit.parkingsystem;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;

/**
 * <b>ParkingSpotTest is built to unit test ParkingSpot.</b>
 * 
 * @see ParkingType
 * @see ParkingSpot
 * 
 * @author laetitiadamen 
 * @version 1.1
 */
@DisplayName("ParkingSpotTest")
public class ParkingSpotTest {

  ParkingSpot parkingSpot = null;

  @Test
  @DisplayName("Test to park a Bike in Bike Spot")
  public void parkingSpotBikeTest() {
    /**
     * GIVEN : Init parking spot
     */
    parkingSpot = new ParkingSpot(1, ParkingType.BIKE, true);

    /**
     * WHEN : Set info
     */
    parkingSpot.setId(4);
    parkingSpot.setParkingType(ParkingType.BIKE);
    parkingSpot.setAvailable(false);

    /**
     * THEN : Verify all info of parking spot
     */
    assertThat(parkingSpot.getId()).isEqualTo(4);
    assertThat(parkingSpot.getParkingType()).isEqualTo(ParkingType.BIKE);
    assertThat(parkingSpot.isAvailable()).isEqualTo(false);
    assertThat(parkingSpot.hashCode()).isEqualTo(4);

  }

  @Test
  @DisplayName("Test to park a Car in Car Spot")
  public void parkingSpotCarTest() {
    /**
     * GIVEN : Init parking spot
     */
    parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);

    /**
     * WHEN : Set info
     */
    parkingSpot.setId(2);
    parkingSpot.setParkingType(ParkingType.CAR);
    parkingSpot.setAvailable(false);

    /**
     * THEN : Verify all info of parking spot
     */
    assertThat(parkingSpot.getId()).isEqualTo(2);
    assertThat(parkingSpot.getParkingType()).isEqualTo(ParkingType.CAR);
    assertThat(parkingSpot.isAvailable()).isEqualTo(false);
    assertThat(parkingSpot.hashCode()).isEqualTo(2);
  }

}