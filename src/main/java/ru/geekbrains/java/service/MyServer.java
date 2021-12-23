package ru.geekbrains.java.service;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import ru.geekbrains.java.handler.ClientHandler;
import ru.geekbrains.java.service.implementation.BaseAuthService;

public class MyServer {

  private static final int PORT = 8189;
  private List<ClientHandler> clients;
  private AuthService authService;

  public AuthService getAuthService() {
    return authService;
  }

  public MyServer() {
    System.out.println("Сервер запустился");
    try (ServerSocket server = new ServerSocket(PORT)) {
      authService = new BaseAuthService();
      authService.start();
      clients = new ArrayList<>();
      while (true) {
        System.out.println("Сервер ожидает подключения...");
        Socket socket = server.accept();
        System.out.println("Клиент подключился");
        new ClientHandler(this, socket);
      }
    } catch (IOException e) {
      System.out.println("Ошибка в работе сервера");
    } finally {
      if (authService != null) {
        authService.stop();
      }
    }
  }

  public synchronized boolean isNickBusy(String nick) {
    for (ClientHandler o : clients) {
      if (o.getName().equals(nick)) {
        return true;
      }
    }
    return false;
  }

  public synchronized void broadcastMsg(String msg) {
    clients.forEach(o -> o.sendMsg(msg));
  }

  public synchronized void unsubscribe(ClientHandler o) {
    clients.remove(o);
  }

  public synchronized void subscribe(ClientHandler o) {
    clients.add(o);
  }
}
