package com.parkit.parkingsystem.model;

import java.time.LocalDateTime;

/**
 * Ticket details.
 */
public class Ticket {
    /**
     * ID.
     */
    private int id;
    /**
     * @see ParkingSpot
     */
    private ParkingSpot parkingSpot;
    /**
     * Vehicle Number.
     */
    private String vehicleRegNumber;
    /**
     * Price.
     */
    private double price;
    /**
     * Arrive Time.
     */
    private LocalDateTime inTime;
    /**
     * Exit Time.
     */
    private LocalDateTime outTime;

    /**
     * Getter ID.
     * @return ID
     */
    public int getId() {
        return id;
    }

    /**
     * Setter ID.
     * @param id ID
     */
    public void setId(final int id) {
        this.id = id;
    }

    /**
     * Getter Parking Spot.
     * @return Parking Spot
     */
    public ParkingSpot getParkingSpot() {
        return parkingSpot;
    }

    /**
     * Setter ParingSpot.
     * @param parkingSpot Parking Spot
     */
    public void setParkingSpot(final ParkingSpot parkingSpot) {
        this.parkingSpot = parkingSpot;
    }

    /**
     * Getter Vehicle Number.
     * @return Vehicle Number
     */
    public String getVehicleRegNumber() {
        return vehicleRegNumber;
    }

    /**
     * Setter Vehicle Number.
     * @param vehicleRegNumber vehicle Number
     */
    public void setVehicleRegNumber(final String vehicleRegNumber) {
        this.vehicleRegNumber = vehicleRegNumber;
    }

    /**
     * Getter Price.
     * @return price
     */
    public double getPrice() {
        return price;
    }

    /**
     * Setter price.
     * @param price price
     */
    public void setPrice(final double price) {
        this.price = price;
    }

    /**
     * Getter Arrive Time.
     * @return arrive time
     */
    public LocalDateTime getInTime() {
        return inTime;
    }

    /**
     * Setter Arrive Time.
     * @param inTime arrive time
     */
    public void setInTime(final LocalDateTime inTime) {
        this.inTime = inTime;
    }

    /**
     * Getter Exit Time.
     * @return exit time
     */
    public LocalDateTime getOutTime() {
        return outTime;
    }
    /**
     * Setter Exit Time.
     * @param outTime exit time
     */
    public void setOutTime(final LocalDateTime outTime) {
        this.outTime = outTime;
    }
}
