package com.parkit.parkingsystem;

import com.parkit.parkingsystem.service.InteractiveShell;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**.
 * class App
 */
@SuppressWarnings("checkstyle:JavadocStyle")
public class App {

    /**.
     * parameters of Logger
     */
    private static final Logger LOGGER = LogManager.getLogger("App");
    private static void main(final String[] args) {
        LOGGER.info("Initializing Parking System");
        InteractiveShell.loadInterface();
    }
}
