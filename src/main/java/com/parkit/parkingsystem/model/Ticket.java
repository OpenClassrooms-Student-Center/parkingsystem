package com.parkit.parkingsystem.model;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.util.DateUtil;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;

public class Ticket {
    private static final double THIRTY_MINUTES = 0.5; // 1hour/2
    private int id;
    private ParkingSpot parkingSpot;
    private String vehicleRegNumber;
    private double price;
    private Date inTime;
    private Date outTime;
    private boolean discount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ParkingSpot getParkingSpot() {
        return parkingSpot;
    }

    public void setParkingSpot(ParkingSpot parkingSpot) {
        this.parkingSpot = parkingSpot;
    }

    public String getVehicleRegNumber() {
        return vehicleRegNumber;
    }

    public void setVehicleRegNumber(String vehicleRegNumber) {
        this.vehicleRegNumber = vehicleRegNumber;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getInTime() {
        return inTime;
    }

    public void setInTime(Date inTime) {
        this.inTime = inTime;
    }

    public Date getOutTime() {
        return outTime;
    }

    public void setOutTime(Date outTime) {
        this.outTime = outTime;
    }

    public boolean hasDiscount() {
        return discount;
    }

    public void setDiscount(boolean discount) {
        this.discount = discount;
    }

    public boolean isInvalidTime() {
        return this.outTime == null || this.outTime.before(this.inTime);
    }

    public void computePrice(double ratePerHour) {
        Duration duration = getDuration();

        if (isLessThanOrEqualTo30Min(duration)) {
            this.setPrice(0);
        } else {
            double hours = (double) duration.toMinutes() / 60;
            this.setPrice(this.discount ? hours * ratePerHour * Fare.FARE_DISCOUNT : hours * ratePerHour);
        }
    }

    public Duration getDuration() {
        LocalDateTime in = DateUtil.toLocalDateTime(inTime);
        LocalDateTime out = DateUtil.toLocalDateTime(outTime);
        return Duration.between(in, out);
    }

    public int getDurationInMinutes() {
        Duration duration = getDuration();
        return Math.toIntExact(duration.toMinutes());
    }

    public int getDurationInHours() {
        Duration duration = getDuration();
        return Math.toIntExact(duration.toHours());
    }

    private boolean isLessThanOrEqualTo30Min(double duration) {
        return duration <= THIRTY_MINUTES;
    }

    private boolean isLessThanOrEqualTo30Min(Duration duration) {
        return duration.toMinutes() <= 30;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", vehicleRegNumber='" + vehicleRegNumber + '\'' +
                ", price=" + price +
                ", inTime=" + inTime +
                ", outTime=" + outTime +
                ", discount=" + discount +
                '}';
    }
}