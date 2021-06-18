package com.parkit.parkingsystem;


import com.parkit.parkingsystem.dao.TicketDAO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TicketDaoTest{

    private static TicketDAO ticketDAO;

    @Test
    public void  isSavedTest(){
        boolean ticketSaved = ticketDAO.isSaved("ABCDEF");
        assertFalse(ticketSaved);
    }

}