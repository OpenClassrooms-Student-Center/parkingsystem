package com.parkit.parkingsystem.service;

import static java.time.temporal.ChronoUnit.SECONDS;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.Ticket;

/**
 * this class calculates the price paid by user to exit parking
 *
 * @author Nicolas BIANCUCCI
 *
 */
public class FareCalculatorService {
	/**
	 * number of seconds for one hour
	 */
	private static final int HOUR = 3600;
	/**
	 * number of seconds for half hour
	 */
	private static final int HALFHOUR = 1800;
	/**
	 * ticket DAO object
	 */
	private TicketDAO ticketDAO;

	/**
	 * class constructor
	 *
	 * @param ticketDAO
	 */
	public FareCalculatorService(TicketDAO ticketDAO) {
		super();
		this.ticketDAO = ticketDAO;
	}

	/**
	 * class constructor
	 */
	public FareCalculatorService() {
		this.ticketDAO = new TicketDAO();
	}

	/**
	 * method to checks whether the exit time is correct throw exception if the exit
	 * is null or becomes incomming time
	 *
	 * @param ticket to which calculate fare
	 */
	public void calculateFare(Ticket ticket) {
		if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
			throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
		}

		double durationInSecondes = SECONDS.between(ticket.getInTime().toInstant(), ticket.getOutTime().toInstant());

		switch (ticket.getParkingSpot().getParkingType()) {
		case CAR: {
			if (durationInSecondes > HALFHOUR) {
				ticket.setPrice(durationInSecondes / HOUR * Fare.CAR_RATE_PER_HOUR);
			}
			break;
		}
		case BIKE: {
			if (durationInSecondes > HALFHOUR) {
				ticket.setPrice(durationInSecondes / HOUR * Fare.BIKE_RATE_PER_HOUR);
			}
			break;
		}
		default:
			throw new IllegalArgumentException("Unkown Parking Type");
		}
		if (ticketDAO.isReccurent(ticket)) {
			ticket.setPrice(ticket.getPrice() * 0.95);
		}

	}
}