package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

/**
 * Interactive Shell with options.
 */
public class InteractiveShell {

    /**
     * @see Logger
     */
    private static final Logger LOGGER =
            LoggerFactory.getLogger(InteractiveShell.class);

    static InputReaderUtil inputReaderUtil = new InputReaderUtil(new Scanner(System.in));

    private static ParkingSpotDAO parkingSpotDAO = new ParkingSpotDAO();

    private static TicketDAO ticketDAO = new TicketDAO();

    static ParkingService parkingService =
            new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);

    /**
     * Load Interface depending on option.
     */
    public static void loadInterface() throws Exception {
        LOGGER.info("App initialized!!!");
        LOGGER.info("Welcome to Parking System!");

        boolean continueApp = true;
        while (continueApp) {
            loadMenu();
            int option = inputReaderUtil.readSelection();
            switch (option) {
                case 1:
                    parkingService.processIncomingVehicle();
                    continueApp = false;
                    break;
                case 2:
                    parkingService.processExitingVehicle();
                    continueApp = false;
                    break;
                case 3:
                    LOGGER.info("Exiting from the system!");
                    continueApp = false;
                    break;
                default:
                    LOGGER.info("Unsupported option. Please "
                        + "enter a number corresponding to the provided menu");
                    break;
            }
        }
    }

    /**
     * Load Menu.
     */
    private static void loadMenu() {
        LOGGER.info("Please select an option."
                + "Simply enter the number to choose an action");
        LOGGER.info("1 New Vehicle Entering - Allocate Parking Space");
        LOGGER.info("2 Vehicle Exiting - Generate Ticket Price");
        LOGGER.info("3 Shutdown System");
    }
}
