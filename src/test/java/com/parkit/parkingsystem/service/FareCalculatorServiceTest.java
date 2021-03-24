package com.parkit.parkingsystem.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Date;

import org.apache.commons.lang.time.DateUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorServiceTest {

	private static FareCalculatorService fareCalculatorService;
	private Ticket ticket;

	@BeforeAll
	private static void setUp() {
		fareCalculatorService = new FareCalculatorService();
	}

	@BeforeEach
	private void setUpPerTest() {
		ticket = new Ticket();
	}

	@Test
	@DisplayName("Calcul du tarif pour une durée de stationnement de 1h pour une voiture")
	public void calculateFareCar() {
		// GIVEN
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// WHEN
		fareCalculatorService.calculateFare(ticket, false);

		// THEN
		assertEquals(ticket.getPrice(), Fare.CAR_RATE_PER_HOUR);
	}

	@Test
	@DisplayName("Calcul du tarif pour une durée de stationnement de 1h pour un vélo")
	public void calculateFareBike() {
		// GIVEN
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// WHEN
		fareCalculatorService.calculateFare(ticket, false);

		// THEN
		assertEquals(ticket.getPrice(), Fare.BIKE_RATE_PER_HOUR);
	}

	@Test
	@DisplayName("Calcul du tarif pour un véhicule inconnu")
	public void calculateFareUnkownType() {
		// GIVEN
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, null, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// THEN
		assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket, false));
	}

	@Test
	@DisplayName("Erreur temps de sortie inférieur ou égal au temps d'entrée")
	public void calculateFareBikeWithFutureInTime() {
		// GIVEN
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() + (60 * 60 * 1000));
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// THEN
		assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket, false));
	}

	@DisplayName("Erreur temps quand le temps de sortie est null")
	@Test
	public void calculateFareCarWithTimeIsNull() {
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(null);
		ticket.setParkingSpot(parkingSpot);
		assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket, false));
	}

	@DisplayName("Throw exception quand le type de parking est null")
	@Test
	public void calculateFareCarWithNullParkingType() {
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, null, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket, false));
	}

	@DisplayName("Throw exception quand le type de parking est inconnu")
	@Test
	public void calculateFareCarWithUnknownParkingType() {
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.UNKNOWN, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket, false));

	}

	@Test
	@DisplayName("Calcul du tarif pour une durée de stationnement de 45Min pour un vélo")
	public void calculateFareBikeWithLessThanOneHourParkingTime() {
		// GIVEN
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (45 * 60 * 1000));// 45 minutes parking time should give 3/4th
																		// parking fare
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// WHEN
		fareCalculatorService.calculateFare(ticket, false);

		// THEN
		assertEquals((0.75 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice());
	}

	@Test
	@DisplayName("Calcul du tarif pour une durée de stationnement de 45Min pour une voiture")
	public void calculateFareCarWithLessThanOneHourParkingTime() {
		// GIVEN
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (45 * 60 * 1000));// 45 minutes parking time should give 3/4th
																		// parking fare
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// WHEN
		fareCalculatorService.calculateFare(ticket, false);

		// THEN
		assertEquals(ticket.getPrice(), 1.125);

	}

	@Test
	@DisplayName("Calcul du tarif pour une durée de stationnement de 24h pour une voiture")
	public void calculateFareCarWithMoreThanADayParkingTime() {
		// GIVEN
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (24 * 60 * 60 * 1000));// 24 hours parking time should give 24 *
																			// parking fare per hour
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// WHEN
		fareCalculatorService.calculateFare(ticket, false);

		// THEN
		assertEquals((24 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
	}

	@Test
	@DisplayName("Stationnement 30min gratuit une voiture")
	public void calculateFareCarLess30Min() {
		// GIVEN
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (30 * 60 * 1000));

		Date outTime = DateUtils.addMinutes(inTime, 30);
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// WHEN
		fareCalculatorService.calculateFare(ticket, false);

		// THEN
		assertEquals(ticket.getPrice(), 0);
	}

	@Test
	@DisplayName("Stationnement 30min gratuit un vélo")
	public void calculateFareBikeLess30Min() {
		// GIVEN
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (30 * 60 * 1000));
		Date outTime = DateUtils.addMinutes(inTime, 30);
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// WHEN
		fareCalculatorService.calculateFare(ticket, false);

		// THEN
		assertEquals(ticket.getPrice(), 0);
	}

	@Test
	@DisplayName("Stationnement recurrent user pour une voiture")
	public void calculateFareCarForRecurrentUsers() {
		// TODO: check that the fare generated has a discount of 5% --> DONE

		Date inTime = new Date();
		double discount = 1.43;
		inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setDiscount(discount);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket, true); // Boolean isRecurrentUser equals to true
															// gives a discount of 5% (1-0.05=0.95)
		assertEquals(1 * Fare.CAR_RATE_PER_HOUR * 0.95, ticket.getPrice());
	}
}
