package com.parkit.parkingsystem.constants;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.parkit.parkingsystem.constants.DBConstants;

public class DBConstantsTest {

	public static final String RECURRENT = null;

	@Test
	public void getNextParking_shouldRetrieveQueryToGetNextParking() {
		assertEquals(DBConstants.GET_NEXT_PARKING_SPOT,
				"select min(PARKING_NUMBER) from parking where AVAILABLE = true and TYPE = ?");
	}

	@Test
	public void updateParkingSpot_shouldRetrieveQueryToUpdateParkingSpot() {
		assertEquals(DBConstants.UPDATE_PARKING_SPOT, "update parking set available = ? where PARKING_NUMBER = ?");
	}

	@Test
	public void getSaveTicket_shouldRetrieveQueryToSaveTicket() {
		assertEquals(DBConstants.SAVE_TICKET,
				"insert into ticket(PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME) values(?,?,?,?,?)");
	}

	@Test
	public void updateTicket_shouldRetrieveQueryToUpdateTicket() {
		assertEquals(DBConstants.UPDATE_TICKET, "update ticket set PRICE=?, OUT_TIME=? where ID=?");
	}

	@Test
	public void getTicket_shouldRetrieveQueryToGetTicket() {
		assertEquals(DBConstants.GET_TICKET,
				"select t.PARKING_NUMBER, t.ID, t.PRICE, t.IN_TIME, t.OUT_TIME, p.TYPE from ticket t,parking p where p.parking_number = t.parking_number and t.VEHICLE_REG_NUMBER=? order by t.IN_TIME  limit 1");
	}

	@Test
	public void getVehicle_Reg_Number_shouldRetrieveQueryToVehicle_Reg_Number() {
		assertEquals(DBConstants.GET_VEHICLE_REG_NUMBER,
				"select exists (select * from ticket t where t.VEHICLE_REG_NUMBER = ?");
	}

}
