package ru.geekbrains.java.handler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import ru.geekbrains.java.service.MyServer;

public class ClientHandler {

  private final MyServer myServer;
  private final Socket socket;
  private final DataInputStream in;
  private final DataOutputStream out;
  private boolean authenticated;

  private String name;

  public String getName() {
    return name;
  }

  public ClientHandler(MyServer myServer, Socket socket) {
    try {
      this.myServer = myServer;
      this.socket = socket;
      this.in = new DataInputStream(socket.getInputStream());
      this.out = new DataOutputStream(socket.getOutputStream());
      this.name = "";
      new Thread(() -> {
        try {
          authentication();
          readMessages();
        } catch (IOException e) {
          e.printStackTrace();
        } finally {
          closeConnection();
        }
      }).start();
    } catch (IOException e) {
      throw new RuntimeException("Проблемы при создании обработчика клиента");
    }
  }

  public void authentication() throws IOException {
    long a = System.currentTimeMillis();
    Thread authDemon = new Thread(() -> {
      while (true) {
        if (System.currentTimeMillis() - a > 120000) {
          sendMsg("Отключен по таймауту");
          sendMsg("/end");
          return;
        } else if (isAuthenticated()) {
          return;
        }
      }
    });
    authDemon.setDaemon(true);
    authDemon.start();

    while (true) {
      String str = in.readUTF();
      if (str.startsWith("/auth")) {
        String[] parts = str.split("\\s");
        String nick = myServer.getAuthService().loginUser(parts[1], parts[2]);
        if (nick != null) {
          if (!myServer.isNickBusy(nick)) {
            sendMsg("/authok;" + nick);
            name = nick;
            setAuthenticated(true);
            myServer.broadcastMsg(name + " зашел в чат");
            myServer.subscribe(this);
            myServer.sendOnlineClients();
            return;
          } else {
            sendMsg("Учетная запись уже используется");
          }
        } else {
          sendMsg("Неверные логин/пароль");
        }
      } else if (str.startsWith("/end")) {
        sendMsg("/end");
        return;
      }
    }
  }

  public void readMessages() throws IOException {
    while (authenticated) {
      String strFromClient = in.readUTF();
      if (strFromClient.equals("/end")) {
        return;
      } else if (strFromClient.startsWith("/recipient")) {
        String recipient = strFromClient.substring(11, strFromClient.indexOf(";")).trim();
        String message = strFromClient.substring(strFromClient.indexOf(";") + 1).trim();
        if (recipient.equals("All...")) {
          myServer.broadcastMsg(name + ": " + message);
        } else {
          myServer.privateMsg(name + ": " + message, recipient, name);
        }
      }
    }
  }

  public void sendMsg(String msg) {
    try {
      out.writeUTF(msg);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void closeConnection() {
    sendMsg("/end");
    myServer.unsubscribe(this);
    myServer.broadcastMsg(name + " вышел из чата");
    try {
      in.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    try {
      out.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    try {
      socket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private synchronized void setAuthenticated(Boolean auth) {
    this.authenticated = auth;
  }

  private synchronized boolean isAuthenticated() {
    return this.authenticated;
  }
}
