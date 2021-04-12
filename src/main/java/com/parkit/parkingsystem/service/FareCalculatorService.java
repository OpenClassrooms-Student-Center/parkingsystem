package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

/**
 * @author Philémon Globléhi <philemon.globlehi@gmail.com>
 */
public class FareCalculatorService {

    public void calculateFare(Ticket ticket){
        if( (null == ticket.getOutTime()) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
        }

        int inHour = ticket.getInTime().getHours();
        int outHour = ticket.getOutTime().getHours();

        //TODO: Some tests are failing here. Need to check if this logic is correct
        int duration = outHour - inHour;

        int durationWithoutBonusTime = this.bonusTime(duration);

        switch (ticket.getParkingSpot().getParkingType()){
            case CAR: {
                ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR);
                break;
            }
            case BIKE: {
                ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR);
                break;
            }
            default: throw new IllegalArgumentException("Unkown Parking Type");
        }
    }

    public int bonusTime(int fareDuration) {
        final int BONUS_TIME_IN_MILLISECONDS = 30 * 60 * 1000;

        if (BONUS_TIME_IN_MILLISECONDS < fareDuration) {
            return fareDuration - BONUS_TIME_IN_MILLISECONDS;
        }

        return 1;
    }
}