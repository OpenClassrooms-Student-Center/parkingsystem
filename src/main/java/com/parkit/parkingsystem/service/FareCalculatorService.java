package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.util.*;
import com.parkit.parkingsystem.dao.*;

public class FareCalculatorService {

	 private  TicketDAO ticketDAO;
	 String vehicleRegNumber;
	 public int getDuration(Ticket ticket) {
	 long inHour = ticket.getInTime().getTime();// time in milliseconds from 1 jan 1970
		long outHour = ticket.getOutTime().getTime();
		int duration = (int) ((outHour - inHour)/(1000 *60));
		System.out.println("duration of the time " + duration);
		return duration;
	 }
	public void calculateFare(Ticket ticket){
		if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
			throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
		}
		//TODO: Some tests are failing here. Need to check if this logic is correct

		int duration =getDuration(ticket);
		if (duration > 30 ) {

			switch (ticket.getParkingSpot().getParkingType()){

			case CAR: {
				ticket.setPrice(duration *( Fare.CAR_RATE_PER_HOUR/60));
				break;
			}
			case BIKE: {
				ticket.setPrice(duration * (Fare.BIKE_RATE_PER_HOUR/60));
				break;
			}
			default: throw new IllegalArgumentException("Unkown Parking Type");
			}
		}else {
			ticket.setPrice(0.0);
		}
	
	}

	
	public void calcualteDiscount(Ticket ticket){
		if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
			throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
		}
		int duration =getDuration(ticket);
		try {
			 Ticket vehicleNumberTicket = ticketDAO.getTicket(vehicleRegNumber);
			InputReaderUtil resultFromDatabase = new InputReaderUtil();
			if (resultFromDatabase.readVehicleRegistrationNumber().equals(vehicleNumberTicket)) {
				switch (ticket.getParkingSpot().getParkingType()){

				case CAR: {
					ticket.setPrice(duration *( Fare.CAR_RATE_PER_HOUR/60*0.95));
					break;
				}
				case BIKE: {
					ticket.setPrice(duration * (Fare.BIKE_RATE_PER_HOUR/60*0.95));
					break;
				}
				default: throw new IllegalArgumentException("Unkown Parking Type");
				}
			}
		}catch(Exception e){
			e.getStackTrace();
			
		}
	}
}
