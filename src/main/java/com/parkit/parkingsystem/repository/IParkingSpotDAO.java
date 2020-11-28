package com.parkit.parkingsystem.repository;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;

public interface IParkingSpotDAO {
    int getNextAvailableSlot(ParkingType parkingType);
    boolean updateParking(ParkingSpot parkingSpot);

}
