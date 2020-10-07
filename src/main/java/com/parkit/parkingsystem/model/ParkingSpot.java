package com.parkit.parkingsystem.model;

import com.parkit.parkingsystem.constants.ParkingType;

/**
 * ParkingSpot details.
 */
public class ParkingSpot {
    /**
     * Number ID of the parking spot.
     */
    private int number;
    /**
     * Type of the parking spot.
     */
    private ParkingType parkingType;
    /**
     * Availability of the parking spot.
     */
    private boolean isAvailable;

    /**
     * Constructor.
     * @param number : spot ID
     * @param parkingType of spot
     * @param isAvailable of spot
     */
    public ParkingSpot(final int number, final ParkingType parkingType,
                       final boolean isAvailable) {
        this.number = number;
        this.parkingType = parkingType;
        this.isAvailable = isAvailable;
    }

    /**
     * Getter ID.
     * @return number
     */
    public int getId() {
        return number;
    }

    /**
     * Setter ID.
     * @param number : ID of spot
     */
    public void setId(final int number) {
        this.number = number;
    }

    /**
     * Getter parking type.
     * @return parking type
     */
    public ParkingType getParkingType() {
        return parkingType;
    }

    /**
     * Setter parking type.
     * @param parkingType bike, car...
     */
    public void setParkingType(final ParkingType parkingType) {
        this.parkingType = parkingType;
    }

    /**
     * Getter availability.
     * @return availability.
     */
    public boolean isAvailable() {
        return isAvailable;
    }

    /**
     * Setter availability.
     * @param available or not
     */
    public void setAvailable(final boolean available) {
        isAvailable = available;
    }

    /**
     * Check equality of Parking Spot by ID.
     * @param o Parking spot
     * @return ID
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ParkingSpot that = (ParkingSpot) o;
        return number == that.number;
    }

    /**
     * Define Hash with ID.
     * @return ID
     */
    @Override
    public int hashCode() {
        return number;
    }
}
