package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Calendar;

import static org.assertj.core.api.BDDAssertions.within;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingSpotDAO parkingSpotDAO;
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;

    @Mock
    private static InputReaderUtil inputReaderUtil;

    private ParkingService parkingService;

    private final String registrationTested = "ABCDEF";

    @BeforeAll
    public static void setUp() throws Exception {
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
        ticketDAO.parkingSpotDAO = parkingSpotDAO;
    }

    @BeforeEach
    public void setUpPerTest() throws Exception {
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn(registrationTested);
        dataBasePrepareService.clearDataBaseEntries();
        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
    }

    @AfterAll
    public static void tearDown() {
        // Nettoyage final si nécessaire
    }

    /**
     * Test d'intégration pour l'entrée d'une voiture dans le parking.
     * Vérifie que le ticket est créé, que le numéro d'immatriculation est correct,
     * que l'heure d'entrée est enregistrée et que la place de parking est occupée.
     */
    @Test
    public void testParkingACar() {
        parkingService.processIncomingVehicle();

        Ticket ticket = ticketDAO.getTicket(registrationTested);

        assertThat(ticket).isNotNull();
        assertThat(registrationTested).isEqualTo(ticket.getVehicleRegNumber());
        assertThat(ticket.getInTime()).isNotNull();
        assertThat(ticket.getParkingSpot().isAvailable()).isFalse();
    }

    /**
     * Test d'intégration pour l'entrée d'un vélo dans le parking.
     * Similaire au test voiture, mais avec un type de véhicule différent (vélo).
     */
    @Test
    public void testParkingABike() {
        when(inputReaderUtil.readSelection()).thenReturn(2);  // Sélection de type vélo

        parkingService.processIncomingVehicle();

        Ticket ticket = ticketDAO.getTicket(registrationTested);

        assertThat(ticket).isNotNull();
        assertThat(registrationTested).isEqualTo(ticket.getVehicleRegNumber());
        assertThat(ticket.getInTime()).isNotNull();
        assertThat(ticket.getParkingSpot().isAvailable()).isFalse();
    }

    /**
     * Test d'intégration pour la sortie d'une voiture du parking après une durée donnée.
     * Vérifie que le ticket est mis à jour avec l'heure de sortie et que la place de parking devient disponible.
     */
    @Test
    public void testParkingLotExit() {
        parkingService.processIncomingVehicle();
        Ticket getInTicket = ticketDAO.getTicket(registrationTested);

        // Simule une sortie après 3 heures
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getInTicket.getInTime());
        calendar.add(Calendar.HOUR_OF_DAY, 3);

        parkingService.processExitingVehicle(calendar.getTime());

        Ticket getOutTicket = ticketDAO.getTicket(registrationTested);
        assertThat(getOutTicket).isNotNull();
        assertThat(getOutTicket.getOutTime()).isNotNull();
        assertThat(getOutTicket.getPrice()).isGreaterThan(0);
        assertThat(getOutTicket.getParkingSpot().isAvailable()).isTrue();
    }

    /**
     * Test d'intégration pour la sortie d'un utilisateur récurrent avec réduction.
     * Vérifie que l'utilisateur récurrent bénéficie d'une réduction de 5 % sur le tarif.
     */
    @Test
    public void testParkingLotExitRecurringUser() {
        // Entrée et sortie pour marquer l'utilisateur comme récurrent
        parkingService.processIncomingVehicle();
        parkingService.processExitingVehicle();

        // Nouvelle entrée du même utilisateur
        parkingService.processIncomingVehicle();
        Ticket getInTicket = ticketDAO.getTicket(registrationTested);

        // Simule une sortie après 3 heures
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getInTicket.getInTime());
        calendar.add(Calendar.HOUR_OF_DAY, 3);

        parkingService.processExitingVehicle(calendar.getTime());

        Ticket getOutTicket = ticketDAO.getTicket(registrationTested);
        double expectedPriceWithDiscount = (3 * Fare.CAR_RATE_PER_HOUR) * 0.95; // 5 % de réduction

        assertThat(getOutTicket.getPrice()).isCloseTo(expectedPriceWithDiscount, within(0.01));
    }
}
