package com.parkit.parkingsystem.model;

import com.parkit.parkingsystem.constants.ParkingType;

/**
 * <b>ParkingSpotDAO class is built to stock ParkingSpot Data.</b>
 * 
 * <p>
 * DAO means "Data Access Object". It is use to separate how we stock data with the main code. So if
 * stocking Data method change, only functional class will have be to changed.
 * 
 * @see DataBaseConfig
 * @see DBConstants
 * @see ParkingType
 * @see ParkingSpot
 * 
 * @author laetitiadamen
 * 
 */

public class ParkingSpot {
  private int number;
  private ParkingType parkingType;
  private boolean isAvailable;

  public ParkingSpot(int number, ParkingType parkingType, boolean isAvailable) {
    this.number = number;
    this.parkingType = parkingType;
    this.isAvailable = isAvailable;
  }


  public int getId() {
    return number;
  }


  public void setId(int number) {
    this.number = number;
  }


  public ParkingType getParkingType() {
    return parkingType;
  }


  public void setParkingType(ParkingType parkingType) {
    this.parkingType = parkingType;
  }


  public boolean isAvailable() {
    return isAvailable;
  }


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
