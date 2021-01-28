package com.parkit.parkingsystem.integration.config;

import com.parkit.parkingsystem.config.DataBaseConfig;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class DataBaseTestConfig extends DataBaseConfig {

  private static final Logger logger = LogManager.getLogger("DataBaseTestConfig");

  public Connection getConnection() throws ClassNotFoundException, SQLException, IOException {
    Properties properties = new Properties();
    FileInputStream fis = new FileInputStream("src/main/resources/log4j.properties");
    properties.load(fis);
    String url = properties.getProperty("urltest");
    String user = properties.getProperty("user");
    String password = properties.getProperty("password");

    logger.info("Create DB connection");
    Class.forName("com.mysql.cj.jdbc.Driver");
    return DriverManager.getConnection(url, user, password);
  }

  public void closeConnection(Connection con) {
    if (con != null) {
      try {
        con.close();
        logger.info("Closing DB connection");
      } catch (SQLException e) {
        logger.error("Error while closing connection", e);
      }
    }
  }

  public void closePreparedStatement(PreparedStatement ps) {
    if (ps != null) {
      try {
        ps.close();
        logger.info("Closing Prepared Statement");
      } catch (SQLException e) {
        logger.error("Error while closing prepared statement", e);
      }
    }
  }

  public void closeResultSet(ResultSet rs) {
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
