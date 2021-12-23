package ru.geekbrains.java.service;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class MyClient extends JFrame {

  private final String SERVER_ADDR = "localhost";
  private final int SERVER_PORT = 8189;

  private boolean authorized = false;

  private JTextField msgInputField;
  private JTextArea chatArea;

  private Socket socket;
  private DataInputStream in;
  private DataOutputStream out;

  public MyClient() {
    try {
      openConnection();
    } catch (IOException e) {
      e.printStackTrace();
    }
    prepareGUI();
  }

  public void openConnection() throws IOException {
    try {
      socket = new Socket("localhost", 8189);
      in = new DataInputStream(socket.getInputStream());
      out = new DataOutputStream(socket.getOutputStream());
      setAuthorized(false);
      Thread t = new Thread(() -> {
        try {
          while (true) {
            String strFromServer = in.readUTF();
            if (strFromServer.startsWith("/authok")) {
              setAuthorized(true);
              break;
            } else if (strFromServer.equalsIgnoreCase("/end")) {
              break;
            }

            chatArea.append(strFromServer + "\n");
          }
          while (true) {
            String strFromServer = in.readUTF();
            if (strFromServer.equalsIgnoreCase("/end")) {
              break;
            }
            chatArea.append(strFromServer);
            chatArea.append("\n");
          }
        } catch (Exception e) {
          e.printStackTrace();
        } finally {
          closeConnection();
        }
      });
      t.setDaemon(true);
      t.start();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void closeConnection() {

    if (socket.isClosed()) {
      return;
    }

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

  public void sendMessage() {
    if (!msgInputField.getText().trim().isEmpty()) {
      try {
        out.writeUTF(msgInputField.getText());
        msgInputField.setText("");
        msgInputField.grabFocus();
      } catch (IOException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Ошибка отправки сообщения");
      }
    }
  }

  public void prepareGUI() {
    // Параметры окна
    setBounds(600, 300, 500, 500);
    setTitle("Клиент");
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    // Текстовое поле для вывода сообщений
    chatArea = new JTextArea();
    chatArea.setEditable(false);
    chatArea.setLineWrap(true);
    add(new JScrollPane(chatArea), BorderLayout.CENTER);

    // Нижняя панель с полем для ввода сообщений и кнопкой отправки сообщений
    JPanel bottomPanel = new JPanel(new BorderLayout());
    JButton btnSendMsg = new JButton("Отправить");
    bottomPanel.add(btnSendMsg, BorderLayout.EAST);
    msgInputField = new JTextField();
    add(bottomPanel, BorderLayout.SOUTH);
    bottomPanel.add(msgInputField, BorderLayout.CENTER);
    btnSendMsg.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        sendMessage();
      }
    });
    msgInputField.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        sendMessage();
      }
    });

    // Настраиваем действие на закрытие окна
    addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        super.windowClosing(e);
        try {
          out.writeUTF("/end");
        } catch (IOException exc) {
          exc.printStackTrace();
        }
      }
    });

    setVisible(true);
  }

  public void setAuthorized(boolean authorized) {
    this.authorized = authorized;
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> new MyClient());
  }

}
