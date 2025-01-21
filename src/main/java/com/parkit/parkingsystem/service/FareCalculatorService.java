package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;


public class FareCalculatorService {


    public void calculateFare(Ticket ticket, boolean discount) {

        if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
            throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
        }
        // Calculer la durée en millisecondes
        long inTimeMillis = ticket.getInTime().getTime();
        long outTimeMillis = ticket.getOutTime().getTime();
        long durationMillis = (outTimeMillis - inTimeMillis);
        // Si la durée est inférieure à 30 minutes, le prix est 0
        if (durationMillis <= 30 * 60 * 1000) {
            ticket.setPrice(0);
            return;
        }
        double durationHours = durationMillis / 60.0 / 60 / 1000;// Diviser pour obtenir la durée en heure
        // Appliquer les tarifs en fonction du type de véhicule
        if (!discount) {
            switch (ticket.getParkingSpot().getParkingType()) {
                case CAR: {
                    ticket.setPrice(durationHours * Fare.CAR_RATE_PER_HOUR);
                    break;
                }
                case BIKE: {
                    ticket.setPrice(durationHours * Fare.BIKE_RATE_PER_HOUR);
                    break;
                }
                default:
                    throw new IllegalArgumentException("Unkown Parking Type");
            }
        } else {
            switch (ticket.getParkingSpot().getParkingType()) {
                case CAR: {
                    ticket.setPrice(durationHours * Fare.CAR_RATE_PER_HOUR * 0.95);
                    break;
                }
                case BIKE: {
                    ticket.setPrice(durationHours * Fare.BIKE_RATE_PER_HOUR * 0.95);
                    break;
                }
                default:
                    throw new IllegalArgumentException("Unkown Parking Type");
            }
        }

    }

    public void calculateFare(Ticket ticket) {
        calculateFare(ticket, false);
    }
}