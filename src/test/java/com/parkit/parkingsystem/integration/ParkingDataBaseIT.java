package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import java.util.Date;

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
    ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
    parkingService.processIncomingVehicle();

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
    ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
    parkingService.processIncomingVehicle();

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
    testParkingACar();
    ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
    parkingService.processExitingVehicle();

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
    testParkingABike();
    ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
    parkingService.processExitingVehicle();

    Ticket ticketParkingLotExit = ticketDAO.getTicket("ABCDEF");
    ticketParkingLotExit.setPrice(Fare.BIKE_RATE_PER_HOUR);
    assertNotNull(ticketParkingLotExit);

    ticketParkingLotExit.getOutTime();
    assertThat(ticketParkingLotExit.getOutTime()).isNotNull();
    ticketParkingLotExit.getPrice();
    assertThat(ticketParkingLotExit.getPrice()).isEqualTo(1);

    ticketDAO.updateTicket(ticketParkingLotExit);
  }
  
  //@Test 
  //public void testIsRecurrentCustomer_FALSE() throws Exception {

    /**
     * GIVEN : call ParkingService
     */
    //ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);

    
  //}

}
