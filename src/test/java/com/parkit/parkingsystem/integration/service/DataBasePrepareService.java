package com.parkit.parkingsystem.integration.service;

import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import java.sql.Connection;

/**
 * <b>DataBasePrepareService is built to clear data base entries for virgin test.</b>
 * 
 * @see Fare
 * @see ParkingSpotDAO
 * @see TicketDAO
 * @see DataBaseTestConfig
 * @see DataBasePrepareService
 * @see ParkingSpot
 * @see Ticket
 * @see ParkingService
 * @see InputReaderUtil
 * 
 * @author laetitiadamen
 * @version 1.1
 */
public class DataBasePrepareService {

  DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();

  public void clearDataBaseEntries() {
    Connection connection = null;
    try {
      connection = dataBaseTestConfig.getConnection();

      // set parking entries to available
      connection.prepareStatement("update parking set available = true").execute();

      // clear ticket entries;
      connection.prepareStatement("truncate table ticket").execute();

    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      dataBaseTestConfig.closeConnection(connection);
    }
  }

}
