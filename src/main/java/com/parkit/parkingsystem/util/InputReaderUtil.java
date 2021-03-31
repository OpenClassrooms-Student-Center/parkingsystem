package com.parkit.parkingsystem.util;

import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**.
 * reads both int and String input provided by a user
 *
 * @author NICO
 */

public class InputReaderUtil {

    /**.
     * InputReaderUtil logger
     */
    private static final Logger LOGGER = LogManager.getLogger("InputReaderUtil");
    /**.
     * allows users provide their choices to ParkingSystem
     */
    private static final Scanner SCAN = new Scanner(System.in, "UTF-8");

    /**.
     * Read the selections
     * @return input by the selections
     */
    public int readSelection() {
        try {
            int input = Integer.parseInt(SCAN.nextLine());
            return input;
        } catch (Exception e) {
            LOGGER.error("Error while reading user input from Shell", e);
            System.out.println("Error reading input. Please enter valid number for proceeding further");
            return -1;
        }
    }

    /**.
     * Read the number registration vehicle
     * @return the number registration vehicle
     * @throws Exception
     */
    public String readVehicleRegistrationNumber() throws Exception {
        try {
            String vehicleRegNumber = SCAN.nextLine();
            if (vehicleRegNumber == null || vehicleRegNumber.trim().length() == 0) {
                throw new IllegalArgumentException("Invalid input provided");
            }
            return vehicleRegNumber;
        } catch (Exception e) {
            LOGGER.error("Error while reading user input from Shell", e);
            System.out.println("Error reading input. Please enter a valid string for vehicle registration number");
            throw e;
        }
    }

}
