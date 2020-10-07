package com.parkit.parkingsystem.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

/**
 * Configuration for DataBase.
 */
public class DataBaseConfig {

    /**
     * @see Logger
     */
    private static final Logger logger = LogManager.getLogger("DataBaseConfig");

    /**
     * Manage DB connection.
     * @return DataBase Connection configuration
     * @throws ClassNotFoundException if doesn't find DB
     * @throws SQLException if can't connect
     */
    public Connection getConnection()
            throws ClassNotFoundException, SQLException {
        logger.info("Create DB connection");
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/prod", "root", "rootroot");
    }

    /**
     * Manage DB connection closing.
     * @param con Connection
     */
    public void closeConnection(final Connection con) {
        if (con != null) {
            try {
                con.close();
                logger.info("Closing DB connection");
            } catch (SQLException e) {
                logger.error("Error while closing connection", e);
            }
        }
    }

    /**
     * Manage Prepared Statement closing.
     * @param ps PreparedStatement
     */
    public void closePreparedStatement(final PreparedStatement ps) {
        if (ps != null) {
            try {
                ps.close();
                logger.info("Closing Prepared Statement");
            } catch (SQLException e) {
                logger.error("Error while closing prepared statement", e);
            }
        }
    }

    /**
     * Manage ResultSet closing.
     * @param rs ResultSet
     */
    public void closeResultSet(final ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
                logger.info("Closing Result Set");
            } catch (SQLException e) {
                logger.error("Error while closing result set", e);
            }
        }
    }
}
