package com.parkit.parkingsystem.model;

import java.util.Calendar;
import java.util.Date;

/**
 * <b> Ticket class is built to stock all methods referencing ticket. </b>
 * 
 * <p>
 * Methods :
 * <ul>
 * <li>getId</li>
 * <li>setId</li>
 * <li>getParkingSpot</li>
 * <li>setParkingSpot</li>
 * <li>getVehicleRegNumber</li>
 * <li>setVehicleRegNumber</li>
 * <li>getPrice</li>
 * <li>setPrice</li>
 * <li>getInTime</li>
 * <li>setInTime</li>
 * <li>getOutTime</li>
 * <li>setOutTime</li>
 * </ul>
 * 
 * 
 * @see DataBaseConfig
 * @see DBConstants
 * @see ParkingType
 * 
 * @author laetitiadamen
 * @version 1.1
 * 
 */

public class Ticket {
  private int id;
  private ParkingSpot parkingSpot;
  private String vehicleRegNumber;
  private double price;
  private Date inTime;
  private Date outTime;

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
}
