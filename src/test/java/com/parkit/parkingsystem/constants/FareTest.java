package com.parkit.parkingsystem.constants;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class FareTest {

	@DisplayName("Test pour le prix une heure velo")
	@Test
	public void bikeRatePerHour_shouldBe_1_0() {
		assertEquals(Fare.BIKE_RATE_PER_HOUR, 1.0);
	}

	@DisplayName("Test pour le prix une heure voiture")
	@Test
	public void carRatePerHour_shouldBe_1_5() {
		assertEquals(Fare.CAR_RATE_PER_HOUR, 1.5);
	}

	@DisplayName("Test pour le prix avec le discount")
	@Test
	public void discount_shouldBe_0_95() {
		assertEquals(Fare.DISCOUNT, 0.95);
	}

}
