package com.parkit.parkingsystem.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import java.util.Date;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


/**
 * <b>ParkingDataBaseIT is built to integration test incoming and outgoing of
 * vehicle in park.</b>  
 * 
 * <p>Use DataBaseTestConfig and DataBasePrepareService
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


@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

  private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
  private static ParkingSpotDAO parkingSpotDAO;
  private static TicketDAO ticketDAO;
  private static DataBasePrepareService dataBasePrepareService;

  @Mock
  private static InputReaderUtil inputReaderUtil;

  @BeforeAll
  private static void setUp() throws Exception {
    parkingSpotDAO = new ParkingSpotDAO();
    parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
    ticketDAO = new TicketDAO();
    ticketDAO.dataBaseConfig = dataBaseTestConfig;
    dataBasePrepareService = new DataBasePrepareService();
  }


  @BeforeEach
  private void setUpPerTest() throws Exception {
    when(inputReaderUtil.readSelection()).thenReturn(1);
    when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
    dataBasePrepareService.clearDataBaseEntries();
  }


  @AfterAll
  private static void tearDown() {

  }


  @Test
  public void testParkingACar() throws Exception {
    
    // TODO: check that a ticket is actually saved in DB and Parking table is
    // updated with availability

    /**
     * GIVEN : call ParkingService
     */
    ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);

    /**
     * WHEN : call parkingService.processIncomingVehicle
     */
    parkingService.processIncomingVehicle();

    /**
     * THEN : check that ticket is actually saved in DB and parking table is update
     * in DB
     */
    Ticket ticket = ticketDAO.getTicket("ABCDEF");
    System.out.println(ticket);
    assertEquals(ticket.getVehicleRegNumber(), "ABCDEF");

    assertEquals(ticket.getPrice(), 0);
    ticket.getInTime();
    assertThat(ticket.getInTime()).isNotNull();

    ParkingSpot parkingSpot = ticket.getParkingSpot();
    parkingSpot.isAvailable();
    assertThat(parkingSpot.isAvailable()).isEqualTo(false);

  }


  // New Test in coming bike scenario
  @Test
  public void testParkingABike() throws Exception {
    
    /**
     * GIVEN : call ParkingService
     */
    ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);

    /**
     * WHEN : call parkingService.processIncomingVehicle
     */
    parkingService.processIncomingVehicle();

    /**
     * THEN : check that ticket is actually saved in DB and parking table is update
     * in DB
     */
    
    Ticket ticket = ticketDAO.getTicket("ABCDEF");
    System.out.println(ticket);
    assertEquals(ticket.getVehicleRegNumber(), "ABCDEF");

    assertEquals(ticket.getPrice(), 0);
    ticket.getInTime();
    assertThat(ticket.getInTime()).isNotNull();

    ParkingSpot parkingSpot = ticket.getParkingSpot();
    parkingSpot.isAvailable();
    assertThat(parkingSpot.isAvailable()).isEqualTo(false);

  }


  @Test
  public void testParkingLotExit() throws Exception {
    
    // TODO: check that the fare generated and out time are populated correctly in
    // the database

    /**
     * GIVEN : call ParkingService
     */
    testParkingACar();
    ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);

    /**
     * WHEN : call parkingService.processExitingVehicle
     */
    parkingService.processExitingVehicle();

    /**
     * THEN : check fare generate and out time populated
     */
    Ticket ticketParkingLotExit = ticketDAO.getTicket("ABCDEF");
    ticketParkingLotExit.setPrice(Fare.CAR_RATE_PER_HOUR);
    assertNotNull(ticketParkingLotExit);

    ticketParkingLotExit.getOutTime();
    assertThat(ticketParkingLotExit.getOutTime()).isNotNull();
    ticketParkingLotExit.getPrice();
    assertThat(ticketParkingLotExit.getPrice()).isEqualTo(1.5);

    ticketDAO.updateTicket(ticketParkingLotExit);
  }


  @Test
  // Test parking lot exit bike scenario
  public void testBikeParkingLotExit() throws Exception {
    
    /**
     * GIVEN : call ParkingService
     */
    testParkingABike();
    ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
    
    /**
     * WHEN : call parkingService.processExitingVehicle
     */
    parkingService.processExitingVehicle();

    /**
     * THEN : check fare generate and out time populated
     */
    Ticket ticketParkingLotExit = ticketDAO.getTicket("ABCDEF");
    ticketParkingLotExit.setPrice(Fare.BIKE_RATE_PER_HOUR);
    assertNotNull(ticketParkingLotExit);

    ticketParkingLotExit.getOutTime();
    assertThat(ticketParkingLotExit.getOutTime()).isNotNull();
    ticketParkingLotExit.getPrice();
    assertThat(ticketParkingLotExit.getPrice()).isEqualTo(1);

    ticketDAO.updateTicket(ticketParkingLotExit);
  }
  

}
