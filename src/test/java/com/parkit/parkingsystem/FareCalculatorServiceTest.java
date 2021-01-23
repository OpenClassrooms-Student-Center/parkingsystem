package com.parkit.parkingsystem;

import static org.junit.jupiter.api.Assertions.*;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import com.parkit.parkingsystem.service.InteractiveShell;
import java.util.Date;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * <b>FareCalculatorServiceTest is built to unit test FareCalculatorService.</b>
 * 
 * @see Fare
 * @see ParkingType
 * @see ParkingSpot
 * @see Ticket
 * @see FareCalculatorService
 * 
 * @author laetitiadamen
 * @version 1.1
 */
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
  public void calculateFareCar() {
    /**
     * GIVEN :
     */
    /**
     * WHEN : Set parking spot
     */
    Date inTime = new Date();
    inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
    Date outTime = new Date();
    ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

    ticket.setInTime(inTime);
    ticket.setOutTime(outTime);
    ticket.setParkingSpot(parkingSpot);
    fareCalculatorService.calculateFare(ticket, false);
    /**
     * THEN : Verify price is OK
     */
    assertEquals(ticket.getPrice(), Fare.CAR_RATE_PER_HOUR);
  }


  @Test
  public void calculateFareBike() {
    /**
     * GIVEN
     */
    /**
     * WHEN : Set parking spot
     */
    Date inTime = new Date();
    inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
    Date outTime = new Date();
    ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

    ticket.setInTime(inTime);
    ticket.setOutTime(outTime);
    ticket.setParkingSpot(parkingSpot);
    fareCalculatorService.calculateFare(ticket, false);
    /**
     * THEN : Verify price is OK
     */
    assertEquals(ticket.getPrice(), Fare.BIKE_RATE_PER_HOUR);
  }


  @Test
  public void calculateFareUnkownType() {
    /**
     * GIVEN :
     */
    /**
     * WHEN : Set parking spot
     */
    Date inTime = new Date();
    inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
    Date outTime = new Date();
    ParkingSpot parkingSpot = new ParkingSpot(1, null, false);

    ticket.setInTime(inTime);
    ticket.setOutTime(outTime);
    ticket.setParkingSpot(parkingSpot);
    /**
     * THEN : Verify throws
     */
    assertThrows(NullPointerException.class,
        () -> fareCalculatorService.calculateFare(ticket, false));
  }


  @Test
  public void calculateFareBikeWithFutureInTime() {
    /**
     * GIVEN :
     */
    /**
     * WHEN : Set parking spot
     */
    Date inTime = new Date();
    inTime.setTime(System.currentTimeMillis() + (60 * 60 * 1000));
    Date outTime = new Date();
    ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

    ticket.setInTime(inTime);
    ticket.setOutTime(outTime);
    ticket.setParkingSpot(parkingSpot);
    /**
     * THEN : Verify throws
     */
    assertThrows(IllegalArgumentException.class,
        () -> fareCalculatorService.calculateFare(ticket, false));
  }


  // new test car scenario
  @Test
  public void calculateFareCarWithFutureInTime() {
    /**
     * GIVEN :
     */
    /**
     * WHEN : Set parking spot
     */
    Date inTime = new Date();
    inTime.setTime(System.currentTimeMillis() + (60 * 60 * 1000));
    Date outTime = new Date();
    ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

    ticket.setInTime(inTime);
    ticket.setOutTime(outTime);
    ticket.setParkingSpot(parkingSpot);
    /**
     * THEN : Verify throws
     */
    assertThrows(IllegalArgumentException.class,
        () -> fareCalculatorService.calculateFare(ticket, false));
  }


  @Test
  public void calculateFareBikeWithLessThanOneHourParkingTime() {
    /**
     * GIVEN :
     */
    /**
     * WHEN : Set parking spot
     */
    Date inTime = new Date();
    // 45 minutes parking time should give 3/4th parking fare =>
    inTime.setTime(System.currentTimeMillis() - (45 * 60 * 1000));
    Date outTime = new Date();
    ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

    ticket.setInTime(inTime);
    ticket.setOutTime(outTime);
    ticket.setParkingSpot(parkingSpot);
    fareCalculatorService.calculateFare(ticket, false);
    /**
     * THEN : Verify price is OK
     */
    assertEquals((0.75 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice());
  }


  @Test
  public void calculateFareCarWithLessThanOneHourParkingTime() {
    /**
     * GIVEN :
     */
    /**
     * WHEN : Set parking spot
     */
    Date inTime = new Date();
    // 45 minutes parking time should give 3/4th parking fare =>
    inTime.setTime(System.currentTimeMillis() - (45 * 60 * 1000));
    Date outTime = new Date();
    ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

    ticket.setInTime(inTime);
    ticket.setOutTime(outTime);
    ticket.setParkingSpot(parkingSpot);
    fareCalculatorService.calculateFare(ticket, false);
    /**
     * THEN : Verify price is OK
     */
    assertEquals((0.75 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
  }


  @Test
  public void calculateFareCarWithMoreThanADayParkingTime() {
    /**
     * GIVEN :
     */
    /**
     * WHEN : Set parking spot
     */
    Date inTime = new Date();
    // 24 hours parking time should give 24 * parking fare per hour =>
    inTime.setTime(System.currentTimeMillis() - (24 * 60 * 60 * 1000));
    Date outTime = new Date();
    ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

    ticket.setInTime(inTime);
    ticket.setOutTime(outTime);
    ticket.setParkingSpot(parkingSpot);
    fareCalculatorService.calculateFare(ticket, false);
    /**
     * THEN : Verify price is OK
     */
    assertEquals((24 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
  }


  // New test Bike scenario
  @Test
  public void calculateFareBikeWithMoreThanADayParkingTime() {
    /**
     * GIVEN :
     */
    /**
     * WHEN : Set parking spot
     */
    Date inTime = new Date();
    // 24 hours parking time should give 24 * parking fare per hour =>
    inTime.setTime(System.currentTimeMillis() - (24 * 60 * 60 * 1000));
    Date outTime = new Date();
    ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

    ticket.setInTime(inTime);
    ticket.setOutTime(outTime);
    ticket.setParkingSpot(parkingSpot);
    fareCalculatorService.calculateFare(ticket, false);
    /**
     * THEN : Verify price is OK
     */
    assertEquals((24 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice());
  }


  // New test
  @Test
  public void calculateFareBikeWithThirtyMinParkingTime() {
    /**
     * GIVEN :
     */
    /**
     * WHEN : Set parking spot
     */
    Date inTime = new Date();
    // 30 minutes =>
    inTime.setTime(System.currentTimeMillis() - (30 * 60 * 1000));
    Date outTime = new Date();
    ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

    ticket.setInTime(inTime);
    ticket.setOutTime(outTime);
    ticket.setParkingSpot(parkingSpot);
    fareCalculatorService.calculateFare(ticket, false);
    /**
     * THEN : Verify price is OK
     */
    assertEquals((0.5 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice());

  }


  // New test
  @Test
  public void calculateFareCarWithThirtyMinParkingTime() {
    /**
     * GIVEN :
     */
    /**
     * WHEN : Set parking spot
     */
    Date inTime = new Date();
    // 30 minutes parking time =>
    inTime.setTime(System.currentTimeMillis() - (30 * 60 * 1000));
    Date outTime = new Date();
    ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

    ticket.setInTime(inTime);
    ticket.setOutTime(outTime);
    ticket.setParkingSpot(parkingSpot);
    fareCalculatorService.calculateFare(ticket, false);
    /**
     * THEN : Verify price is OK
     */
    assertEquals((0.5 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());

  }


  // New test
  @Test
  public void calculateFareBikeWithLessThanThirtyMinParkingTime() {
    /**
     * GIVEN :
     */
    /**
     * WHEN : Set parking spot
     */
    Date inTime = new Date();
    // 20 minutes parking time =>
    inTime.setTime(System.currentTimeMillis() - (20 * 60 * 1000));
    Date outTime = new Date();
    ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

    ticket.setInTime(inTime);
    ticket.setOutTime(outTime);
    ticket.setParkingSpot(parkingSpot);
    fareCalculatorService.calculateFare(ticket, false);
    /**
     * THEN : Verify price is OK
     */
    assertEquals(0, ticket.getPrice());

  }


  // New test
  @Test
  public void calculateFareCarWithLessThanThirtyMinParkingTime() {
    /**
     * GIVEN :
     */
    /**
     * WHEN : Set parking spot
     */
    Date inTime = new Date();
    // 20 minutes parking time =>
    inTime.setTime(System.currentTimeMillis() - (20 * 60 * 1000));
    Date outTime = new Date();
    ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

    ticket.setInTime(inTime);
    ticket.setOutTime(outTime);
    ticket.setParkingSpot(parkingSpot);
    fareCalculatorService.calculateFare(ticket, false);
    /**
     * THEN : Verify price is OK
     */
    assertEquals(0, ticket.getPrice());

  }


  // New test 5% off for recurrent user - Car scenario
  @Test
  public void calculateFivePourcentOffForCarReccurentUser() {
    /**
     * GIVEN :
     */
    /**
     * WHEN : Set parking spot
     */
    Date inTime = new Date();
    // 1 hour parking
    inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
    Date outTime = new Date();
    ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
    boolean recurrent = true;

    ticket.setInTime(inTime);
    ticket.setOutTime(outTime);
    ticket.setParkingSpot(parkingSpot);
    fareCalculatorService.calculateFare(ticket, recurrent);
    /**
     * THEN : Verify price is OK
     */
    assertEquals((0.95 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());

  }


  // New test 5% off for recurrent user - Bike scenario
  @Test
  public void calculateFivePourcentOffForBikeReccurentUser() {
    /**
     * GIVEN :
     */
    /**
     * WHEN : Set parking spot
     */
    Date inTime = new Date();
    // 1 hour parking
    inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
    Date outTime = new Date();
    ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
    boolean recurrent = true;

    ticket.setInTime(inTime);
    ticket.setOutTime(outTime);
    ticket.setParkingSpot(parkingSpot);
    fareCalculatorService.calculateFare(ticket, recurrent);
    /**
     * THEN : Verify price is OK
     */
    assertEquals((0.95 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice());

  }
}
