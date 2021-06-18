package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseTest {

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingSpotDAO parkingSpotDAO;
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;
    private static FareCalculatorService fareCalculator;

    @Mock
    private static InputReaderUtil inputReaderUtil;

    @BeforeAll
    private static void setUp() throws Exception{
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
        fareCalculator = new FareCalculatorService();
    }

    @BeforeEach
    private void setUpPerTest() throws Exception {
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        dataBasePrepareService.clearDataBaseEntries();
    }

    @AfterAll
    private static void tearDown(){

    }

    @Test
    public void testParkingACar(){
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO, fareCalculator);
        int next = parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);
        parkingService.processIncomingVehicle();
        //TODO: check that a ticket is actualy saved in DB and Parking table is updated with availability
        //etape 1: on recupere le ticket pour le vehicule imatriculé abcdef
        Ticket ticket = ticketDAO.getTicket("ABCDEF");
        //etape 2: on teste que le ticket n'est pas null
        Assertions.assertNotNull(ticket);
        //etape 3: on regarde si le ticket est associé a un parking
        ParkingSpot parkingSpot = ticket.getParkingSpot();
        Assertions.assertNotNull(parkingSpot);
        //etape 4: on regarde si la column availibity est bien a 0
        Assertions.assertFalse(parkingSpot.isAvailable());
        //etape 5 bonus:on verifie que la place disponible est bien celle retourné
        Assertions.assertEquals(next, parkingSpot.getId());
     
        
    }

    @Test
    public void testParkingLotExit(){
    	dataBasePrepareService.clearDataBaseEntries();
        testParkingACar();
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO, fareCalculator);
        parkingService.processExitingVehicle();
        //TODO: check that the fare generated and out time are populated correctly in the database
        //etape 1: on recupere le ticket pour le vehicule imatriculé abcdef
        Ticket ticket = ticketDAO.getTicket("ABCDEF");
        //etape 2: on teste que le ticket n'est pas null
        Assertions.assertNotNull(ticket);
        // verifier que le outime ticket n'est pas null
        Assertions.assertNotNull(ticket.getOutTime());
        // verifier que le prix existe et positif
       Assertions.assertTrue(ticket.getPrice() >= 0);
    }

}
