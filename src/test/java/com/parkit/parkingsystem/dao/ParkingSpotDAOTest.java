package com.parkit.parkingsystem.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.model.ParkingSpot;

@ExtendWith(MockitoExtension.class)
class ParkingSpotDAOTest {

	@Mock
	private static DataBaseTestConfig dataBaseTestConfig;
	@Mock
	private Connection con;
	@Mock
	private PreparedStatement preparedStatement;
	@Mock
	private ResultSet resultSet;

	ParkingSpot parkingSpot;
	private static ParkingSpotDAO parkingSpotDAO;

	@BeforeEach
	private void setUp() throws Exception {

		parkingSpotDAO = new ParkingSpotDAO();
		parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;

		when(dataBaseTestConfig.getConnection()).thenReturn(con);
		when(con.prepareStatement(anyString())).thenReturn(preparedStatement);
	}

	@Test
	void nextParkingSpot_shouldIsFound() throws SQLException {
		// quand le preparedStatement GET_NEXT_PARKING_SPOT est appelé
		// retourne le resultSet
		// qui renvoie "true" si un emplacement suivant est trouvé
		// WHEN
		when(preparedStatement.executeQuery()).thenReturn(resultSet);
		when(resultSet.next()).thenReturn(true);
		int result = parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);

		// THEN
		assertEquals(0, result); // la méthode renvoie -1 si la connection echoue donc 0 si elle fonctionne
	}

	@Test
	void nextParkingSpot_shouldIsFoundforAcar() throws SQLException {

		// WHEN
		when(preparedStatement.executeQuery()).thenReturn(resultSet);
		when(resultSet.next()).thenReturn(true);
		when(resultSet.getInt(1)).thenReturn(2);
		int result = parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);

		// THEN
		assertEquals(2, result);
	}

	@Test
	void updateParkingSpot_shouldForAcar() throws SQLException {

		// GIVEN
		parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		// WHEN
		when(preparedStatement.executeUpdate()).thenReturn(1, 1);
		boolean result = parkingSpotDAO.updateParking(parkingSpot);

		// THEN
		assertTrue(result);
	}

	@Test
	void updateParkingSpot_shouldForAbike() throws SQLException {
		// une moto est garée à l'emplacement 4
		// GIVEN
		parkingSpot = new ParkingSpot(4, ParkingType.BIKE, false);
		// quand le preparedStatement UPDATE_PARKING_SPOT est appelé
		// dit que le parking est libre (set boolean à 1)
		// pour l'emplacement 4
		// WHEN
		when(preparedStatement.executeUpdate()).thenReturn(1, 4);
		boolean result = parkingSpotDAO.updateParking(parkingSpot);

		// THEN
		assertTrue(result);
	}

	@Test
	void nextParkingSpot_shouldIsNotFoundforAcar() throws SQLException {

		// WHEN
		when(preparedStatement.executeQuery()).thenReturn(resultSet);
		when(resultSet.next()).thenReturn(false);
		int result = parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);

		// THEN
		assertEquals(-1, result);
	}

	@Test
	void update_shouldFail() throws SQLException {

		// GIVEN
		parkingSpot = new ParkingSpot(4, ParkingType.BIKE, false);

		// WHEN
		when(preparedStatement.executeUpdate()).thenThrow(SQLException.class);
		boolean result = parkingSpotDAO.updateParking(parkingSpot);

		// THEN
		assertFalse(result);
	}
}
