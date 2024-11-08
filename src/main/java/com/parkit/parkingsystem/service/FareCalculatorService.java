package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

import java.time.Duration;
import java.time.Instant;



public class FareCalculatorService {

    public static double discountAmount = 0.95;

    public void calculateFare(Ticket ticket)
    {
        calculateFare(ticket,false);
    }

    public void calculateFare(Ticket ticket, boolean discount){

        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }


        System.out.println("Discount = " + discount);

        // Convertir les dates en Instant
        Instant inTimeInstant = ticket.getInTime().toInstant();
        Instant outTimeInstant = ticket.getOutTime().toInstant();

        // Calculer la durée entre les deux dates en heures
        Duration duration = Duration.between(inTimeInstant, outTimeInstant);

        double durationInHours = (double) duration.toMinutes() / 60;

        if(durationInHours <= 0.5)
        {
            ticket.setPrice(0);
        }
        else{
            switch (ticket.getParkingSpot().getParkingType()){
                case CAR: {
                    ticket.setPrice(durationInHours * Fare.CAR_RATE_PER_HOUR);
                    break;
                }
                case BIKE: {
                    ticket.setPrice(durationInHours * Fare.BIKE_RATE_PER_HOUR);
                    break;
                }
                default: throw new IllegalArgumentException("Unkown Parking Type");
            }
        }

        if(discount)
        {
            ticket.setPrice(ticket.getPrice() * discountAmount);
        }

    }
}