import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class ClientGUI extends JFrame {

  private JTextField msgInputField;
  private JTextArea chatArea;


  public ClientGUI() {
    /*
    try {
      connection.o
    } catch (IOException e) {
      e.printStackTrace();
    }
    prepareGUI();*/
  }

  public void prepareGUI() {

//    // Параметры окна
//    setBounds(600, 300, 500, 500);
//    setTitle("Клиент");
//    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//
//    // Текстовое поле для вывода сообщений
//    chatArea = new JTextArea();
//    chatArea.setEditable(false);
//    chatArea.setLineWrap(true);
//    add(new JScrollPane(chatArea), BorderLayout.CENTER);
//
//    // Нижняя панель с полем для ввода сообщений и кнопкой отправки сообщений
//    JPanel bottomPanel = new JPanel(new BorderLayout());
//    JButton btnSendMsg = new JButton("Отправить");
//    bottomPanel.add(btnSendMsg, BorderLayout.EAST);
//    msgInputField = new JTextField();
//    add(bottomPanel, BorderLayout.SOUTH);
//    bottomPanel.add(msgInputField, BorderLayout.CENTER);
//    btnSendMsg.addActionListener(new ActionListener() {
//      @Override
//      public void actionPerformed(ActionEvent e) {
//        sendMessage();
//      }
//    });
//    msgInputField.addActionListener(new ActionListener() {
//      @Override
//      public void actionPerformed(ActionEvent e) {
//        sendMessage();
//      }
//    });
//
//    // Настраиваем действие на закрытие окна
//    addWindowListener(new WindowAdapter() {
//      @Override
//      public void windowClosing(WindowEvent e) {
//        super.windowClosing(e);
//        try {
//          out.writeUTF("/end");
//          closeConnection();
//        } catch (IOException exc) {
//          exc.printStackTrace();
//        }
//      }
//    });
//
//    setVisible(true);
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> new ClientGUI());
  }

}
