package com.parkit.parkingsystem;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.parkit.parkingsystem.constants.Fare;

public class FareTest {

	@Test
	public void bikeRatePerHour_shouldBe_1_0() {
		assertEquals(Fare.BIKE_RATE_PER_HOUR, 1.0);
	}

	@Test
	public void carRatePerHour_shouldBe_1_5() {
		assertEquals(Fare.CAR_RATE_PER_HOUR, 1.5);
	}

	@Test
	public void discount_shouldBe_0_95() {
		assertEquals(Fare.DISCOUNT, 0.95);
	}

}
