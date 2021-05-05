package com.parkit.parkingsystem.model;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class Ticket {
    private int id;
    private ParkingSpot parkingSpot;
    private String vehicleRegNumber;
    private double price;

    private Timestamp inTime;
    private Timestamp outTime;

    public Timestamp getInTime() {
        if (inTime == null ){
            return null;
        } else {
            return new Timestamp(inTime.getTime());
        }
    }

    public void setInTime(Timestamp inTime) {
        if (inTime == null) {
            this.inTime = null;
        } else {
            this.inTime = new Timestamp(inTime.getTime());
        }
    }

    public Timestamp getOutTime() {
        if (outTime == null) {
            return null;
        } else {
            return new Timestamp(outTime.getTime());
        }
    }

    public void setOutTime(Timestamp outTime) {
        if (outTime == null) {
            this.outTime = null;
        } else {
            this.outTime = new Timestamp(outTime.getTime());
        }

    }


    private Boolean isRecurrent;
    private Boolean lastUpdated;

}
