package ru.geekbrains.java.service;

import com.sun.corba.se.spi.activation.Server;
import ru.geekbrains.java.ServerApp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static Connection connection;

    private DBConnection() {
    }

    public static Connection getInstance() throws SQLException {
        if (connection == null) {
            return DriverManager.getConnection("jdbc:sqlite::resource:entries.db");
        }
        return connection;
    }

    public static void disconnect() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
