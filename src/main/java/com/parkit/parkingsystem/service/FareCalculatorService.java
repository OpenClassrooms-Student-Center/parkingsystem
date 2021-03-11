package com.parkit.parkingsystem.service;

import java.util.concurrent.TimeUnit;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

	// TimeUnit convertit en millisecondes to minutes
	private static final double HOUR_IN_MILLISECONDS = TimeUnit.MINUTES.toMillis(60);
	private static final double HALF_HOUR_IN_MILLISECONDS = TimeUnit.MINUTES.toMillis(30);
	private double priceTicket;
	private double priceTicketConvert;

	public void calculateFare(Ticket ticket) {
		if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
			throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime());
		}

		// getTime renvoie des millisecondes en minutes
		double inHour = ticket.getInTime().getTime();
		double outHour = ticket.getOutTime().getTime();

		// TODO: Some tests are failing here. Need to check if this logic is correct
		// Pour calculer la duree en fonction de la date d'entree et la date de sortie
		double duration = outHour - inHour;

		// Si la duree est inférieur à 30 min le parking est gratuit
		if (duration <= HALF_HOUR_IN_MILLISECONDS) {
			ticket.setPrice(0);
			return;
		}

		if (ticket.isReccuringUser == true) {
			priceTicket = ((duration * Fare.CAR_RATE_PER_HOUR) / HOUR_IN_MILLISECONDS * Fare.DISCOUNT);
			priceTicketConvert = (double) Math.round(priceTicket * 100) / 100;
			ticket.setDiscount(priceTicketConvert);
		}

		switch (ticket.getParkingSpot().getParkingType()) {
		case CAR: {
			priceTicket = (duration * Fare.CAR_RATE_PER_HOUR / HOUR_IN_MILLISECONDS);
			priceTicketConvert = (double) Math.round(priceTicket * 100) / 100;
			ticket.setPrice(priceTicketConvert);
			break;
		}
		case BIKE: {
			priceTicket = (duration * Fare.BIKE_RATE_PER_HOUR / HOUR_IN_MILLISECONDS);
			priceTicketConvert = (double) Math.round(priceTicket * 100) / 100;
			ticket.setPrice(priceTicketConvert);
			break;
		}
		default:
			throw new NullPointerException("Unkown Parking Type");
		}

	}
}