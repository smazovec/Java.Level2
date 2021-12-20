import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;

public class EchoServer {

  public EchoServer() throws IOException {

    try (ServerSocket serverSocket = new ServerSocket(8189)) {
      System.out.println("Сервер запущен, ожидаем подключения...");
      Connection connection = Connection.openConnection(serverSocket);
      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
      System.out.println("Клиент подключился");
      connection.sendMessage("Успешно подключен к серверу");

      new Thread(() -> {
        try {
          while (true) {
            String inMessage = connection.receiveMessage();
            System.out.println("Клиент: " + inMessage);
            if (connection.isEndMessage()) {
              break;
            }
          }
          connection.sendEndMessage();
          connection.closeConnection();
          System.out.println("Серверное соединение закрыто");
        } catch (IOException e) {
          e.printStackTrace();
        }
      }).start();

      new Thread(() -> {
        try {
          while (true) {
            String outMessage = bufferedReader.readLine();
            if (outMessage != null) {
              connection.sendMessage(outMessage);
            }
            if (connection.isEndMessage()) {
              break;
            }
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
      }).start();

    } catch (
        IOException e) {
      e.printStackTrace();
    }

  }

  public static void main(String[] args) {
    try {
      new EchoServer();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
