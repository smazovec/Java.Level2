package ru.geekbrains.java;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.Socket;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;

public class ClientApp extends JFrame {

  private static final String SERVER_ADDR = "localhost";
  private static final int SERVER_PORT = 8189;
  private static final String ALL_RECIPIENT = "All...";
  private boolean authorized;

  private JPanel bottomPanel;
  private JTextField msgInputField;
  private JTextArea chatArea;
  private JButton btnSendMsg;
  private JList<String> clientsListArea;
  private final DefaultListModel<String> clientsListData = new DefaultListModel<>();

  private JPanel loginPanel;
  private JTextField loginArea;
  private JTextField passArea;

  private Socket socket;
  private DataInputStream in;
  private DataOutputStream out;
  private String currentRecipient = ALL_RECIPIENT;
  private String nick = "";
  private File history_file;
  private BufferedWriter history_writer;
  private BufferedReader history_reader;

  public ClientApp() {
    try {
      openConnection();
    } catch (IOException e) {
      e.printStackTrace();
    }
    prepareGUI();
  }

  private void openConnection() throws IOException {
    try {
      socket = new Socket(SERVER_ADDR, SERVER_PORT);
      in = new DataInputStream(socket.getInputStream());
      out = new DataOutputStream(socket.getOutputStream());
      Thread t = new Thread(() -> {
        try {
          while (true) {
            String strFromServer = in.readUTF();
            if (strFromServer.startsWith("/authok")) {
              nick = strFromServer.substring(strFromServer.indexOf(";") + 1).trim();
              setTitle(nick);
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
            } else if (strFromServer.startsWith("/clientlst")) {
              updateClientList(strFromServer);
              continue;
            }
            chatArea.append(strFromServer + "\n");

            // Запись истории чата
            try (BufferedWriter history_writer = new BufferedWriter(
                new FileWriter(history_file, true))) {
              history_writer.write(strFromServer + "\n");

            } catch (IOException e) {
              e.printStackTrace();
            }
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

  private void closeConnection() {

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

  private void sendMessage() {
    if (!msgInputField.getText().trim().isEmpty()) {
      try {
        out.writeUTF("/recipient@" + currentRecipient + ";" + msgInputField.getText());
        msgInputField.setText("");
        msgInputField.grabFocus();
      } catch (IOException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Ошибка отправки сообщения");
      }
    }
  }

  private void prepareGUI() {
    // Параметры окна
    setBounds(600, 300, 500, 500);
    setTitle("Клиент");
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    // Табличное поле для вывода списка участников чата
    clientsListData.addElement(currentRecipient);
    clientsListArea = new JList<>(clientsListData);
    clientsListArea.setFixedCellWidth(100);
    clientsListArea.setSelectedIndex(0);
    add(new JScrollPane(clientsListArea), BorderLayout.WEST);

    // Текстовое поле для вывода сообщений
    chatArea = new JTextArea();
    chatArea.setEditable(false);
    chatArea.setLineWrap(true);
    add(new JScrollPane(chatArea), BorderLayout.CENTER);

    // Нижняя панель с полем для ввода сообщений и кнопкой отправки сообщений
    bottomPanel = new JPanel(new BorderLayout());
    btnSendMsg = new JButton("Отправить " + currentRecipient);
    bottomPanel.add(btnSendMsg, BorderLayout.EAST);
    msgInputField = new JTextField();
    add(bottomPanel, BorderLayout.SOUTH);
    bottomPanel.add(msgInputField, BorderLayout.CENTER);

    clientsListArea.addListSelectionListener(e -> onListSelect(e));
    btnSendMsg.addActionListener(e -> sendMessage());
    msgInputField.addActionListener(e -> sendMessage());

    // Скрыты до авторизации
    bottomPanel.setVisible(false);

    // Нижняя панель с полями для ввода логинаи пароля
    loginPanel = new JPanel(new BorderLayout());
    add(loginPanel, BorderLayout.NORTH);

    // Элементы для авторизации
    loginArea = new JTextField(15);
    loginArea.setToolTipText("Логин");
    passArea = new JTextField(15);
    passArea.setToolTipText("Пароль");
    JButton btnLogin = new JButton("Залогиниться");

    loginPanel.add(loginArea, BorderLayout.WEST);
    loginPanel.add(passArea, BorderLayout.CENTER);
    loginPanel.add(btnLogin, BorderLayout.EAST);

    btnLogin.addActionListener(e -> onLoginClick());

    // Настраиваем действие на закрытие окна
    this.addWindowListener(new WindowAdapter() {
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

  private void onListSelect(ListSelectionEvent e) {
    if (e.getValueIsAdjusting()) {
      currentRecipient = clientsListArea.getSelectedValue().toString();
      btnSendMsg.setText("Отправить " + currentRecipient);
    }
  }

  private void onLoginClick() {
    try {
      out.writeUTF("/auth " + loginArea.getText() + " " + passArea.getText());
      loginArea.setText("");
      passArea.setText("");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void setAuthorized(boolean authorized) {
    bottomPanel.setVisible(authorized);
    loginPanel.setVisible(!authorized);

    history_file = new File("./history_" + nick.trim() + ".txt");

    // восстановление истории сообщений
    if (!history_file.exists()) {
      try {
        history_file.createNewFile();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    try (LineNumberReader lnr = new LineNumberReader(new FileReader(history_file));
        BufferedReader history_reader = new BufferedReader(new FileReader(history_file))) {

      // не придумал другого способа, кроме как создать LineNumberReader
      // и через него получить общее количество строк в файле истории
      long historyLinesCount = lnr.lines().count();
      history_reader.lines().skip(historyLinesCount > 100 ? historyLinesCount - 100 : 0)
          .forEach(s -> chatArea.append(s + "\n"));
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  private boolean iAuthorized() {
    return this.authorized;
  }

  private void updateClientList(String clientList) {
    clientsListData.clear();
    clientsListData.addElement(ALL_RECIPIENT);
    String[] parts = clientList.split(";");
    for (int i = 1; i < parts.length; i++) {
      clientsListData.addElement(parts[i]);
    }
    clientsListArea.setSelectedIndex(0);
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(ClientApp::new);
  }

}
