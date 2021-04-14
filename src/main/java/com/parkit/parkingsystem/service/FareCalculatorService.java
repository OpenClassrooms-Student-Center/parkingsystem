package com.parkit.parkingsystem.service;

import java.time.Duration;
import java.util.Date;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {


	public void calculateFare(Ticket ticket) {
		System.out.println(ticket.getInTime());
		System.out.println(ticket.getOutTime());
		if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
			throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
		}

		Date inHour = ticket.getInTime();
		Date outHour = ticket.getOutTime();

// TODO: Some tests are failing here. Need to check if this logic is correct
		Duration duration = Duration.between(inHour.toInstant(), outHour.toInstant());

		double reductionUserRecurring = 1;
		if (ticket.isUserRecurring()) {
			reductionUserRecurring = 0.95;
		}

		switch (ticket.getParkingSpot().getParkingType()) {
		case CAR:

			if (duration.getSeconds() <= 1800) {
				ticket.setPrice(0);
			} else {
				ticket.setPrice(Fare.roundedFare(
						reductionUserRecurring * (duration.getSeconds() / 3600.0) * Fare.CAR_RATE_PER_HOUR));
			}
			break;
		case BIKE:

			if (duration.getSeconds() <= 1800) {
				ticket.setPrice(0);
			} else {
				ticket.setPrice(Fare.roundedFare(
						reductionUserRecurring * (duration.getSeconds() / 3600.0) * Fare.BIKE_RATE_PER_HOUR));
			}

			break;
		default:
			throw new IllegalArgumentException("Unkown Parking Type");
		}

	}
}