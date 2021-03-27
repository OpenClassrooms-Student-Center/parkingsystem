package com.parkit.parkingsystem.util;

import com.parkit.parkingsystem.config.DataBaseConfig;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Philémon Globléhi <philemon.globlehi@gmail.com>
 */
public class Singleton {

    private DataBaseConfig dataBaseConfig = new DataBaseConfig();
    private Connection con = null;

    public Connection connection() throws SQLException, ClassNotFoundException {
        if (null != this.con) {
            return con;
        }

        this.con = dataBaseConfig.getConnection();
        return this.con;
    }
}
