package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * TicketDAO class is to built to stock ticket to the database.
 * 
 * <p>
 * DAO means "Data Access Object". It use to separate how we stock data with the main code. So if
 * stocking Data method change, only functional class will have be to changed.
 * 
 * @see DataBaseConfig
 * @see DBConstants
 * @see ParkingType
 * @see ParkingSpot
 * @see Ticket
 * 
 * @author laetitiadamen
 * @version 1.1
 */
public class TicketDAO {

  /**
   * Call Logger.
   * 
   * @param logger Logger's name is "TicketDAO"
   * @since 1.0
   */
  private static final Logger logger = LogManager.getLogger("TicketDAO");

  /**
   * Call class DataBaseConfig.
   * 
   * @since 1.0
   */
  public DataBaseConfig dataBaseConfig = new DataBaseConfig();

  /**
   * saveTicket() method is built to save ticket in the database. In a try/catch/finally
   * instruction, it will :
   * <ul>
   * <li>getConnection from Database in MySQL</li>
   * <li>use prepareStatement of saving a ticket</li>
   * <li>run query</li>
   * <li>close connection</li>
   * </ul>
   * 
   * @param ticket Ticket
   * @return false
   */
  public boolean saveTicket(Ticket ticket) {
    Connection con = null;
    try {
      con = dataBaseConfig.getConnection();
      PreparedStatement ps = con.prepareStatement(DBConstants.SAVE_TICKET);
      // ID, PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME)
      // ps.setInt(1,ticket.getId());
      ps.setInt(1, ticket.getParkingSpot().getId());
      ps.setString(2, ticket.getVehicleRegNumber());
      ps.setDouble(3, ticket.getPrice());
      ps.setTimestamp(4, new Timestamp(ticket.getInTime().getTime()));
      ps.setTimestamp(5,
          (ticket.getOutTime() == null) ? null : (new Timestamp(ticket.getOutTime().getTime())));
      return ps.execute();
    } catch (Exception ex) {
      logger.error("Error fetching next available slot", ex);
    } finally {
      dataBaseConfig.closeConnection(con);
      return false;
    }
  }


  /**
   * getTicket() method is built to get in Database.
   * <ul>
   * <li>getConnection from Database in MySQL</li>
   * <li>use prepareStatement of getting a ticket with the vehicle's Id (regNumber)</li>
   * <li>if exist, run query</li>
   * <li>close ResultSet</li>
   * <li>close PreparedStatement</li>
   * <li>return ticket</li>
   * <li>close connection</li>
   * </ul>
   * 
   * @param vehicleRegNumber register number of the vehicle
   * @return ticket
   */
  public Ticket getTicket(String vehicleRegNumber) {
    Connection con = null;
    Ticket ticket = null;
    try {
      con = dataBaseConfig.getConnection();
      PreparedStatement ps = con.prepareStatement(DBConstants.GET_TICKET);
      // ID, PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME)
      ps.setString(1, vehicleRegNumber);
      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        ticket = new Ticket();
        ParkingSpot parkingSpot =
            new ParkingSpot(rs.getInt(1), ParkingType.valueOf(rs.getString(6)), false);
        ticket.setParkingSpot(parkingSpot);
        ticket.setId(rs.getInt(2));
        ticket.setVehicleRegNumber(vehicleRegNumber);
        ticket.setPrice(rs.getDouble(3));
        ticket.setInTime(rs.getTimestamp(4));
        ticket.setOutTime(rs.getTimestamp(5));
      }
      dataBaseConfig.closeResultSet(rs);
      dataBaseConfig.closePreparedStatement(ps);
    } catch (Exception ex) {
      logger.error("Error fetching next available slot", ex);
    } finally {
      dataBaseConfig.closeConnection(con);
      return ticket;
    }
  }


  /**
   * updateTicket() is built to update ticket in DataBase.
   * 
   * In a try/catch/finally instruction, it will :
   * <ul>
   * <li>getConnection from Database in MySQL</li>
   * <li>use prepareStatement of updating ticket</li>
   * <li>run query</li>
   * <li>close connection</li>
   * </ul>
   * 
   * @param ticket Ticket
   * @return false
   * @since 1.0
   */
  public boolean updateTicket(Ticket ticket) {
    Connection con = null;
    try {
      con = dataBaseConfig.getConnection();
      PreparedStatement ps = con.prepareStatement(DBConstants.UPDATE_TICKET);
      ps.setDouble(1, ticket.getPrice());
      ps.setTimestamp(2, new Timestamp(ticket.getOutTime().getTime()));
      ps.setInt(3, ticket.getId());
      ps.execute();
      return true;
    } catch (Exception ex) {
      logger.error("Error saving ticket info", ex);
    } finally {
      dataBaseConfig.closeConnection(con);
    }
    return false;
  }

  public boolean recurrentCustomer(String vehicleRegNumber) {
    Connection con = null;
    boolean result = false;
    ResultSet rs = null;
    int count = 0; 
    try {
      con = dataBaseConfig.getConnection();
      PreparedStatement ps = con.prepareStatement(DBConstants.RECURRENTCUSTOMER);      
      ps.setString(1, vehicleRegNumber);      
      rs = ps.executeQuery();
      while (rs.next()) {
        rs.getInt(1);
        count = rs.getInt(1);
      }
      if (count >= 1) {
        result = true; 
      }
    } catch (Exception ex) {
      logger.error("Error fetching customer", ex);
    } finally {
      dataBaseConfig.closeConnection(con);
      return result;
    }
  }
}
