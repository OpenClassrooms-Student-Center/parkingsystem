package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {
    public void calculateFare(Ticket ticket) {
        calculateFare(ticket, false); // Par défaut, aucun discount
    }
    public void calculateFare(Ticket ticket, boolean discount){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }

        long durationInMillis = ticket.getOutTime().getTime() - ticket.getInTime().getTime();

        double durationInHours = durationInMillis / (1000.0 * 60 * 60);

        if(durationInMillis <  30 * 60 * 1000){
            ticket.setPrice(0);
            return;
        }

        double ratePerHour;

        switch (ticket.getParkingSpot().getParkingType()){
            case CAR:
                ratePerHour = Fare.CAR_RATE_PER_HOUR;
                break;
            case BIKE:
                ratePerHour = Fare.BIKE_RATE_PER_HOUR;
                break;
            default:
                throw new IllegalArgumentException("Unknown Parking Type");
        }

        double price = durationInHours * ratePerHour;

        // Applying 5% discount
//        if (discount) {
//            price *= 0.95;
//        }
        ticket.setPrice(price);

    }
}