package com.parkit.parkingsystem.service;

import static java.time.temporal.ChronoUnit.SECONDS;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

	public void calculateFare(Ticket ticket) {
		if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
			throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
		}

		double duration = SECONDS.between(ticket.getInTime().toInstant(), ticket.getOutTime().toInstant());

		switch (ticket.getParkingSpot().getParkingType()) {
		case CAR: {
			if (duration > 1800) {
				ticket.setPrice(duration / 3600 * Fare.CAR_RATE_PER_HOUR);
			}
			break;
		}
		case BIKE: {
			if (duration > 1800) {
				ticket.setPrice(duration / 3600 * Fare.BIKE_RATE_PER_HOUR);
			}
			break;
		}
		default:
			throw new IllegalArgumentException("Unkown Parking Type");
		}
		if (ticket.isDiscount())
			ticket.setPrice(ticket.getPrice() * 0.95);

	}
}