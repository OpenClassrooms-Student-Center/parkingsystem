package com.parkit.parkingsystem.constants;

/**.
 * this class contains diffrent SQL queries
 *
 * @author Nicolas BIANCUCCI
 *
 */
public class DBConstants {
	/**.
	 * sql query to used for available parking spots
	 */
	public static final String GET_NEXT_PARKING_SPOT = "select min(PARKING_NUMBER) from parking where AVAILABLE = true and TYPE = ?";
	/**.
	 * sql query to used to update parking spot
	 */
	public static final String UPDATE_PARKING_SPOT = "update parking set available = ? where PARKING_NUMBER = ?";
	/**.
	 * sql query to used to save tickets into DB
	 */
	public static final String SAVE_TICKET = "insert into ticket(PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME) values(?,?,?,?,?)";
	/**.
	 * sql query to used to update ticket with price and outTime
	 */
	public static final String UPDATE_TICKET = "update ticket set PRICE=?, OUT_TIME=? where ID=?";
	/**.
	 * sql query to used to retrieve a ticket from DB
	 */
	public static final String GET_TICKET = "select t.PARKING_NUMBER, t.ID, t.PRICE, t.IN_TIME, t.OUT_TIME, p.TYPE from ticket t,parking p where p.parking_number = t.parking_number and t.VEHICLE_REG_NUMBER=? order by t.IN_TIME  limit 1";
	/**.
	 * sql query to used to count reccurence with Vehicule reg number
	 */
	public static final String IS_RECURRENT = "select count(*) from ticket t where t.VEHICLE_REG_NUMBER = ?";
}
