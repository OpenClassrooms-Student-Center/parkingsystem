package com.parkit.parkingsystem.repository;

import com.parkit.parkingsystem.model.Ticket;

public interface ITicketDAO {
    boolean saveTicket(Ticket ticket);
    Ticket getTicket(String vehicleRegNumber);
    boolean updateTicket(Ticket ticket);
}
