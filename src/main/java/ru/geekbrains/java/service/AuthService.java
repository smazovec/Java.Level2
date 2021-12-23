package ru.geekbrains.java.service;

public interface AuthService {

  void start();

  void stop();

  String getNickByLoginPass(String login, String pass);

}
