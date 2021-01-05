package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * <b>InteractiveShell class is built to interact user in terminal console.</b>
 * 
 * <p>
 * Users will have 3 choices :
 * <ul>
 * <li>"1 New Vehicle Entering - Allocate Parking Space"</li>
 * <li>"2 Vehicle Exiting - Generate Ticket Price"</li>
 * <li>"3 Shutdown System"</li>
 * </ul>
 * 
 * @see ParkingSpotDAO
 * @see TicketDAO
 * @see InputReaderUtil
 * 
 * @author laetitiadamen
 * @version
 */

public class InteractiveShell {

  /**
   * Call Logger.
   * 
   * @param logger Logger's name is "InteractiveShell"
   * @since 1.0
   */
  private static final Logger logger = LogManager.getLogger("InteractiveShell");

  /**
   * loadInterface is built to set user's interface when application is run. Calling
   * InputReaderUtil/ParkingSpotDAO/TicketDAO/ParkingService. Inside a while loop, will run method
   * depending on user's choice.
   * 
   * @since 3.0
   */
  public static void loadInterface() {
    logger.info("App initialized!!!");
    System.out.println("Welcome to Parking System!");

    boolean continueApp = true;
    InputReaderUtil inputReaderUtil = new InputReaderUtil();
    ParkingSpotDAO parkingSpotDAO = new ParkingSpotDAO();
    TicketDAO ticketDAO = new TicketDAO();
    ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);

    while (continueApp) {
      loadMenu();
      int option = inputReaderUtil.readSelection();
      switch (option) {
        case 1: {
          parkingService.processIncomingVehicle();
          break;
        }
        case 2: {
          parkingService.processExitingVehicle();
          break;
        }
        case 3: {
          System.out.println("Exiting from the system!");
          continueApp = false;
          break;
        }
        default:
          System.out.println(
              "Unsupported option. Please enter a number corresponding to the provided menu");
      }
    }
  }


  /**
   * loadMenu is built to print choices.
   * 
   * @since 1.0
   */
  private static void loadMenu() {
    System.out.println("Please select an option. Simply enter the number to choose an action");
    System.out.println("1 New Vehicle Entering - Allocate Parking Space");
    System.out.println("2 Vehicle Exiting - Generate Ticket Price");
    System.out.println("3 Shutdown System");
  }

}
