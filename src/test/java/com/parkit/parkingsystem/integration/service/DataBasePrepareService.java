package com.parkit.parkingsystem.integration.service;

import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;

import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDateTime;

import static com.parkit.parkingsystem.constants.Fare.CAR_RATE_PER_HOUR;


public class DataBasePrepareService {

    DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();

    public void clearDataBaseEntries() {
        Connection connection = null;
        try {
            connection = dataBaseTestConfig.getConnection();

            //set parking entries to available
            connection.prepareStatement("update parking set available = true").execute();

            //clear ticket entries;
            connection.prepareStatement("truncate table ticket").execute();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dataBaseTestConfig.closeConnection(connection);
        }
    }

    public void inCar(int times) {
        LocalDateTime inTime = LocalDateTime.now().minusHours(times);

        try (
                Connection con = dataBaseTestConfig.getConnection();
                Statement stmt = con.createStatement()
        ) {
            String sql = String.format(
                    "insert into ticket(PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME) values(%d, '%s', %d, '%s', %s)",
                    1,
                    "ABCDEF",
                    0,
                    inTime,
                    "NULL"
            );
            stmt.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void carExit() {
        LocalDateTime outTime = LocalDateTime.now();

        try (Connection con = dataBaseTestConfig.getConnection()) {
            try (Statement stmt = con.createStatement()) {
                String sql = String.format(
                        "update ticket set PRICE= %s, OUT_TIME= '%s' where ID= %d",
                        CAR_RATE_PER_HOUR,
                        outTime,
                        1
                );
                stmt.execute(sql);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
