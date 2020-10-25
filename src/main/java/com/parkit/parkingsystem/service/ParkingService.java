package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

/**
 * Service for Park.
 */
public class ParkingService {

    /**
     * @see Logger
     */
    private static final Logger logger = LoggerFactory.getLogger(ParkingService.class);

    /**
     * @see FareCalculatorService
     */
    private static FareCalculatorService fareCalculatorService
            = new FareCalculatorService();

    /**
     * @see InputReaderUtil
     */
    private InputReaderUtil inputReaderUtil;
    /**
     * @see ParkingSpotDAO
     */
    private ParkingSpotDAO parkingSpotDAO;
    /**
     * @see TicketDAO
     */
    private TicketDAO ticketDAO;

    /**
     * Constructor.
     * @param inputReaderUtil user selection
     * @param parkingSpotDAO model parking spot
     * @param ticketDAO model ticket
     */
    public ParkingService(final InputReaderUtil inputReaderUtil,
                          final ParkingSpotDAO parkingSpotDAO,
                          final TicketDAO ticketDAO) {
        this.inputReaderUtil = inputReaderUtil;
        this.parkingSpotDAO = parkingSpotDAO;
        this.ticketDAO = ticketDAO;
    }

    /**
     * Process when vehicle enter.
     */
    public void processIncomingVehicle() throws Exception {
        try {
            ParkingSpot parkingSpot = getNextParkingNumberIfAvailable();

            if (parkingSpot != null && parkingSpot.getId() > 0) {

                String vehicleRegNumber = getVehicleRegNumber();

                parkingSpot.setAvailable(false);

                parkingSpotDAO.updateParking(parkingSpot);
                //allot this parking space and mark it's availability as false

                LocalDateTime inTime = LocalDateTime.now();

                Ticket ticket = new Ticket();
                /*
                ID, PARKING_NUMBER, VEHICLE_REG_NUMBER,
                PRICE, IN_TIME, OUT_TIME)
                ticket.setId(ticketID);
                */
                ticket.setParkingSpot(parkingSpot);
                ticket.setVehicleRegNumber(vehicleRegNumber);
                ticket.setPrice(0);
                ticket.setInTime(inTime);
                ticket.setOutTime(null);
                ticketDAO.saveTicket(ticket);
                logger.info("Generated Ticket and saved in DB");
                logger.info(
                        "Please park your vehicle in spot number:"
                                + parkingSpot.getId());
                logger.info("Recorded in-time for vehicle number:"
                        + vehicleRegNumber + " is:" + inTime);
            }
        } catch (Exception e) {
            logger.error("Unable to process incoming vehicle", e);
            throw e;
        }
    }

    /**
     * Get the vehicle registration number.
     * @return vehicle registration number
     * @throws Exception if vehicle reg number doesn't exist
     */
    private String getVehicleRegNumber() throws Exception {
        logger.info("Please type the vehicle registration number "
                + "and press enter key");
        return inputReaderUtil.readVehicleRegistrationNumber();
    }

    /**
     * Get the next parking number available.
     * @return parking spot
     * @throws Exception if parking is full
     */
    public ParkingSpot getNextParkingNumberIfAvailable() throws Exception {
        int parkingNumber = 0;
        ParkingSpot parkingSpot = null;
        ParkingType parkingType = getVehicleType();

        parkingNumber = parkingSpotDAO.getNextAvailableSlot(parkingType);
        if (parkingNumber > 0) {
            parkingSpot = new ParkingSpot(parkingNumber, parkingType, true);
        } else {
            Exception exception = new Exception("Error "
                    + "fetching parking number from DB."
                    + "Parking slots might be full");
            logger.error("Error fetching next available parking slot",
                    exception);
            throw exception;
        }
        return parkingSpot;
    }

    /**
     * get the vehicle type.
     * @return vehicle type
     */
    private ParkingType getVehicleType() {
        logger.info("Please select vehicle type from menu");
        logger.info("1 CAR");
        logger.info("2 BIKE");
        int input = inputReaderUtil.readSelection();
        switch (input) {
            case 1: {
                return ParkingType.CAR;
            }
            case 2: {
                return ParkingType.BIKE;
            }
            default: {
                logger.info("Incorrect input provided");
                logger.error("Error parsing user input for type of vehicle");
                throw new IllegalArgumentException("Entered input is invalid");
            }
        }
    }

    /**
     * Process when vehicle exit.
     */
    public void processExitingVehicle() throws Exception {
        try {
            String vehicleRegNumber = getVehicleRegNumber();
            Ticket ticket = ticketDAO.getTicket(vehicleRegNumber);
            LocalDateTime outTime = LocalDateTime.now();
            ticket.setOutTime(outTime);
            fareCalculatorService.calculateFare(ticket);
            if (ticketDAO.updateTicket(ticket)) {
                ParkingSpot parkingSpot = ticket.getParkingSpot();
                parkingSpot.setAvailable(true);
                parkingSpotDAO.updateParking(parkingSpot);
                logger.info("Please pay the parking fare:"
                        + ticket.getPrice());
                logger.info("Recorded out-time for vehicle number:"
                        + ticket.getVehicleRegNumber() + " is:" + outTime);
            } else {
                logger.info("Unable to update ticket information."
                        + "Error occurred");
            }
        } catch (Exception e) {
            logger.error("Unable to process exiting vehicle", e);
            throw e;
        }
    }
}
