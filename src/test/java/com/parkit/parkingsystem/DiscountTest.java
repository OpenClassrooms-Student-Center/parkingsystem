package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

public class DiscountTest {

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static DataBasePrepareService dataBasePrepareService;

    private static FareCalculatorService fareCalculatorService;
    private Ticket ticketWithDiscount;
    private Ticket ticketWithoutDiscount;
    public ParkingSpot firstParkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
    public ParkingSpot secondParkingSpot = new ParkingSpot(2, ParkingType.CAR, true);
    public String vehicleRegNumber = "ABCDEF";

    public Date firstInTime;
    public Date firstOutTime;
    public Date secondInTime;
    public Date secondOutTime;

    private static TicketDAO ticketDAO;

    @BeforeAll
    private static void setUp() {
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        fareCalculatorService = new FareCalculatorService();
        dataBasePrepareService = new DataBasePrepareService();
    }

    @BeforeEach
    private void prepareTest() {
        dataBasePrepareService.clearDataBaseEntries();

        secondInTime = new Date();
        secondInTime.setTime( System.currentTimeMillis() - ( 60 * 60 * 1000 ) );
        secondOutTime = new Date();

        ticketWithoutDiscount = new Ticket();
        ticketWithoutDiscount.setParkingSpot(firstParkingSpot);
        ticketWithoutDiscount.setVehicleRegNumber(vehicleRegNumber);
        ticketWithoutDiscount.setDiscountRate(1);

        ticketWithDiscount = new Ticket();
        ticketWithDiscount.setInTime(secondInTime);
        ticketWithDiscount.setOutTime(secondOutTime);
        ticketWithDiscount.setParkingSpot(secondParkingSpot);
        ticketWithDiscount.setVehicleRegNumber(vehicleRegNumber);
    }

    @Test
    public void discountForPaidFirstTicket() {
        firstInTime = new Date();
        firstInTime.setTime( System.currentTimeMillis() - ( 24 * 60 * 60 * 1000 ) );
        firstOutTime = new Date();
        firstOutTime.setTime( System.currentTimeMillis() - ( 24 * 60 * 60 * 1000 ) + ( 60 * 60 * 1000 ) );
        ticketWithoutDiscount.setInTime(firstInTime);
        ticketWithoutDiscount.setOutTime(firstOutTime);
        fareCalculatorService.calculateFare(ticketWithoutDiscount);
        ticketDAO.saveTicket(ticketWithoutDiscount);

        ticketDAO.saveTicket(ticketWithDiscount);
        ticketWithDiscount = ticketDAO.getTicket(vehicleRegNumber);
        fareCalculatorService.calculateFare(ticketWithDiscount);
        assertEquals(((1 * Fare.CAR_RATE_PER_HOUR) * 0.95), ticketWithDiscount.getPrice() );
    }

}
