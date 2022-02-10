package ru.geekbrains.java.service;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.geekbrains.java.handler.ClientHandler;
import ru.geekbrains.java.service.implementation.BaseAuthService;

public class MyServer {

  private static final int PORT = 8189;
  private static final Logger LOGGER = LogManager.getLogger(MyServer.class);

  private List<ClientHandler> clients;
  private AuthService authService;

  public AuthService getAuthService() {
    return authService;
  }

  public MyServer() {
    LOGGER.info("Server is run");
    try (ServerSocket server = new ServerSocket(PORT)) {
      authService = new BaseAuthService();
      authService.start();
      clients = new ArrayList<>();
      while (true) {
        LOGGER.info("Server waiting connection...");
        Socket socket = server.accept();
        LOGGER.info("Client is connecting...");
        new ClientHandler(this, socket);
      }
    } catch (IOException e) {
      LOGGER.error(e);
    } finally {
      if (authService != null) {
        authService.stop();
        LOGGER.info("Authentication service stopping");
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

  public synchronized void privateMsg(String msg, String recipient, String sender) {
    clients.stream().filter(o -> o.getName().equals(recipient)
        || o.getName().equals(sender)).forEach(o -> o.sendMsg(msg));
  }

  public synchronized void sendOnlineClients() {
    StringBuffer clientListBuffer = new StringBuffer();
    clients.forEach(o -> clientListBuffer.append(";" + o.getName()));
    broadcastMsg("/clientlst" + clientListBuffer.toString());
  }

  public synchronized void unsubscribe(ClientHandler o) {
    clients.remove(o);
  }

  public synchronized void subscribe(ClientHandler o) {
    clients.add(o);
  }

}
