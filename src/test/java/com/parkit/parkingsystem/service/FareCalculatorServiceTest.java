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

import java.sql.Timestamp;
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
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        ticket.setInTime(new Timestamp(System.currentTimeMillis() - (60*60*1000)));
        ticket.setOutTime(new Timestamp(System.currentTimeMillis()));
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals(Fare.CAR_RATE_PER_HOUR, ticket.getPrice());
    }

    @Test
    public void calculateFareBike() {
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
        ticket.setInTime(new Timestamp(System.currentTimeMillis() - (60*60*1000)));
        ticket.setOutTime(new Timestamp(System.currentTimeMillis()));
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals(ticket.getPrice(), Fare.BIKE_RATE_PER_HOUR);
        assertEquals("", "");
    }

    @Test
    public void calculateFareUnkownType() {
        ParkingSpot parkingSpot = new ParkingSpot(1, null, false);
        ticket.setInTime(new Timestamp(System.currentTimeMillis() - (60*60*1000)));
        ticket.setOutTime(new Timestamp(System.currentTimeMillis()));
        ticket.setParkingSpot(parkingSpot);
        assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket));
    }

    @Test
    public void calculateFareBikeWithFutureInTime() {
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
        ticket.setInTime(new Timestamp(System.currentTimeMillis() + (60*60*1000)));
        ticket.setOutTime(new Timestamp(System.currentTimeMillis()));
        ticket.setParkingSpot(parkingSpot);
        assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket));
    }

    @Test
    public void calculateFareBikeWithLessThanOneHourParkingTime() {
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
        ticket.setInTime(new Timestamp(System.currentTimeMillis() - (45*60*1000)));
        ticket.setOutTime(new Timestamp(System.currentTimeMillis()));
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals((0.75 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice());
    }

    @Test
    public void calculateFareCarWithLessThanOneHourParkingTime() {
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        ticket.setInTime(new Timestamp(System.currentTimeMillis() - (45*60*1000)));
        ticket.setOutTime(new Timestamp(System.currentTimeMillis()));
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals((0.75 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
    }

    @Test
    public void calculateFareCarWithMoreThanADayParkingTime() {
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        ticket.setInTime(new Timestamp(System.currentTimeMillis() - (24*60*60*1000)));
        ticket.setOutTime(new Timestamp(System.currentTimeMillis()));
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals((24 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
    }

    @Test
    public void apply30FreeMinutesReductionFareBikeLessThan30min() {
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        ticket.setInTime(new Timestamp(System.currentTimeMillis() - (20*60*1000)));
        ticket.setOutTime(new Timestamp(System.currentTimeMillis()));
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        fareCalculatorService.apply30FreeMinutesReduction(ticket);
        assertEquals(0, ticket.getPrice());
    }

    @Test
    public void apply30FreeMinutesReductionFareCarLessThan30min() {
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        ticket.setInTime(new Timestamp(System.currentTimeMillis() - (20*60*1000)));
        ticket.setOutTime(new Timestamp(System.currentTimeMillis()));
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        fareCalculatorService.apply30FreeMinutesReduction(ticket);
        assertEquals(0, ticket.getPrice());
    }

    @Test
    public void apply30FreeMinutesReductionFareBikeEquals30min() {
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        ticket.setInTime(new Timestamp(System.currentTimeMillis() - (30*60*1000)));
        ticket.setOutTime(new Timestamp(System.currentTimeMillis()));
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        fareCalculatorService.apply30FreeMinutesReduction(ticket);
        assertEquals(0, ticket.getPrice());
    }

    @Test
    public void apply30FreeMinutesReductionFareCarEquals30min() {
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        ticket.setInTime(new Timestamp(System.currentTimeMillis() - (30*60*1000)));
        ticket.setOutTime(new Timestamp(System.currentTimeMillis()));
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        fareCalculatorService.apply30FreeMinutesReduction(ticket);
        assertEquals(0, ticket.getPrice());
    }


    @Test
    public void apply30FreeMinutesReductionFareCar() {
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        ticket.setInTime(new Timestamp(System.currentTimeMillis() - (60*60*1000)));
        ticket.setOutTime(new Timestamp(System.currentTimeMillis()));
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        fareCalculatorService.apply30FreeMinutesReduction(ticket);
        assertEquals((Fare.CAR_RATE_PER_HOUR - 0.5 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
    }

    @Test
    public void apply30FreeMinutesReductionFareBike() {
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
        ticket.setInTime(new Timestamp(System.currentTimeMillis() - (60*60*1000)));
        ticket.setOutTime(new Timestamp(System.currentTimeMillis()));
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        fareCalculatorService.apply30FreeMinutesReduction(ticket);
        assertEquals((Fare.BIKE_RATE_PER_HOUR - 0.5 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice());
    }

    @Test
    public void apply30FreeMinutesReductionBikeWithLessThanOneHourParkingTime() {
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
        ticket.setInTime(new Timestamp(System.currentTimeMillis() - (45*60*1000)));
        ticket.setOutTime(new Timestamp(System.currentTimeMillis()));
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        fareCalculatorService.apply30FreeMinutesReduction(ticket);
        assertEquals(((0.75 - 0.5) * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice()); //45min - 30 free first minutes
    }

    @Test
    public void apply30FreeMinutesReductionCarWithLessThanOneHourParkingTime() {
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        ticket.setInTime(new Timestamp(System.currentTimeMillis() - (45*60*1000)));
        ticket.setOutTime(new Timestamp(System.currentTimeMillis()));
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        fareCalculatorService.apply30FreeMinutesReduction(ticket);
        assertEquals(0.38, ticket.getPrice());
    }

    @Test
    public void apply30FreeMinutesReductionCarWithMoreThanADayParkingTime() {
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        ticket.setInTime(new Timestamp(System.currentTimeMillis() - (24*60*60*1000)));
        ticket.setOutTime(new Timestamp(System.currentTimeMillis()));
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        fareCalculatorService.apply30FreeMinutesReduction(ticket);
        assertEquals(24 * Fare.CAR_RATE_PER_HOUR - 0.5 * Fare.CAR_RATE_PER_HOUR, ticket.getPrice());
    }


    @Test
    public void applyRecurrentUserReductionFareCar() {
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        ticket.setInTime(new Timestamp(System.currentTimeMillis() - (60*60*1000)));
        ticket.setOutTime(new Timestamp(System.currentTimeMillis()));
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        fareCalculatorService.applyRecurrentUserReduction(ticket);
        assertEquals(Math.round((Fare.CAR_RATE_PER_HOUR - 0.05 * Fare.CAR_RATE_PER_HOUR) * 100) / 100.0, ticket.getPrice());
    }

    @Test
    public void applyRecurrentUserReductionFareBike() {
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
        ticket.setInTime(new Timestamp(System.currentTimeMillis() - (60*60*1000)));
        ticket.setOutTime(new Timestamp(System.currentTimeMillis()));
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        fareCalculatorService.applyRecurrentUserReduction(ticket);
        assertEquals((Fare.BIKE_RATE_PER_HOUR - 0.05 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice());
    }

    @Test
    public void applyRecurrentUserReductionBikeWithLessThanOneHourParkingTime() {
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
        ticket.setInTime(new Timestamp(System.currentTimeMillis() - (45*60*1000)));
        ticket.setOutTime(new Timestamp(System.currentTimeMillis()));
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
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        ticket.setInTime(new Timestamp(System.currentTimeMillis() - (45*60*1000)));
        ticket.setOutTime(new Timestamp(System.currentTimeMillis()));
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
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        ticket.setInTime(new Timestamp(System.currentTimeMillis() - (24*60*60*1000)));
        ticket.setOutTime(new Timestamp(System.currentTimeMillis()));
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        fareCalculatorService.applyRecurrentUserReduction(ticket);
        assertEquals((24 * Fare.CAR_RATE_PER_HOUR - (0.05 * 24 * Fare.CAR_RATE_PER_HOUR)), ticket.getPrice());
    }

    @Test
    public void applyRecurrentUserReductionCarWith36HoursParkingTime() {
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        ticket.setInTime(new Timestamp(System.currentTimeMillis() - (36*60*60*1000)));
        ticket.setOutTime(new Timestamp(System.currentTimeMillis()));
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        fareCalculatorService.applyRecurrentUserReduction(ticket);
        assertEquals((36 * Fare.CAR_RATE_PER_HOUR - (0.05 * 36 * Fare.CAR_RATE_PER_HOUR)), ticket.getPrice());
    }

    @Test
    public void calculateDuration1Hour() {
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        ticket.setInTime(new Timestamp(System.currentTimeMillis() - (60*60*1000)));
        ticket.setOutTime(new Timestamp(System.currentTimeMillis()));
        ticket.setParkingSpot(parkingSpot);
        Double duration = fareCalculatorService.calculateDuration(ticket);
        assertEquals(Double.valueOf(1), duration);
    }

    @Test
    public void calculateDurationOnMarchTimeOffset() throws ParseException {
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
        ticket.setInTime(new Timestamp(inCalendar.getTime().getTime()));
        ticket.setOutTime(new Timestamp(outCalendar.getTime().getTime()));
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
        ticket.setInTime(new Timestamp(inCalendar.getTime().getTime()));
        ticket.setOutTime(new Timestamp(outCalendar.getTime().getTime()));
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
        ticket.setIsRecurrent(true);
        ticket.setInTime(new Timestamp(inTime.getTime()));
        ticket.setOutTime(new Timestamp(outTime.getTime()));
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
        ticket.setIsRecurrent(true);
        ticket.setInTime(new Timestamp(inTime.getTime()));
        ticket.setOutTime(new Timestamp(outTime.getTime()));
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals(1.5, ticket.getPrice());
        fareCalculatorService.applyReduction(ticket);
        assertEquals(0.71, ticket.getPrice());
    }

}
