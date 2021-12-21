import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Connection {

  public static final String END_MESSAGE = "/end";

  private final Socket socket;
  private final DataInputStream in;
  private final DataOutputStream out;
  private String inMessage;

  private Connection(String address, int port) throws IOException {
    socket = new Socket(address, port);
    in = new DataInputStream(socket.getInputStream());
    out = new DataOutputStream(socket.getOutputStream());
  }

  private Connection(ServerSocket serverSocket) throws IOException {
    socket = serverSocket.accept();
    in = new DataInputStream(socket.getInputStream());
    out = new DataOutputStream(socket.getOutputStream());
  }

  public static Connection openConnection(String address, int port) throws IOException {
    return new Connection(address, port);
  }

  public static Connection openConnection(ServerSocket serverSocket) throws IOException {
    return new Connection(serverSocket);
  }

  public void closeConnection() {
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

  public void sendMessage(String message) throws IOException {
    this.out.writeUTF(message);
  }

  public void sendEndMessage() throws IOException {
    sendMessage(END_MESSAGE);
  }

  public String receiveMessage() throws IOException {
    inMessage = this.in.readUTF();
    return inMessage;
  }

  public boolean isEndMessage() {
    return inMessage.equals(END_MESSAGE);
  }

  public boolean isClosed() {
    return socket.isClosed();
  }

}
