package com.parkit.parkingsystem;

import com.parkit.parkingsystem.service.InteractiveShell;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * <b>App is main class of the project. It is built to run the Park'it interface in console.</b>
 * 
 * @author laetitiadamen
 * @version 1.1
 */

public class App {
  private static final Logger logger = LogManager.getLogger("App");

  /**
   * The main method of this project. Call InteractiveShell's method :loadInterface It will open
   * App's interface in console with customers choices.
   * 
   * @see InteractiveShell
   * @param args array of string arguments
   * @since 1.0
   */

  public static void main(String args[]) {
    logger.info("Initializing Parking System");
    InteractiveShell.loadInterface();
  }
}
