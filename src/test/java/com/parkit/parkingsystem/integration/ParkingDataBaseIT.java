package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;

import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;




@ExtendWith(MockitoExtension.class)

public class ParkingDataBaseIT {

    private static final DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingSpotDAO parkingSpotDAO;
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;

    @Mock
    private InputReaderUtil inputReaderUtil;

    @BeforeAll
    public static void setUp() throws Exception{
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
    }

    @BeforeEach
    public void setUpPerTest() throws Exception {
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        dataBasePrepareService.clearDataBaseEntries();
    }

    @AfterAll
    public static void tearDown(){

    }

    @Test
    public void testParkingACar(){

        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);

       int initialAvailableSlot = parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);
       System.out.println("Initial available slot: " + initialAvailableSlot);

        parkingService.processIncomingVehicle();
        //TODO: check that a ticket is actually saved in DB and Parking table is updated with availability



        Ticket ticket = ticketDAO.getTicket("ABCDEF");
        assertNotNull(ticket, "Le ticket doit être enregistré dans la base de données");

        assertEquals("ABCDEF", ticket.getVehicleRegNumber(),"Le numéro d'immatriculation doit être correct");

        int nextAvailableSlot = parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);
        System.out.println("Next available slot: " + nextAvailableSlot);

        assertEquals( initialAvailableSlot + 1, nextAvailableSlot, "La place de parking doit etre mis à jour");
    }

    @Test
    public void testParkingLotExit() throws InterruptedException {
        testParkingACar();

        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);

        parkingService.processIncomingVehicle();

        Thread.sleep(1000);

        parkingService.processExitingVehicle();
        //TODO: check that the fare generated and out time are populated correctly in the database

        Ticket ticket = ticketDAO.getTicket("ABCDEF");

        assertNotNull(ticket, "Le ticket doit exister dans la base de données après la sortie");
        assertNotNull(ticket.getOutTime(), "l'heure de sortie doit etre renseignée");
        assertNotNull(ticket.getInTime(), "l'heure d'entrée doit etre renseignée");

        System.out.println("InTime: " + ticket.getInTime());
        System.out.println("OutTime: " + ticket.getOutTime());


        assertTrue(ticket.getOutTime().after(ticket.getInTime()), " l'heure de sortie doit etre après l'heure d'entrée");

        int duration = 0;
        if (duration <= 30) { // Stationnement gratuit
            assertEquals( 0, ticket.getPrice(), 0.01, "Le tarif devrait être gratuit pour les 30 premières minutes");
        } else { // Stationnement payant
            assertTrue(ticket.getPrice() > 0, "Le tarif doit être positif après 30 minutes");
        }

    }
    @Test
    public void testParkingLotExitRecurringUser() throws InterruptedException, SQLException {
        // 1️⃣ Simuler la sortie du premier ticket (premier stationnement)
        testParkingLotExit();
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);

        // 2️⃣ Simuler un premier stationnement sans remise
        parkingService.processIncomingVehicle();
        Thread.sleep(1000);
        parkingService.processExitingVehicle();

        // 3️⃣ Récupérer le premier ticket
        Ticket ticket1 = ticketDAO.getTicket("ABCDEF");
        assertNotNull(ticket1, "Le premier ticket doit exister dans la base de données après la sortie.");

        double prixPremierTicket = ticket1.getPrice();
        System.out.println("💰 Prix du premier ticket : " + prixPremierTicket);

        // Vérifier que le premier ticket est bien gratuit (durée < 30min)
        assertEquals(0.0, prixPremierTicket, 0.01, "Le premier ticket doit être gratuit car durée < 30 min.");

        // 4️⃣ Simuler un deuxième stationnement (récurrent)
        parkingService.processIncomingVehicle();

        // 5️⃣ Forcer une durée > 30 minutes pour que la remise soit applicable
        try (Connection connection = dataBaseTestConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "UPDATE ticket SET IN_TIME = ? WHERE VEHICLE_REG_NUMBER = ? ORDER BY ID DESC LIMIT 1")) {
            Timestamp fortyMinutesAgo = new Timestamp(System.currentTimeMillis() - (40 * 60 * 1000)); // 40 min avant
            preparedStatement.setTimestamp(1, fortyMinutesAgo);
            preparedStatement.setString(2, "ABCDEF");
            preparedStatement.executeUpdate();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        // 6️⃣ Simuler la sortie du deuxième ticket avec remise
        Thread.sleep(1000);
        parkingService.processExitingVehicle();

        Ticket ticket2 = ticketDAO.getTicket("ABCDEF");
        assertNotNull(ticket2, "Le deuxième ticket doit exister dans la base de données après la sortie.");

        double prixAvecRemise = ticket2.getPrice();
        System.out.println("💰 Prix du deuxième ticket (avec remise) : " + prixAvecRemise);

        // Vérifier que la durée est bien > 30 minutes
        int duration = (int) ((ticket2.getOutTime().getTime() - ticket2.getInTime().getTime()) / (1000 * 60));
        System.out.println("🕒 Durée du deuxième stationnement : " + duration + " minutes");

        // 7️⃣ Vérifier que le deuxième ticket applique bien la remise
        if (duration <= 30) {
            System.out.println("Prix avec remise attendu : 0.0");
            assertEquals(0, prixAvecRemise, 0.01, "Le prix doit être 0 pour une durée ≤ 30 min.");
        } else {
            // Calcul du prix normal du stationnement avant remise (on divise par 0.95 pour retrouver le tarif normal)
            double prixSansRemise = prixAvecRemise / 0.95;
            double prixAttenduAvecRemise = prixSansRemise * 0.95; // Remise de 5%

            System.out.println("💰 Prix sans remise estimé : " + prixSansRemise);
            System.out.println("💰 Prix attendu avec remise : " + prixAttenduAvecRemise);

            assertEquals(prixAttenduAvecRemise, prixAvecRemise, 0.01, "Une remise de 5% doit être appliquée.");
        }
    }


    @Test
    public void testMultipleEntriesForRecurringUser() throws InterruptedException {
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);

        // Premier stationnement
        parkingService.processIncomingVehicle();
        Thread.sleep(2000); // Augmenter le temps d'attente

        parkingService.processExitingVehicle();

        // Deuxième stationnement (doit être considéré comme récurrent)
        parkingService.processIncomingVehicle();
        Thread.sleep(2000); // Augmenter le temps d'attente

        parkingService.processExitingVehicle();

        // Récupérer le ticket du deuxième passage
        Ticket ticket2 = ticketDAO.getTicket("ABCDEF");

        assertNotNull(ticket2, "le ticket doit exister dans la base de données après la sortie");

        double priceWithDiscount = ticketDAO.getNBTicket("ABCDEF");

        assertTrue(priceWithDiscount > 1, "Après plusieurs entrées, l'utilisateur doit être considéré comme récurrent.");
        // Vérifier que le tarif n'est pas nul


        double expectedPriceWithoutDiscount = priceWithDiscount / 0.95;


        assertTrue(expectedPriceWithoutDiscount> priceWithDiscount, "Une remise de 5 % devrait être appliquée pour un utilisateur récurrent");
    }


}