package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.Ticket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FareCalculatorService {
	
	private static final Logger logger = LogManager.getLogger(FareCalculatorService.class);

	public void calculateFare(Ticket ticket) {
		if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
			throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
		}

		double inHour = ticket.getInTime().getTime();
		double outHour = ticket.getOutTime().getTime();

		double duration = (outHour - inHour) / 3600000;

		if (duration < 0.5) {
			ticket.setPrice(0);
		}

		else {

			switch (ticket.getParkingSpot().getParkingType()) {
			case CAR: {
				TicketDAO ticketDao = new TicketDAO();
				ticketDao.dataBaseConfig = new DataBaseConfig();
				int ticketQuantity = ticketDao.countTicketByVehiculeRegNumber(ticket.getVehicleRegNumber());
				if (ticketQuantity > 1) {
					ticket.setPrice(duration * Fare.CAR_WITH_DISCOUNT);
					logger.info(
							"As a recurring user, you benefit from a 5% discount=" + duration * Fare.CAR_WITH_DISCOUNT);
				} else {
					ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR);
				}
				break;
			}
			case BIKE: {
				TicketDAO ticketDao = new TicketDAO();
				ticketDao.dataBaseConfig = new DataBaseConfig();
				int ticketQuantity = ticketDao.countTicketByVehiculeRegNumber(ticket.getVehicleRegNumber());
				if (ticketQuantity > 1) {
					ticket.setPrice(duration * Fare.BIKE_WITH_DISCOUNT);
					logger.info("As a recurring user of our parking lot, you benefit from a discount="
							+ duration * Fare.BIKE_WITH_DISCOUNT);
				} else {
					ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR);
				}
				break;
			}
			default:
				throw new IllegalArgumentException("Unkown Parking Type");
			}
		}
	}
}