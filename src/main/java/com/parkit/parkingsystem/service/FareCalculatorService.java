package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket, boolean isDiscount){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }

        double discountCar= 0;
        double discountBike = 0;

        long inHour = ticket.getInTime().getTime();
        long outHour = ticket.getOutTime().getTime();

        long durationMillis = (outHour - inHour);
        double duration = durationMillis / (1000.0*60*60);

        if (isDiscount) {
             discountCar = 0.05 * duration * Fare.CAR_RATE_PER_HOUR;
             discountBike = 0.05 * duration * Fare.BIKE_RATE_PER_HOUR;
        }

        if (durationMillis < (1000.0*30*60)){
            ticket.setPrice(0);
        }

        else {
        switch (ticket.getParkingSpot().getParkingType()) {
            case CAR: {

                double tarif = duration * Fare.CAR_RATE_PER_HOUR - discountCar;
                BigDecimal tarifArrondis = getTarifArrondis(tarif);
                ticket.setPrice(tarifArrondis.doubleValue());
                break;

            }
            case BIKE: {

                double tarif = duration * Fare.BIKE_RATE_PER_HOUR - discountBike;
                BigDecimal tarifArrondis = getTarifArrondis(tarif);
                ticket.setPrice(tarifArrondis.doubleValue());
                break;

            }
            default:
                throw new IllegalArgumentException("Unkown Parking Type");
            }
        }

    }

    private static BigDecimal getTarifArrondis(double tarif) {
        return new BigDecimal(tarif).setScale(3, RoundingMode.HALF_UP);
    }
}