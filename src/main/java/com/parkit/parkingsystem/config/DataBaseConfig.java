package com.parkit.parkingsystem.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**.
 * this class connect to database and close them
 *
 * @author Nicolas BIANCUCCI
 */

public class DataBaseConfig {

    /**.
     * databaseconfig logger
     */
    private static final Logger LOGGER = LogManager.getLogger("DataBaseConfig");

    /**.
     * Connect to sql as root
     *
     * @return connection to database
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public Connection getConnection() throws ClassNotFoundException, SQLException {
        LOGGER.info("Create DB connection");
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/prod?serverTimezone=UTC", "root", "nico5310");
    }

    /**.
     * close database connection
     *
     * @param con Connection con
     */
    public void closeConnection(final Connection con) {
        if (con != null) {
            try {
                con.close();
                LOGGER.info("Closing DB connection");
            } catch (SQLException e) {
                LOGGER.error("Error while closing connection", e);
            }
        }
    }

    /**.
     * close prepared statement
     *
     * @param ps as an instance of PreparedStatement to be closed
     */
    public void closePreparedStatement(final PreparedStatement ps) {
        if (ps != null) {
            try {
                ps.close();
                LOGGER.info("Closing Prepared Statement");
            } catch (SQLException e) {
                LOGGER.error("Error while closing prepared statement", e);
            }
        }
    }

    /**.
     * close resultset
     *
     * @param rs as an instance of resultset to be closed
     */
    public void closeResultSet(final ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
                LOGGER.info("Closing Result Set");
            } catch (SQLException e) {
                LOGGER.error("Error while closing result set", e);
            }
        }
    }
}
