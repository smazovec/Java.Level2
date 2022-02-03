package ru.geekbrains.java.service;

public interface AuthService {

  void start();

  void stop();

  String loginUser(String login, String pass);

}
