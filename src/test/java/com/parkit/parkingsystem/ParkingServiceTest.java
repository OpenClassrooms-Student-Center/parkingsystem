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

    private static ParkingType parkingType;

    @Mock
    private static InputReaderUtil inputReaderUtil;
    @Mock
    private static ParkingSpotDAO parkingSpotDAO;
    @Mock
    private static TicketDAO ticketDAO;



    @Test
    public void processIncomingVehicleTestingIfParkingSpotisAvailable() {
        Ticket ticket = new Ticket();
        try {
            parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
             ParkingSpot parkingSpot = mock( ParkingSpot.class);
             ParkingSpotDAO parkingSpotDAO = mock(ParkingSpotDAO.class);
            when(inputReaderUtil.readSelection()).thenReturn(1);
            when(parkingSpot.getId()).thenReturn(4);
            when(parkingSpotDAO.getNextAvailableSlot(parkingType.CAR)).thenReturn(1);

            parkingSpot.setAvailable(false);
            ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
            ticket.setParkingSpot(parkingSpot);
            ticket.setVehicleRegNumber("ABCDEF");
            ticket.setPrice(0);
            ticketDAO.saveTicket(ticket);
            System.out.println(ticketDAO.isSaved("ABCDEF")+ "is saved");
            System.out.println(parkingSpot.getId()+" this is your parking ID");
            System.out.println(ticket.getVehicleRegNumber()+" your vehicle reg is");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set up test mock objects");
        }

        parkingService.processIncomingVehicle();
        verify(parkingSpotDAO, Mockito.times(1)).getNextAvailableSlot(any(ParkingType.class));
    }

    @Test
    public void processIncomingVehicleTestingIfTicketIsSaved() {
        Ticket ticket = new Ticket();
        try {
            ParkingSpot parkingSpot = mock(ParkingSpot.class);
            when(inputReaderUtil.readSelection()).thenReturn(1);
            parkingSpotDAO.updateParking(parkingSpot);
            ticket.setParkingSpot(parkingSpot);
            ticket.setVehicleRegNumber("ABCDEF");
            ticket.setPrice(0);
            ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
            ticket.setOutTime(null);
            ticketDAO.saveTicket(ticket);
            parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
            } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set up test mock objects");
        }

        parkingService.processIncomingVehicle();
        verify(ticketDAO, Mockito.times(1)).saveTicket(ticket);
    }

    @Test
    public void getVehicleTypeTestCar() {
        try {

            when(inputReaderUtil.readSelection()).thenReturn(1);
            ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
            Ticket ticket = new Ticket();
            ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
            ticket.setParkingSpot(parkingSpot);
            ticket.setVehicleRegNumber("ABCDEF");
            parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set up test mock objects");
        }
        assertEquals(ParkingType.CAR, parkingService.getVehichleType());
    }

    @Test
    public void getVehicleTypeTestBike() {
        try {

            when(inputReaderUtil.readSelection()).thenReturn(2);
            ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
            Ticket ticket = new Ticket();
            ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
            ticket.setParkingSpot(parkingSpot);
            ticket.setVehicleRegNumber("ABCDEF");
            parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set up test mock objects");
        }
        assertEquals(ParkingType.BIKE, parkingService.getVehichleType());
    }

    @Test
    public void getVehicleTypeTestUnknown() {
        try {

            when(inputReaderUtil.readSelection()).thenReturn(3);
            ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
            Ticket ticket = new Ticket();
            ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
            ticket.setParkingSpot(parkingSpot);
            ticket.setVehicleRegNumber("ABCDEF");
            parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set up test mock objects");
        }
        Assertions.assertThrows(IllegalArgumentException.class, () -> parkingService.getVehichleType());
    }


    @Test
    public void processExitingVehicleTest() {
        try {
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");

            ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
            Ticket ticket = new Ticket();
            ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
            ticket.setParkingSpot(parkingSpot);
            when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
            when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);

            when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);

            parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set up test mock objects");
        }
        parkingService.processExitingVehicle();
        verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
    }


}
