package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

/**
 * @author Nicolas BIANCUCCI this class contains ParkingDataBase integration tests
 */
@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

    private static final String VehicleRegNumber = "ABCDEF";

    private final static DataBaseTestConfig     dataBaseTestConfig = new DataBaseTestConfig();
    private static DataBasePrepareService dataBasePrepareService;
    @Spy
    private static ParkingSpotDAO         parkingSpotDAO;
    @Spy
    private static TicketDAO              ticketDAO;
    @Mock
    private static InputReaderUtil        inputReaderUtil;
    private        ParkingService         parkingService;

    @BeforeAll
    private static void setUp() throws Exception {

        parkingSpotDAO                = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        ticketDAO                     = new TicketDAO(dataBaseTestConfig);
        ticketDAO.dataBaseConfig      = dataBaseTestConfig;
        dataBasePrepareService        = new DataBasePrepareService();
    }

    @AfterAll
    private static void tearDown() {

    }

    @BeforeEach
    private void setUpPerTest() throws Exception {

        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn(VehicleRegNumber);
        dataBasePrepareService.clearDataBaseEntries();
    }

    @DisplayName("Parking system save ticket to DB and Update parkingSpot with availability")
    @Test
    public void testParkingACar() {

        // GIVEN:
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        // WHEN:
        parkingService.processIncomingVehicle();
        // THEN:
        Mockito.verify(ticketDAO).saveTicket(Mockito.any(Ticket.class));
        Mockito.verify(parkingSpotDAO).updateParking(Mockito.any(ParkingSpot.class));
        assertEquals(2, parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR));

    }

    @DisplayName("Parking system generated fare and out tim saving to DB")
    @Test
    public void testParkingLotExit() throws InterruptedException {

        // GIVEN:
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processIncomingVehicle();
        Thread.sleep(500);
        // WHEN:
        parkingService.processExitingVehicle();

        // THEN:
        Ticket ticket                    = ticketDAO.getTicket(VehicleRegNumber);
        int    numberOfNextAvailableSlot = parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);

        assertEquals(0, ticket.getPrice());
        assertNotNull(ticket.getOutTime());
        assertEquals(1, numberOfNextAvailableSlot);
    }
}
//