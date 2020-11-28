package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;


public class FareCalculatorService {

    public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }
        double duration = calculateDuration(ticket);

        switch (ticket.getParkingSpot().getParkingType()){
            case CAR : {
                if (duration <= 0.5) {
                    ticket.setPrice(0);
                    }
                else {
                    ticket.setPrice((duration - 0.5) * Fare.CAR_RATE_PER_HOUR);
                }
                break;
            }
            case BIKE : {
                if (duration <= 0.5) {
                    ticket.setPrice(0);
                }
                else {
                    ticket.setPrice((duration - 0.5) * Fare.BIKE_RATE_PER_HOUR);
                }
                break;
            }
            default: throw new IllegalArgumentException("Unknown Parking Type");
        }
    }
    public double calculateDuration (Ticket ticket) {
        Instant inH = ticket.getInTime().toInstant();
        Instant outH = ticket.getOutTime().toInstant();

        LocalDateTime inHour = LocalDateTime.ofInstant(inH, ZoneId.systemDefault());
        LocalDateTime outHour = LocalDateTime.ofInstant(outH, ZoneId.systemDefault());

        Duration duration = Duration.between(inHour, outHour);

        double dur = (double)duration.getSeconds() / 3600;
        double dr = Math.round(dur * 100.0) /100.0;
        return dr;
    }
}