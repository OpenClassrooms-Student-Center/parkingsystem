package com.parkit.parkingsystem;

        import com.parkit.parkingsystem.constants.Fare;
        import com.parkit.parkingsystem.constants.ParkingType;
        import com.parkit.parkingsystem.dao.ParkingSpotDAO;
        import com.parkit.parkingsystem.dao.TicketDAO;
        import com.parkit.parkingsystem.model.ParkingSpot;
        import com.parkit.parkingsystem.model.Ticket;
        import com.parkit.parkingsystem.service.ParkingService;
        import com.parkit.parkingsystem.util.InputReaderUtil;
        import org.junit.jupiter.api.Assertions;
        import org.junit.jupiter.api.BeforeEach;
        import org.junit.jupiter.api.Test;
        import org.junit.jupiter.api.extension.ExtendWith;
        import org.mockito.Mock;
        import org.mockito.Mockito;
        import org.mockito.junit.jupiter.MockitoExtension;
        import org.mockito.stubbing.OngoingStubbing;

        import java.util.Date;

        import static org.junit.jupiter.api.Assertions.assertEquals;
        import static org.junit.jupiter.api.Assertions.assertTrue;
        import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ParkingServiceTest {


    private static ParkingService parkingService;

    @Mock
    private static InputReaderUtil inputReaderUtil;
    @Mock
    private static ParkingSpotDAO parkingSpotDAO;
    @Mock
    private static TicketDAO ticketDAO;
    @Mock
    private static ParkingSpot parkingSpot;
    private static ParkingType parkingType;

    @BeforeEach
    private void setUpPerTest() {
        try {

            when(inputReaderUtil.readSelection()).thenReturn(1);
          lenient().when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");

            ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
            Ticket ticket = new Ticket();
            ticket.setInTime(new Date(System.currentTimeMillis() - (60*60*1000)));
            ticket.setParkingSpot(parkingSpot);
            ticket.setVehicleRegNumber("ABCDEF");
            lenient().when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
            lenient().when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);

            lenient().when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);

            parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        } catch (Exception e) {
            e.printStackTrace();
            throw  new RuntimeException("Failed to set up test mock objects");
        }


    }
    @Test
    public void processIncomingVehicleTest()  {
        parkingService.processIncomingVehicle();
        verify(ticketDAO,times(1));
    }
    @Test
    public void getVehicleTypeTestCar(){
        when(inputReaderUtil.readSelection()).thenReturn(1);
        assertEquals(ParkingType.CAR,parkingService.getVehichleType() );
    }
    @Test
    public void getVehicleTypeTestBike(){
        when(inputReaderUtil.readSelection()).thenReturn(2);
        assertEquals(ParkingType.BIKE,parkingService.getVehichleType());
    }
    @Test
    public void getVehicleTypeTestUnknown(){
        when(inputReaderUtil.readSelection()).thenReturn(3);
        Assertions.assertThrows(IllegalArgumentException.class,() -> parkingService.getVehichleType());
    }


    @Test
    public void processExitingVehicleTest(){
        parkingService.processExitingVehicle();
        verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
    }


}
