package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

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
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals(Fare.CAR_RATE_PER_HOUR, ticket.getPrice());
    }

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
        assertEquals("", "");
    }

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

    @Test
    public void calculateFareBikeWithLessThanOneHourParkingTime() {
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (45 * 60 * 1000));//45 minutes parking time should give 3/4th parking fare
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals((0.75 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice());
    }

    @Test
    public void calculateFareCarWithLessThanOneHourParkingTime() {
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (45 * 60 * 1000));//45 minutes parking time should give 3/4th parking fare
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals((0.75 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
    }

    @Test
    public void calculateFareCarWithMoreThanADayParkingTime() {
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (24 * 60 * 60 * 1000));//24 hours parking time should give 24 * parking fare per hour
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals((24 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
    }

    @Test
    public void apply30FreeMinutesReductionFareBikeLessThan30min() {
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (20 * 60 * 1000));
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        fareCalculatorService.apply30FreeMinutesReduction(ticket);
        assertEquals(0, ticket.getPrice());
    }

    @Test
    public void apply30FreeMinutesReductionFareCarLessThan30min() {
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (20 * 60 * 1000));
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        fareCalculatorService.apply30FreeMinutesReduction(ticket);
        assertEquals(0, ticket.getPrice());
    }

    @Test
    public void apply30FreeMinutesReductionFareBikeEquals30min() {
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (30 * 60 * 1000));
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        fareCalculatorService.apply30FreeMinutesReduction(ticket);
        assertEquals(0, ticket.getPrice());
    }

    @Test
    public void apply30FreeMinutesReductionFareCarEquals30min() {
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (30 * 60 * 1000));
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        fareCalculatorService.apply30FreeMinutesReduction(ticket);
        assertEquals(0, ticket.getPrice());
    }


    @Test
    public void apply30FreeMinutesReductionFareCar() {
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        fareCalculatorService.apply30FreeMinutesReduction(ticket);
        assertEquals((Fare.CAR_RATE_PER_HOUR - 0.5 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
    }

    @Test
    public void apply30FreeMinutesReductionFareBike() {
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        fareCalculatorService.apply30FreeMinutesReduction(ticket);
        assertEquals((Fare.BIKE_RATE_PER_HOUR - 0.5 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice());
    }

    @Test
    public void apply30FreeMinutesReductionBikeWithLessThanOneHourParkingTime() {
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (45 * 60 * 1000));//45 minutes parking time should give 3/4th parking fare
        System.out.println(inTime);
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        fareCalculatorService.apply30FreeMinutesReduction(ticket);
        assertEquals(((0.75 - 0.5) * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice()); //45min - 30 free first minutes
    }

    @Test
    public void apply30FreeMinutesReductionCarWithLessThanOneHourParkingTime() {
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (45 * 60 * 1000));//45 minutes parking time should give 3/4th parking fare
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        fareCalculatorService.apply30FreeMinutesReduction(ticket);
        assertEquals(0.38, ticket.getPrice());
    }

    @Test
    public void apply30FreeMinutesReductionCarWithMoreThanADayParkingTime() {
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (24 * 60 * 60 * 1000));//24 hours parking time should give 24 * parking fare per hour
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        fareCalculatorService.apply30FreeMinutesReduction(ticket);
        assertEquals(24 * Fare.CAR_RATE_PER_HOUR - 0.5 * Fare.CAR_RATE_PER_HOUR, ticket.getPrice());
    }


    @Test
    public void applyRecurrentUserReductionFareCar() {
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        fareCalculatorService.applyRecurrentUserReduction(ticket);
        assertEquals(Math.round((Fare.CAR_RATE_PER_HOUR - 0.05 * Fare.CAR_RATE_PER_HOUR) * 100) / 100.0, ticket.getPrice());
    }

    @Test
    public void applyRecurrentUserReductionFareBike() {
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        fareCalculatorService.applyRecurrentUserReduction(ticket);
        assertEquals((Fare.BIKE_RATE_PER_HOUR - 0.05 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice());
    }

    @Test
    public void applyRecurrentUserReductionBikeWithLessThanOneHourParkingTime() {
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (45 * 60 * 1000));//45 minutes parking time should give 3/4th parking fare
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        fareCalculatorService.applyRecurrentUserReduction(ticket);
        double price = 0.75 * Fare.BIKE_RATE_PER_HOUR;
        double reduction = 0.05 * price;
        reduction = Math.round(reduction * 100.0) / 100.0;
        assertEquals(price - reduction, ticket.getPrice());
    }

    @Test
    public void applyRecurrentUserReductionCarWithLessThanOneHourParkingTime() {
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (45 * 60 * 1000));//45 minutes parking time should give 3/4th parking fare
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        fareCalculatorService.applyRecurrentUserReduction(ticket);
        double price = 0.75 * Fare.CAR_RATE_PER_HOUR;
        double reduction = 0.05 * price;
        reduction = Math.round(reduction * 100.0) / 100.0;
        assertEquals(Math.round((price - reduction) * 100.0) / 100.0, ticket.getPrice());
    }

    @Test
    public void applyRecurrentUserReductionCarWithMoreThanADayParkingTime() {
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (24 * 60 * 60 * 1000));
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        fareCalculatorService.applyRecurrentUserReduction(ticket);
        assertEquals((24 * Fare.CAR_RATE_PER_HOUR - (0.05 * 24 * Fare.CAR_RATE_PER_HOUR)), ticket.getPrice());
    }

    @Test
    public void calculateDuration1Hour() {
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        Double duration = fareCalculatorService.calculateDuration(ticket);
        assertEquals(Double.valueOf(1), duration);
    }

    @Test
    public void calculateDurationOnMarchTimeOffset() throws ParseException {
        TimeZone tz = TimeZone.getTimeZone("Europe/Paris");
        TimeZone.setDefault(tz);
        Calendar cal = Calendar.getInstance(tz, Locale.FRANCE);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.FRANCE);
        Date dateBeforeDST = df.parse("2021-03-28 01:55");
        Date dateAfterDST = df.parse("2021-03-28 02:55");
        Calendar inCalendar = Calendar.getInstance();
        Calendar outCalendar = Calendar.getInstance();
        inCalendar.setTime(dateBeforeDST);
        assertEquals(0, inCalendar.get(Calendar.DST_OFFSET));
        outCalendar.setTime(dateAfterDST);
        assertEquals(3600000, outCalendar.get(Calendar.DST_OFFSET));
        assertEquals(2, (outCalendar.get(Calendar.HOUR_OF_DAY) - inCalendar.get(Calendar.HOUR_OF_DAY)));
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        ticket.setInTime(inCalendar.getTime());
        ticket.setOutTime(outCalendar.getTime());
        ticket.setParkingSpot(parkingSpot);
        Double duration = fareCalculatorService.calculateDuration(ticket);
        assertEquals(Double.valueOf(1), duration);
    }

    @Test
    public void calculateDurationOnOctoberTimeOffset() throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.FRANCE);
        Date dateBeforeDST = df.parse("2020-10-25 01:00");
        Date dateAfterDST = df.parse("2020-10-25 03:00");
        Calendar inCalendar = Calendar.getInstance();
        Calendar outCalendar = Calendar.getInstance();
        inCalendar.setTime(dateBeforeDST);
        assertEquals(3600000, inCalendar.get(Calendar.DST_OFFSET));
        outCalendar.setTime(dateAfterDST);
        assertEquals(0, outCalendar.get(Calendar.DST_OFFSET));
        System.out.println(inCalendar);
        System.out.println(outCalendar);
        assertEquals(2, (outCalendar.get(Calendar.HOUR_OF_DAY) - inCalendar.get(Calendar.HOUR_OF_DAY)));
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        ticket.setInTime(inCalendar.getTime());
        ticket.setOutTime(outCalendar.getTime());
        ticket.setParkingSpot(parkingSpot);
        Double duration = fareCalculatorService.calculateDuration(ticket);
        assertEquals(Double.valueOf(2), duration);
    }

    @Test
    public void calculateFareWithBikeReductionCase() {
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
        Date outTime = new Date();
        Calendar outCalendar = Calendar.getInstance();
        outCalendar.setTime(outTime);
        ticket.setRecurrent(true);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        fareCalculatorService.applyReduction(ticket);
        assertEquals(0.48, ticket.getPrice());
    }

    @Test
    public void calculateFareWithCarReductionCase() {
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
        Date outTime = new Date();
        Calendar outCalendar = Calendar.getInstance();
        outCalendar.setTime(outTime);
        ticket.setRecurrent(true);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals(1.5, ticket.getPrice());
        fareCalculatorService.applyReduction(ticket);
        assertEquals(0.71, ticket.getPrice());
    }

}
