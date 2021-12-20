import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class EchoClient {

  private final String SERVER_ADDR = "localhost";
  private final int SERVER_PORT = 8189;

  public EchoClient() throws IOException {
    Connection connection = Connection.openConnection(SERVER_ADDR, SERVER_PORT);
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

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
        while (true) {
          String outMessage = bufferedReader.readLine();
          if (outMessage != null) {
            connection.sendMessage(outMessage);
          }
          if (connection.isEndMessage()) {
            break;
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
