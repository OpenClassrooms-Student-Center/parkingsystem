package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

	public void calculateFare(Ticket ticket, boolean isRecurrentUser) {
		if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
			throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
		}

		// Retourne si Reccurent user ou non
		TicketDAO ticketDAO = new TicketDAO();
		ticketDAO.isReccurentUser(ticket.getVehicleRegNumber());
		// Retourne le nombre de millisecondes de InTime
		long inHour = ticket.getInTime().getTime();
		// Retourne le nombre de millisecondes de InTime OutTime
		long outHour = ticket.getOutTime().getTime();

		// TODO#1: Some tests are failing here. Need to check if this logic is correct

		// Pour calculer la durée en minutes
		long duration = (outHour - inHour) / 60000;
		// Si la durée est supérieur à 30 on procedera au calcul suivant
		if (duration > 30) {
			// If the user is recurring a discount of 5% is applied
			Double getDiscountOrNot = isRecurrentUser ? (60 / 0.95) : 60;

			switch (ticket.getParkingSpot().getParkingType()) {
			case CAR: {
				ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR / getDiscountOrNot);
				break;
			}
			case BIKE: {
				ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR / getDiscountOrNot);
				break;
			}
			default:
				throw new IllegalArgumentException("Unkown Parking Type");
			}
		}
		// Si la durée est inférieur à 30 min pas de fee
		else {
			ticket.setPrice(0);
		}

	}
}