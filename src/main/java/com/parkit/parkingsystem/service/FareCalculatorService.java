package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.Ticket;

import java.time.Duration;
import java.util.Date;

public class FareCalculatorService {

    private TicketDAO ticketDAO = new TicketDAO();

    public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }

        Date inHour = ticket.getInTime();
        Date outHour = ticket.getOutTime();

        Duration duration = Duration.between(inHour.toInstant(), outHour.toInstant());

        double reductionFactor = 1;
        if( ticketDAO.getTicket(ticket.getVehicleRegNumber()).getOutTime() != null ){
            reductionFactor = 0.95;
        }

        switch (ticket.getParkingSpot().getParkingType()){
            case CAR: {
                if (duration.getSeconds() <= 1800) {
                    ticket.setPrice(0);
                } else {
                    ticket.setPrice(reductionFactor * (duration.getSeconds() / 3600.0) * Fare.CAR_RATE_PER_HOUR);
                }
                break;
            }
            case BIKE: {
                if (duration.getSeconds() <= 1800) {
                    ticket.setPrice(0);
                } else {
                    ticket.setPrice(reductionFactor * (duration.getSeconds()/3600.0) * Fare.BIKE_RATE_PER_HOUR);
                }
                break;
            }
            default: throw new IllegalArgumentException("Unkown Parking Type");
        }
    }
}