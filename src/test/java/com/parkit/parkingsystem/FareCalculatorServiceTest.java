package com.parkit.parkingsystem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Date;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;

/**
 *
 * @author Nicolas BIANCUCCI
 * this class contains FareCalculatorService unit tests
 */

@ExtendWith(MockitoExtension.class)
public class FareCalculatorServiceTest {

	/**
	 * Class under test
	 */
	private static FareCalculatorService fareCalculatorService;
	/**
	 * instantiating Ticket
	 */
	private Ticket ticket;

	/**
	 * mocking TicketDAO
	 */
	@Mock
	private static TicketDAO ticketDAO;

	/**
	 * instantiating FareCalculatorService before all tests
	 */
	@BeforeAll
	private static void setUp() {
		fareCalculatorService = new FareCalculatorService();
	}

	/**
	 * instantiating a Ticket before each test
	 */
	@BeforeEach
	private void setUpPerTest() {
		ticket = new Ticket();
	}

	@Tag("CalculatingFareForCar")
	@DisplayName("Calculate Car fare for one hour parking duration")
	@Test
	public void calculateFareCar() {
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		fareCalculatorService.calculateFare(ticket);

		assertEquals(ticket.getPrice(), Fare.CAR_RATE_PER_HOUR);
	}

	@Tag("CalculatingFareForCar")
	@DisplayName("Calculate Car fare for less than one hour parking duration")
	@Test
	public void calculateFareCarWithLessThanOneHourParkingTime() {
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (45 * 60 * 1000));
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		fareCalculatorService.calculateFare(ticket);

		assertEquals((0.75 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
	}

	@Tag("CalculatingFareForCar")
	@DisplayName("Calculate Car fare for twenty four hour parking duration")
	@Test
	public void calculateFareCarWithMoreThanADayParkingTime() {
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (24 * 60 * 60 * 1000));
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		fareCalculatorService.calculateFare(ticket);

		assertEquals((24 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
	}

	@Tag("CalculatingFareForCar")
	@DisplayName("Free parking service for less then thirty minutes parking duration")
	@Test
	public void calculateFareCarWithLessThanThrityMinutesParkingTime() {
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (25 * 60 * 1000));
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		fareCalculatorService.calculateFare(ticket);

		assertEquals((0 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
	}

	@Tag("CalculatingFareForCar")
	@DisplayName("Discount Car fare for recurring users with reduces for one hour parking duration")
	@Test
	public void calculateFareCarWithDiscountForCustomer() {
		fareCalculatorService = new FareCalculatorService(ticketDAO);
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		Mockito.when(ticketDAO.isReccurent(ticket)).thenReturn(true);

		fareCalculatorService.calculateFare(ticket);

		assertEquals((0.95 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
	}

	@Tag("CalculatingFareForCar")
	@DisplayName("Throw exception when inTime comes after OutTime")
	@Test
	public void calculateFareCarWithFutureInTime() {
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() + (60 * 60 * 1000));
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket));
	}

	@Tag("CalculatingFareForCar")
	@DisplayName("Throw exception when Car outTime is null")
	@Test
	public void givenACarWithNullOutTime() {
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(null);
		ticket.setParkingSpot(parkingSpot);

		assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket));
	}

	@Tag("CalculatingFareForBike")
	@DisplayName("Calculate Bike fare for one hour parking duration")
	@Test
	public void calculateFareBike() {
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		fareCalculatorService.calculateFare(ticket);

		assertEquals(ticket.getPrice(), Fare.BIKE_RATE_PER_HOUR);
	}

	@Tag("CalculatingFareForBike")
	@DisplayName("Calculate Bike fare for less than one hour parking duration")
	@Test
	public void calculateFareBikeWithLessThanOneHourParkingTime() {
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (45 * 60 * 1000));
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		fareCalculatorService.calculateFare(ticket);

		assertEquals((0.75 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice());
	}

	@Tag("CalculatingFareForBike")
	@DisplayName("Calculate Bike fare for twenty four hour parking duration")
	@Test
	public void calculateFareBikeWithMoreThanADayParkingTime() {
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (24 * 60 * 60 * 1000));
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		fareCalculatorService.calculateFare(ticket);

		assertEquals((24 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice());
	}

	@Tag("CalculatingFareForBike")
	@DisplayName("Free parking service for less then thirty minutes parking duration")
	@Test
	public void calculateFareBikeWithLessThanThrityMinutesParkingTime() {
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (25 * 60 * 1000));
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		fareCalculatorService.calculateFare(ticket);

		assertEquals((0 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice());
	}

	@Tag("CalculatingFareForBike")
	@DisplayName("Discount Bike fare for recurring users with reduces for one hour parking duration")
	@Test
	public void calculateFareBikeWithDiscountForCustomer() {
		fareCalculatorService = new FareCalculatorService(ticketDAO);
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		Mockito.when(ticketDAO.isReccurent(ticket)).thenReturn(true);

		fareCalculatorService.calculateFare(ticket);

		assertEquals((0.95 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice());
	}

	@Tag("CalculatingFareForBike")
	@DisplayName("Throw exception when inTime comes after OutTime")
	@Test
	public void calculateFareBikeWithFutureInTime() {
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() + (60 * 60 * 1000));
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket));
	}

	@Tag("CalculatingFareForBike")
	@DisplayName("Throw exception when Bike outTime is null")
	@Test
	public void givenABikeWithNullOutTime() {
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(null);
		ticket.setParkingSpot(parkingSpot);

		assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket));
	}

	@Tag("CalculatingFareForUnknownParkingType")
	@DisplayName("Throw exception when parking type is null")
	@Test
	public void calculateFareUnkownType() {
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, null, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket));
	}

}
