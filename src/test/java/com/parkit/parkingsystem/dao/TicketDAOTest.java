package com.parkit.parkingsystem.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.jupiter.api.AfterEach;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;


/**
 * <b>TicketDAOIT is built to integration test TicketDAO.</b>
 * 
 * @see Fare
 * @see ParkingType
 * @see TicketDAO
 * @see DataBaseTestConfig
 * @see DataBasePrepareService
 * @see ParkingSpot
 * @see Ticket
 * 
 * @author laetitiadamen
 * @version 1.1
 */

public class TicketDAOTest {
  
  private static TicketDAO ticketDAO;
  private static DataBasePrepareService dataBasePrepareService;

  @BeforeEach
  private void setUpPerTest() throws Exception {
    ticketDAO = new TicketDAO();
    dataBasePrepareService = new DataBasePrepareService();
    dataBasePrepareService.clearDataBaseEntries();
  }

  @AfterEach
  private void closeUpPerTest() throws Exception {
    dataBasePrepareService = new DataBasePrepareService();
    dataBasePrepareService.clearDataBaseEntries();
  }

  @Test
  @DisplayName("Test when saveTicket is called, DB should return info of this ticket")
  public void saveTicket_OK_TEST() {

    /**
     * GIVEN : call ticketDAO.dataBaseConfig and set info of saving ticket
     */
    ticketDAO.dataBaseConfig = new DataBaseTestConfig();

    Ticket ticketInDB = new Ticket();
    ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
    ticketInDB.setParkingSpot(parkingSpot);
    ticketInDB.setVehicleRegNumber("ABCDEF");
    ticketInDB.setPrice(0);
    Date inTime = new Date();
    inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
    ticketInDB.setInTime(inTime);
    Date outTime = new Date();
    ticketInDB.setOutTime(outTime);

    /**
     * WHEN : save ticket
     */
    ticketDAO.saveTicket(ticketInDB);

    /**
     * THEN : verify parking spot is set and ticket info are set in DB
     */
    Ticket getTicketInDB = ticketDAO.getTicket("ABCDEF");

    double priceInDB = 0;

    assertThat(getTicketInDB.getParkingSpot().getId()).isEqualTo(1);
    assertThat(getTicketInDB.getParkingSpot().getParkingType()).isEqualTo(ParkingType.CAR);
    assertThat(getTicketInDB.getParkingSpot().isAvailable()).isEqualTo(false);

    assertThat(getTicketInDB.getVehicleRegNumber()).isEqualTo("ABCDEF");
    assertThat(getTicketInDB.getPrice()).isEqualTo(priceInDB);
    assertThat(getTicketInDB.getInTime()).isNotNull();
    assertThat(getTicketInDB.getOutTime()).isNotNull();

  }

  @Test
  @DisplayName("Error fetching next slot")
  public void saveTicket_KO_TEST() {

    /**
     * GIVEN : call ticketDAO.dataBaseConfig and set info of saving ticket with
     * wrong Spot for Parking type a car
     */
    ticketDAO.dataBaseConfig = new DataBaseTestConfig();

    Ticket ticketInDB = new Ticket();
    ParkingSpot parkingSpot = new ParkingSpot(4, ParkingType.CAR, false);
    ticketInDB.setParkingSpot(parkingSpot);
    ticketInDB.setVehicleRegNumber("ABCDEF");
    ticketInDB.setPrice(0);
    Date inTime = new Date();
    inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
    ticketInDB.setInTime(inTime);
    Date outTime = new Date();
    ticketInDB.setOutTime(outTime);

    /**
     * WHEN : save ticket
     */
    boolean isSaved = ticketDAO.saveTicket(ticketInDB);

    /**
     * THEN : verify parking spot is not set and ticket info are not set in DB
     */
    assertThat(isSaved).isFalse();
    assertThat(ticketInDB.getId()).isEqualTo(0);
  }

  @Test
  @DisplayName("Test when updateTicket is called, DB should return info of this ticket updated")
  public void updateTicket_TEST() throws InterruptedException {

    /**
     * GIVEN : call ticketDAO.dataBaseConfig and set info of saving ticket. Wait one
     * second and set new out time and price
     */
    ticketDAO.dataBaseConfig = new DataBaseTestConfig();

    Ticket ticketInDB = new Ticket();
    ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
    ticketInDB.setParkingSpot(parkingSpot);
    ticketInDB.setVehicleRegNumber("ABCDEF");

    Date inTime = new Date();
    inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
    ticketInDB.setInTime(inTime);
    Date outTime = new Date();
    ticketInDB.setOutTime(outTime);
    ticketInDB.setPrice(Fare.CAR_RATE_PER_HOUR);
    ticketDAO.saveTicket(ticketInDB);
    Thread.sleep(1000);
    ticketInDB.setOutTime(new Date());
    ticketInDB.setPrice(0);

    /**
     * WHEN : update ticket
     */
    boolean ticketUpdated = ticketDAO.updateTicket(ticketInDB);

    /**
     * THEN : verify ticket is update in DB
     */
    assertThat(ticketInDB.getPrice()).isEqualTo(0);
    assertThat(ticketInDB.getOutTime()).isNotNull();
    assertTrue(ticketUpdated);

  }

  @Test
  @DisplayName("Test when a user own two ticket in DB should return recurrent customer")
  public void recurentCustomer_TEST() {

    /**
     * GIVEN : call ticketDAO.dataBaseConfig and set info of saving 2 tickets.
     */
    ticketDAO.dataBaseConfig = new DataBaseTestConfig();

    Ticket ticketInDB_firstPark = new Ticket();
    ParkingSpot parkingSpot_firstPark = new ParkingSpot(1, ParkingType.CAR, false);
    ticketInDB_firstPark.setParkingSpot(parkingSpot_firstPark);
    ticketInDB_firstPark.setVehicleRegNumber("ABCDEF");
    ticketInDB_firstPark.setPrice(0);
    Date inTime_firstPark = new Date();
    inTime_firstPark.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
    ticketInDB_firstPark.setInTime(inTime_firstPark);
    Date outTime_firstPark = new Date();
    ticketInDB_firstPark.setOutTime(outTime_firstPark);
    ticketDAO.saveTicket(ticketInDB_firstPark);

    Ticket ticketInDB_secondPark = new Ticket();
    ParkingSpot parkingSpot_secondPark = new ParkingSpot(1, ParkingType.CAR, false);
    ticketInDB_secondPark.setParkingSpot(parkingSpot_secondPark);
    ticketInDB_secondPark.setVehicleRegNumber("ABCDEF");
    ticketInDB_secondPark.setPrice(0);
    Date inTime = new Date();
    inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
    ticketInDB_secondPark.setInTime(inTime);
    Date outTime_secondPark = new Date();
    ticketInDB_secondPark.setOutTime(outTime_secondPark);
    ticketDAO.saveTicket(ticketInDB_secondPark);

    /**
     * WHEN : recurrentCustomer
     */
    boolean recurrent_Customer = ticketDAO.recurrentCustomer("ABCDEF");

    /**
     * THEN : verify that it is recurrent customer
     */
    assertThat(recurrent_Customer).isTrue();
  }
}

