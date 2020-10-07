package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.Ticket;

import java.time.Duration;

/**
 * Service for fare calculator.
 */
public class FareCalculatorService {

    /**
     * Calculate fare.
     * @param ticket from user
     */
    public void calculateFare(final Ticket ticket) {
        if (ticket.getParkingSpot() == null) {
            throw new IllegalArgumentException("No ParkingSpot provided");
        }

        if (ticket.getInTime() == null) {
            throw new IllegalArgumentException("No In Time provided");
        }

        if (ticket.getOutTime() == null) {
            throw new IllegalArgumentException("No Out Time provided");
        }

        if (ticket.getOutTime().isBefore(ticket.getInTime())) {
            throw new IllegalArgumentException("Out time provided is incorrect:"
                    + ticket.getOutTime().toString());
        }

        double duration = (double) (Duration.between(
                ticket.getInTime(), ticket.getOutTime()).toMinutes()) / 60;

        ParkingType parkingType = ticket.getParkingSpot().getParkingType();

        if (parkingType == null) {
            throw new IllegalArgumentException("Unkown Parking Type");
        } else {
            switch (parkingType) {
                case CAR: {
                    ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR);
                    break;
                }
                case BIKE: {
                    ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR);
                    break;
                }
                default:
            }
        }
    }
}