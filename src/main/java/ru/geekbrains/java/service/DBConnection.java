package ru.geekbrains.java.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DBConnection {

  private static Connection connection;
  private static final Logger LOGGER = LogManager.getLogger(DBConnection.class);

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
        LOGGER.info("Connection to database closed");
      }
    } catch (SQLException e) {
      LOGGER.error(e);
    }
  }

}
