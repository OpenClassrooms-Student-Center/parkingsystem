package com.parkit.parkingsystem;

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
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
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
	void testNextParkingSpotIsFound() throws SQLException {
		// quand le preparedStatement GET_NEXT_PARKING_SPOT est appelé
		// retourne le resultSet
		// qui renvoie "true" si un emplacement suivant est trouvé
		when(preparedStatement.executeQuery()).thenReturn(resultSet);
		when(resultSet.next()).thenReturn(true);

		int result = parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);

		assertEquals(0, result); // la méthode renvoie -1 si la connection echoue donc 0 si elle fonctionne
	}

	@Test
	void testNextParkingSpotIsFoundforAcar() throws SQLException {
		when(preparedStatement.executeQuery()).thenReturn(resultSet);
		when(resultSet.next()).thenReturn(true);
		when(resultSet.getInt(1)).thenReturn(2);

		int result = parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);

		assertEquals(2, result);
	}

	@Test
	void testUpdateParkingSpotForAcar() throws SQLException {
		parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		when(preparedStatement.executeUpdate()).thenReturn(1, 1);

		boolean result = parkingSpotDAO.updateParking(parkingSpot);

		assertTrue(result);
	}

	@Test
	void testUpdateParkingSpotForAbike() throws SQLException {
		// une moto est garée à l'emplacement 4
		parkingSpot = new ParkingSpot(4, ParkingType.BIKE, false);
		// quand le preparedStatement UPDATE_PARKING_SPOT est appelé
		// dit que le parking est libre (set boolean à 1)
		// pour l'emplacement 4
		when(preparedStatement.executeUpdate()).thenReturn(1, 4);

		boolean result = parkingSpotDAO.updateParking(parkingSpot);

		assertTrue(result);
	}

	@Test
	void testNextParkingSpotIsNotFoundforAcar() throws SQLException {
		when(preparedStatement.executeQuery()).thenReturn(resultSet);
		when(resultSet.next()).thenReturn(false);

		int result = parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);

		assertEquals(-1, result);
	}

	@Test
	void testUpdateFail() throws SQLException {
		parkingSpot = new ParkingSpot(4, ParkingType.BIKE, false);
		when(preparedStatement.executeUpdate()).thenThrow(SQLException.class);

		boolean result = parkingSpotDAO.updateParking(parkingSpot);

		assertFalse(result);
	}
}
