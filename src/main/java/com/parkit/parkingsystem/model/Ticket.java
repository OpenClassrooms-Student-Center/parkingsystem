package com.parkit.parkingsystem.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Ticket {
    private int id;
    private ParkingSpot parkingSpot;
    private String vehicleRegNumber;
    private double price;
    private Date inTime;
    private Date outTime;
    private Boolean isRecurrent;
    private Boolean lastUpdated;

}
