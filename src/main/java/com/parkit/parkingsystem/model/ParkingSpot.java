package com.parkit.parkingsystem.model;

import com.parkit.parkingsystem.constants.ParkingType;

/**
 * this class permit to storage and retrieve values from DB
 *
 * @author Nicolas BIANCUCCI
 *
 */
public class ParkingSpot {
	/**
	 * Parking spot ID
	 */
	private int number;

	/**
	 * Parkingtype chose type vehicle
	 */
	private ParkingType parkingType;

	/**
	 * availability of slot to parking spot
	 */
	private boolean isAvailable;

	/**
	 * class constructor
	 *
	 * @param number      parking spot id
	 * @param parkingType type of vehicle
	 * @param isAvailable available parking spot or not
	 */
	public ParkingSpot(int number, ParkingType parkingType, boolean isAvailable) {
		this.number = number;
		this.parkingType = parkingType;
		this.isAvailable = isAvailable;
	}

	/**
	 * getter of the parkingspot id
	 *
	 * @return the unique number of a given parking spot
	 */
	public int getId() {
		return number;
	}

	/**
	 * setter of the parkingspot id
	 *
	 * @param number is parkingspot id
	 */
	public void setId(int number) {
		this.number = number;
	}

	/**
	 * getter of parkingtype
	 *
	 * @return the appropriate parking type
	 */
	public ParkingType getParkingType() {
		return parkingType;
	}

	/**
	 * setter of the parking type
	 *
	 * @param parkingType the appropriate parking type
	 */
	public void setParkingType(ParkingType parkingType) {
		this.parkingType = parkingType;
	}

	/**
	 * getter of parkingspot is available
	 *
	 * @return true if that parking spot is available
	 */
	public boolean isAvailable() {
		return isAvailable;
	}

	/**
	 * setter of the parkingspot is available
	 *
	 * @param available true if a given parkingspot is available false if busy
	 */
	public void setAvailable(boolean available) {
		isAvailable = available;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		ParkingSpot that = (ParkingSpot) o;
		return number == that.number;
	}

	@Override
	public int hashCode() {
		return number;
	}
}
