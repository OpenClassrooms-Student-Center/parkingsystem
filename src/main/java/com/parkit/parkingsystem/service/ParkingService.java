package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.util.InputReaderUtil;
import java.util.Date;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * <b>ParkingService class is built to simulate process incoming and outgoing of vehicle in Park.
 * </b>
 * 
 * @author laetitiadamen
 * @version
 */

public class ParkingService {

  /**
   * Call Logger.
   * 
   * @param logger Logger's name is "ParkingService"
   * @since 1.0
   */
  private static final Logger logger = LogManager.getLogger("ParkingService");

  /**
   * Call class FareCalculatorService.
   * 
   * @param fareCalculatorService calling class FareCalculatorService
   * @since 1.0
   */
  private static FareCalculatorService fareCalculatorService = new FareCalculatorService();

  private InputReaderUtil inputReaderUtil;
  private ParkingSpotDAO parkingSpotDAO;
  private TicketDAO ticketDAO;

  public ParkingService(InputReaderUtil inputReaderUtil, ParkingSpotDAO parkingSpotDAO,
      TicketDAO ticketDAO) {
    this.inputReaderUtil = inputReaderUtil;
    this.parkingSpotDAO = parkingSpotDAO;
    this.ticketDAO = ticketDAO;
  }


  /**
   * processIncomingVehicle is built to simulate incoming of vehicle in parking. In a
   * try/catch/finally instruction, it will :
   * <ul>
   * <li>call getNextParkingNumberIfAvailable method</li>
   * <li>update ParkingSpot</li>
   * <li>set and save new ticket</li>
   * </ul>
   * 
   * @param inTime inTime of incoming vehicle
   * @since
   * 
   */
  public void processIncomingVehicle() {
    try {
      ParkingSpot parkingSpot = getNextParkingNumberIfAvailable();
      if (parkingSpot != null && parkingSpot.getId() > 0) {
        String vehicleRegNumber = getVehichleRegNumber();
        parkingSpot.setAvailable(false);
        // allot this parking space and mark it's availability as false
        parkingSpotDAO.updateParking(parkingSpot);

        Date inTime = new Date();
        Ticket ticket = new Ticket();
        // ID, PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME)
        // ticket.setId(ticketID);
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber(vehicleRegNumber);
        ticket.setPrice(0);
        ticket.setInTime(inTime);
        ticket.setOutTime(null);
        ticketDAO.saveTicket(ticket);
        System.out.println("Generated Ticket and saved in DB");
        System.out.println("Please park your vehicle in spot number:" + parkingSpot.getId());
        System.out
            .println("Recorded in-time for vehicle number:" + vehicleRegNumber + " is:" + inTime);
      }
    } catch (Exception e) {
      logger.error("Unable to process incoming vehicle", e);
    }
  }


  /**
   * getVehichleRegNumber is built to get vehicle regulation number with
   * inputReaderUtil.readVehicleRegistrationNumber().
   * 
   * @return vehicle number
   * @throws Exception incorrect vehicle regulation number
   */
  private String getVehichleRegNumber() throws Exception {
    System.out.println("Please type the vehicle registration number and press enter key");
    return inputReaderUtil.readVehicleRegistrationNumber();
  }


  /**
   * getNextParkingNumberIfAvailable is built to get next parking number if available using
   * parkingSpotDAO.getNextAvailableSlot(parkingType).
   * 
   * @return parkingSpot
   */
  public ParkingSpot getNextParkingNumberIfAvailable() {
    int parkingNumber = 0;
    ParkingSpot parkingSpot = null;
    try {
      ParkingType parkingType = getVehichleType();
      parkingNumber = parkingSpotDAO.getNextAvailableSlot(parkingType);
      if (parkingNumber > 0) {
        parkingSpot = new ParkingSpot(parkingNumber, parkingType, true);
      } else {
        throw new Exception("Error fetching parking number from DB. Parking slots might be full");
      }
    } catch (IllegalArgumentException ie) {
      logger.error("Error parsing user input for type of vehicle", ie);
    } catch (Exception e) {
      logger.error("Error fetching next available parking slot", e);
    }
    return parkingSpot;
  }


  /**
   * getVehichleType is built to get the vehicle type using switch and
   * inputReaderUtil.readSelection().
   * 
   * @return ParkingType.CAR car
   * @return ParkingType.BIKE bike
   */
  private ParkingType getVehichleType() {
    System.out.println("Please select vehicle type from menu");
    System.out.println("1 CAR");
    System.out.println("2 BIKE");
    int input = inputReaderUtil.readSelection();
    switch (input) {
      case 1: {
        return ParkingType.CAR;
      }
      case 2: {
        return ParkingType.BIKE;
      }
      default: {
        System.out.println("Incorrect input provided");
        throw new IllegalArgumentException("Entered input is invalid");
      }
    }
  }


  /**
   * processExitingVehicle is built to simulate outgoing of vehicle in parking. In a
   * try/catch/finally instruction, it will :
   * <ul>
   * <li>call getTicket from ticketDAO</li>
   * <li>set out time</li>
   * <li>call calculateFare from fareCalculatorService</li>
   * <li>call updateParking from ticketDAO</li>
   * </ul>
   * 
   * @since
   */
  public void processExitingVehicle() {
    boolean recurrent = false;
    try {
      String vehicleRegNumber = getVehichleRegNumber();
      Ticket ticket = ticketDAO.getTicket(vehicleRegNumber);
      Date outTime = new Date();
      ticket.setOutTime(outTime); 
      recurrent = ticketDAO.recurrentCustomer(vehicleRegNumber);
      fareCalculatorService.calculateFare(ticket, recurrent);      
      if (ticketDAO.updateTicket(ticket)) {
        ParkingSpot parkingSpot = ticket.getParkingSpot();
        parkingSpot.setAvailable(true);
        parkingSpotDAO.updateParking(parkingSpot);
        System.out.println("Please pay the parking fare:" + ticket.getPrice());
        System.out.println("Recorded out-time for vehicle number:" + ticket.getVehicleRegNumber()
            + " is:" + outTime);
      } else {
        System.out.println("Unable to update ticket information. Error occurred");
      }
    } catch (Exception e) {
      logger.error("Unable to process exiting vehicle", e);
    }
  }
}
