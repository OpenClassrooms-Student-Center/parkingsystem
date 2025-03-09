package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class ParkingSpotDAOTest {

    private ParkingSpotDAO parkingSpotDAO;

    @BeforeEach
    void setUp() {
        parkingSpotDAO = new ParkingSpotDAO();
    }

    @Test
    void testGetNextAvailableSlot() {
        int slot = parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);
        assertTrue(slot > 0, "Le slot doit être supérieur à 0");
    }

    @Test
    void testUpdateParking() {
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        boolean result = parkingSpotDAO.updateParking(parkingSpot);
        assertTrue(result, "L'update doit réussir");
    }
    @Test
    void testUpdateParking_Failure() {
        ParkingSpotDAO parkingSpotDAO = new ParkingSpotDAO();
        ParkingSpot parkingSpot = new ParkingSpot(-1, ParkingType.CAR, false); // ID invalide

        boolean result = parkingSpotDAO.updateParking(parkingSpot);

        assertFalse(result, "La mise à jour doit échouer si l'ID est invalide.");
    }



}
