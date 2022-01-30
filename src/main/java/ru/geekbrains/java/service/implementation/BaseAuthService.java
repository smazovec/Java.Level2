package ru.geekbrains.java.service.implementation;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import ru.geekbrains.java.service.AuthService;
import ru.geekbrains.java.service.DBConnection;

public class BaseAuthService implements AuthService {

    private static class Entry {

        private final String login;
        private final String pass;
        private final String nick;

        public Entry(String login, String pass, String nick) {
            this.login = login;
            this.pass = pass;
            this.nick = nick;
        }
    }

    private final List<Entry> entries;

    @Override
    public void start() {
        System.out.println("Сервис аутентификации запущен");
    }

    @Override
    public void stop() {
        System.out.println("Сервис аутентификации остановлен");
    }

    @Override
    public String loginUser(String login, String pass) {
        for (Entry o : entries) {
            if (o.login.equals(login) && o.pass.equals(pass)) {
                return o.nick;
            }
        }
        return null;
    }

    public BaseAuthService() {
        entries = new ArrayList<>();

        try {
            Connection dbCon = DBConnection.getInstance();
            Statement stmt = dbCon.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM users;");
            while (rs.next()) {
                entries.add(new Entry(rs.getString("login"), rs.getString("pass"), rs.getString("nick")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        DBConnection.disconnect();
    }

}

