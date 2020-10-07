package com.parkit.parkingsystem.constants;

/**
 * Manage DB constants.
 */
public class DBConstants {

    /**
     * Next parking spot.
     */
    public static final String GET_NEXT_PARKING_SPOT =
            "select min(PARKING_NUMBER) from parking where AVAILABLE ="
                    + "true and TYPE = ?";

    /**
     * Update parking spot.
     */
    public static final String UPDATE_PARKING_SPOT =
            "update parking set available = ? where PARKING_NUMBER = ?";

    /**
     * Save ticket.
     */
    public static final String SAVE_TICKET =
            "insert into ticket(PARKING_NUMBER, VEHICLE_REG_NUMBER, "
                    + "PRICE, IN_TIME, OUT_TIME) values(?,?,?,?,?)";

    /**
     * Update ticket.
     */
    public static final String UPDATE_TICKET =
            "update ticket set PRICE=?, OUT_TIME=? where ID=?";

    /**
     * Get ticket.
     */
    public static final String GET_TICKET =
            "select t.PARKING_NUMBER, t.ID, t.PRICE, t.IN_TIME, t.OUT_TIME,"
                    + "p.TYPE from ticket t,parking p where p.parking_number "
                    + "= t.parking_number and"
                    + "t.VEHICLE_REG_NUMBER=? order by t.IN_TIME  limit 1";
}
