package com.parkit.parkingsystem.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.parkit.parkingsystem.constants.ParkingType;

@DisplayName("ParkingSpotTest")
public class ParkingSpotTest {

	ParkingSpot parkingSpot = null;

	@DisplayName("Test car parking spot")
	@Test
	public void testCarParkingSpot() {
		/**
		 * GIVEN Initialized Car ParkingSpot
		 */
		parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);

		/**
		 * WHEN a parking spot is set
		 */
		parkingSpot.setId(1);
		parkingSpot.setParkingType(ParkingType.CAR);
		parkingSpot.setAvailable(false);

		/**
		 * THEN verify ticket has the right information in all the getMethod()
		 */
		assertEquals(parkingSpot.getId(), 1);
		assertEquals(parkingSpot.getParkingType(), ParkingType.CAR);
		assertEquals(parkingSpot.isAvailable(), false);
	}

	@DisplayName("Test vélo parking spot")
	@Test
	public void testBikeParkingSpot() throws Exception {
		/**
		 * GIVEN Initialized Bike ParkingSpot
		 */
		parkingSpot = new ParkingSpot(1, ParkingType.BIKE, true);

		/**
		 * WHEN a parking spot is set
		 */
		parkingSpot.setId(4);
		parkingSpot.setParkingType(ParkingType.BIKE);
		parkingSpot.setAvailable(false);

		/**
		 * THEN verify ticket has the right information in all the getMethod()
		 */
		assertEquals(parkingSpot.getId(), 4);
		assertEquals(parkingSpot.getParkingType(), ParkingType.BIKE);
		assertEquals(parkingSpot.isAvailable(), false);
	}

	@DisplayName("Test hash method")
	@Test
	public void testHashMethod() {
		parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
		assertEquals(parkingSpot.hashCode(), 1);
	}
}
