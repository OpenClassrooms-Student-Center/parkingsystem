package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.Ticket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.parkit.parkingsystem.constants.ParkingType.BIKE;
import static com.parkit.parkingsystem.constants.ParkingType.CAR;

public class FareCalculatorService {

    private static final Logger logger = LogManager.getLogger(FareCalculatorService.class);

    public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }
        double duration = calculateDuration(ticket);
        try {
            ticket.setPrice(duration * getRate(ticket.getParkingSpot().getParkingType()));
            logger.info("Price calculation succeded. Ticket saved.");
        } catch ( IllegalArgumentException e ) {
            logger.error("There was an error in price calculation. Price not saved.");
        }
    }

    public void applyReduction(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }
        apply30FreeMinutesReduction(ticket);
        if (ticket.getRecurrent()) {
            System.out.println("Recurrent User 5% reduction applied.");
            applyRecurrentUserReduction(ticket);
        }
    }


    public void apply30FreeMinutesReduction(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }
        double duration = calculateDuration(ticket);
        if (duration >= 0.5) {
            ticket.setPrice(ticket.getPrice() - (double)30/60 * getRate(ticket.getParkingSpot().getParkingType()));
            ticket.setPrice(Math.round((ticket.getPrice())*100)/100.0);
        } else {
            ticket.setPrice(0);
        }
    }

    public void applyRecurrentUserReduction(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }
        double reduction = 0.05 * ticket.getPrice();
        ticket.setPrice(ticket.getPrice() - reduction);
        ticket.setPrice(Math.round((ticket.getPrice())*100)/100.0);
    }

    public double calculateDuration(Ticket ticket) {
        Calendar inCalendar = Calendar.getInstance();
        Calendar outCalendar = Calendar.getInstance();
        inCalendar.setTime(ticket.getInTime());
        outCalendar.setTime(ticket.getOutTime());

        double  durationHour = (outCalendar.get(Calendar.HOUR_OF_DAY) - inCalendar.get(Calendar.HOUR_OF_DAY));
        if (outCalendar.get(Calendar.DST_OFFSET)-inCalendar.get(Calendar.DST_OFFSET) == 3600000) {
            durationHour -= 1;
        }
        return (outCalendar.get(Calendar.DAY_OF_MONTH) - inCalendar.get(Calendar.DAY_OF_MONTH))*24+
                durationHour  + (double)(outCalendar.get(Calendar.MINUTE) - inCalendar.get(Calendar.MINUTE))/60;
    }

    private double getRate(ParkingType type) {
        Map<ParkingType, Double> rates = new HashMap();
        rates.put(CAR, Fare.CAR_RATE_PER_HOUR);
        rates.put(BIKE, Fare.BIKE_RATE_PER_HOUR);
        return rates.get(type);
    }
}