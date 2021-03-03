package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

    public boolean calculateFare(Ticket ticket) {
        if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
            throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
        }

        float totalIntMin;
        float totalOutMin;

        int inDay = ticket.getInTime().getDay();
        int inHour = ticket.getInTime().getHours();
        int inMin = ticket.getInTime().getMinutes();
        int outDay = ticket.getOutTime().getDay();
        int outHour = ticket.getOutTime().getHours();
        int outMin = ticket.getOutTime().getMinutes();

        if (inDay > outDay) {
            outDay = outDay + 7;
        }
        totalIntMin = (inDay * 24 * 60) + (inHour * 60) + inMin;
        totalOutMin = (outDay * 24 * 60) + (outHour * 60) + outMin;
        float duration = totalOutMin - totalIntMin;

        if (duration <= 30) {
            ticket.setPrice(0);
            return false;
        } else {
            double carRatePrice = ((duration / 60) * Fare.CAR_RATE_PER_HOUR * ticket.getDiscountRate());
            double bikeRatePrice = ((duration / 60) * Fare.BIKE_RATE_PER_HOUR * ticket.getDiscountRate());

            switch (ticket.getParkingSpot().getParkingType()) {
                case CAR: {
                    ticket.setPrice(carRatePrice);
                    return true;
                }
                case BIKE: {
                    ticket.setPrice(bikeRatePrice);
                    return true;
                }
                default:
                    throw new IllegalArgumentException("Unkown Parking Type");
            }
        }
    }
}