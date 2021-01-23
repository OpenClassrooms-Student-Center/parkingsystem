package com.parkit.parkingsystem.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * <b>InputReaderUtil class is built to read user's action.</b>
 * 
 * @see ParkingSpotDAO
 * @see TicketDAO
 * 
 * @author laetitiadamen
 * @version 1.1
 */


import java.util.Scanner;

public class InputReaderUtil {  

  /**
   * Call Scanner
   * 
   * @param scan Scanner
   * @since 1.0
   */
  private static Scanner scan = new Scanner(System.in);

  /**
   * Call Logger
   * 
   * @param logger Logger's name is "InputReaderUtil"
   * @since 1.0
   */  
  private static final Logger logger = LogManager.getLogger("InputReaderUtil");

  /**
   * readSelection is built to read user's selection
   * 
   * @return -1
   * @since 1.0
   */
  public int readSelection() {
    try {
      int input = Integer.parseInt(scan.nextLine());
      return input;
    } catch (Exception e) {
      logger.error("Error while reading user input from Shell", e);
      System.out.println("Error reading input. Please enter valid number for proceeding further");
      return -1;
    }
  }

  /**
   * readVehicleRegistrationNumber is built to read Vehicle Registration Number
   * 
   * @return vehicleRegNumber VehicleRegNumber
   * @throws Exception Invalid vehicle registration number
   */
  public String readVehicleRegistrationNumber() throws Exception {
    try {
      String vehicleRegNumber = scan.nextLine();
      if (vehicleRegNumber == null || vehicleRegNumber.trim().length() == 0) {
        throw new IllegalArgumentException("Invalid input provided");
      }
      return vehicleRegNumber;
    } catch (Exception e) {
      logger.error("Error while reading user input from Shell", e);
      System.out.println(
          "Error reading input. Please enter a valid string for vehicle registration number");
      throw e;
    }
  }

}
