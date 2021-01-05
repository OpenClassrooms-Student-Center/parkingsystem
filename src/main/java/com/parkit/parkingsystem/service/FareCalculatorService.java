package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

/**
 * <b>FareCalculatorService class is built to be a calculator.</b>
 * 
 * @author laetitiadamen
 *
 */

public class FareCalculatorService {

  /**
   * calculateFare() method is built to calculate fare depending the duration. inHour / outHour park
   * and depending of vehicle's type. price sets (5% off, 30min free) depending on the
   * duration,vehicle's type and recurrence.
   * 
   * @param ticket Ticket
   * @param recurrent recurrent boolean
   */

  public void calculateFare(Ticket ticket, boolean recurrent) {
    if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
      throw new IllegalArgumentException(
          "Out time provided is incorrect:" + ticket.getOutTime().toString());
    }

    double inHour = ticket.getInTime().getTime();
    double outHour = ticket.getOutTime().getTime();

    double duration = (outHour - inHour) / (1000 * 60 * 60);

    if (duration < Fare.FREE_PARKING_DURATION) {
      duration = 0;
    }
    double reduction = 0;
    if (recurrent == true) {
      reduction = 0.95;
    } else {
      reduction = 1;
    }

    switch (ticket.getParkingSpot().getParkingType()) {
      case CAR: {
        ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR * reduction);
        break;
      }
      case BIKE: {
        ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR * reduction);
        break;
      }
      default:
        throw new IllegalArgumentException("Unkown Parking Type");
    }

  }
}