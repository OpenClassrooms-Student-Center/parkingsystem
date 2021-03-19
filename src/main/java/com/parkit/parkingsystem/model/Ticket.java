package com.parkit.parkingsystem.model;

import java.util.Date;

/**
 * this class permits the storage and retrieving values to ticket table from
 * database
 *
 * @author Nicolas BIANCUCCI
 *
 */
public class Ticket {
	/**
	 * represents unique id of ticket
	 */
	private int id;
	/**
	 * number of parkingspot in wich the vehicule will park
	 */
	private ParkingSpot parkingSpot;
	/**
	 * represents registration number vehicule
	 */
	private String vehicleRegNumber;
	/**
	 * price will paid by user customer to exit parking
	 */
	private double price;
	/**
	 * It's the time to enters to the parking
	 */
	private Date inTime;
	/**
	 * It's the time to exit to the parking
	 */
	private Date outTime;

	/**
	 * getter of ticket id
	 *
	 * @return int ticket indentifier
	 */
	public int getId() {
		return id;
	}

	/**
	 * setter of ticket id
	 *
	 * @param id to ticket
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * getter of the ticket parkingspot
	 *
	 * @return parking spot instance
	 */
	public ParkingSpot getParkingSpot() {
		return parkingSpot;
	}

	/**
	 * setter of the ticket parking spot
	 *
	 * @param parkingSpot instance
	 */
	public void setParkingSpot(ParkingSpot parkingSpot) {
		this.parkingSpot = parkingSpot;
	}

	/**
	 * getter of the ticket vehiculeRegNumber
	 *
	 * @return vehicule registration number associated with ticket
	 */
	public String getVehicleRegNumber() {
		return vehicleRegNumber;
	}

	/**
	 * setter of the ticket vehiculeRegNumber
	 *
	 * @param vehicleRegNumber instance
	 */
	public void setVehicleRegNumber(String vehicleRegNumber) {
		this.vehicleRegNumber = vehicleRegNumber;
	}

	/**
	 * getter of ticket price
	 *
	 * @return the price to user paid to exit parking
	 */
	public double getPrice() {
		return price;
	}

	/**
	 * setter of ticket price
	 *
	 * @param price to be set on the ticket
	 */
	public void setPrice(double price) {
		this.price = price;
	}

	/**
	 * getter of the ticket inTime
	 *
	 * @return incoming time
	 */
	public Date getInTime() {
		if (inTime == null) {
			return null;
		} else {
			return new Date(inTime.getTime());
		}
	}

	/**
	 * setter of ticket inTime
	 *
	 * @param inTime it's time to be set on the ticket
	 */
	public void setInTime(Date inTime) {
		if (inTime == null) {
			this.inTime = null;
		} else {
			this.inTime = new Date(inTime.getTime());
		}
	}

	/**
	 * getter of the ticket outTime
	 *
	 * @return time to user exit parking
	 */
	public Date getOutTime() {
		if (outTime == null) {
			return null;
		} else {
			return new Date(outTime.getTime());
		}
	}

	/**
	 * setter of the ticket outTime
	 *
	 * @param outTime it's time to be set on the ticket
	 */
	public void setOutTime(Date outTime) {
		if (outTime == null) {
			this.outTime = null;
		} else {
			this.outTime = new Date(outTime.getTime());
		}

	}

}
