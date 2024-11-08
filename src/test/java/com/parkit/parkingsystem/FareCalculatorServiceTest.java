package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;

public class FareCalculatorServiceTest {

    private static FareCalculatorService fareCalculatorService;
    private Ticket ticket;

    @BeforeAll
    public static void setUp() {
        fareCalculatorService = new FareCalculatorService();
    }

    @BeforeEach
    public void setUpPerTest() {
        ticket = new Ticket();
    }

    /**
     * Test pour calculer le tarif d'un véhicule de type voiture.
     * Vérifie que le tarif correspond au tarif par heure pour une heure de stationnement.
     */
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
        assertThat(ticket.getPrice()).isEqualTo(Fare.CAR_RATE_PER_HOUR);
    }

    /**
     * Test pour calculer le tarif d'un véhicule de type moto.
     * Vérifie que le tarif correspond au tarif par heure pour une heure de stationnement.
     */
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
        assertThat(ticket.getPrice()).isEqualTo(Fare.BIKE_RATE_PER_HOUR);
    }

    /**
     * Test pour vérifier le comportement lorsque le type de véhicule est inconnu.
     * Doit lancer une NullPointerException.
     */
    @Test
    public void calculateFareUnknownType() {
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, null, false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket));
    }

    /**
     * Test pour vérifier le comportement lorsque l'heure d'entrée est dans le futur.
     * Doit lancer une IllegalArgumentException.
     */
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

    /**
     * Test pour calculer le tarif d'une moto pour un temps de stationnement inférieur à une heure.
     * Vérifie que le tarif est de 75 % du tarif par heure pour 45 minutes.
     */
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

        assertThat(ticket.getPrice()).isEqualTo(0.75 * Fare.BIKE_RATE_PER_HOUR);
    }

    /**
     * Test pour calculer le tarif d'une voiture pour un temps de stationnement inférieur à une heure.
     * Vérifie que le tarif est de 75 % du tarif par heure.
     */
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

        assertThat(ticket.getPrice()).isEqualTo(0.75 * Fare.CAR_RATE_PER_HOUR);
    }

    /**
     * Test pour calculer le tarif d'une voiture pour un temps de stationnement supérieur à une journée.
     * Vérifie que le tarif est bien égal à 24 fois le tarif par heure.
     * Vérifie que le calcul du temps passé est bon quand la sortie est le lendemain.
     */
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

        assertThat(ticket.getPrice()).isEqualTo(24 * Fare.CAR_RATE_PER_HOUR);
    }

    /**
     * Test pour calculer le tarif d'une voiture pour un temps de stationnement de 30 minutes.
     * Vérifie que le tarif est gratuit.
     */
    @Test
    public void calculateFareCarWithHalfHourParkingTime() {
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (30 * 60 * 1000));
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);

        assertThat(ticket.getPrice()).isEqualTo(0);
    }

    /**
     * Test pour calculer le tarif d'une moto pour un temps de stationnement de 13 minutes.
     * Vérifie que le tarif est gratuit.
     */
    @Test
    public void calculateFareBikeWithLessThanHalfHourParkingTime() {
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (13 * 60 * 1000));
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);

        assertThat(ticket.getPrice()).isEqualTo(0);
    }

    /**
     * Test pour calculer le tarif d'une voiture avec une remise.
     * Vérifie que le tarif reçoit bien la remise de 5%.
     */
    @Test
    public void calculateFareCarWithDiscount() {
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket, true);

        assertThat(ticket.getPrice()).isEqualTo(0.95 * Fare.CAR_RATE_PER_HOUR);
    }

    /**
     * Test pour calculer le tarif d'une moto avec une remise.
     * Vérifie que le tarif reçoit bien la remise de 5%.
     */
    @Test
    public void calculateFareBikeWithDiscount() {
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket, true);

        assertThat(ticket.getPrice()).isEqualTo(0.95 * Fare.BIKE_RATE_PER_HOUR);
    }
}
