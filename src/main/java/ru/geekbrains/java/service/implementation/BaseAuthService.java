package ru.geekbrains.java.service.implementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.geekbrains.java.service.AuthService;
import ru.geekbrains.java.service.DBConnection;

public class BaseAuthService implements AuthService {

  private static final Logger LOGGER = LogManager.getLogger(BaseAuthService.class);
  private Connection dbCon;

  @Override
  public void start() {
    try {
      dbCon = DBConnection.getInstance();
    } catch (SQLException e) {
      LOGGER.error(e);
    }
    LOGGER.info("Authentication service running");
  }

  @Override
  public void stop() {
    DBConnection.disconnect();
    LOGGER.info("Authentication service stopped");
  }

  @Override
  public String loginUser(String login, String pass) {

    try {
      PreparedStatement ps = dbCon.prepareStatement(
          "SELECT nick FROM users WHERE login = ? AND pass = ?;");
      ps.setString(1, login.trim());
      ps.setString(2, pass.trim());
      ResultSet rs = ps.executeQuery();

      if (rs.next()) {
        return rs.getString("nick");
      }

    } catch (SQLException e) {
      LOGGER.error(e);
    }
    return null;
  }

}


