import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class EchoClient {

  private final String SERVER_ADDR = "192.168.50.132";
  private final int SERVER_PORT = 8189;

  public EchoClient() throws IOException {
    final Connection connection = Connection.openConnection(SERVER_ADDR, SERVER_PORT);
    final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

    new Thread(() -> {
      try {
        while (true) {
          String inMessage = connection.receiveMessage();
          System.out.println("Сервер: " + inMessage);
          if (connection.isEndMessage()) {
            break;
          }
        }
        connection.sendEndMessage();
        connection.closeConnection();
        System.out.println("Клиентское соединение закрыто");
      } catch (Exception e) {
        e.printStackTrace();
      }
    }).start();

    new Thread(() -> {
      try {
        while (!connection.isClosed()) {
          String outMessage = bufferedReader.readLine();
          if (connection.isClosed()) {
            break;
          }
          if (outMessage != null) {
            connection.sendMessage(outMessage);
          }
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }).start();

  }

  public static void main(String[] args) {
    try {
      new EchoClient();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
