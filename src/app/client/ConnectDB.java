package app.client;

import app.HomeUpdate;
import app.db.Database;
import app.server.Server;
import app.ui.ButtonGradient;
import app.ui.RoundedPasswordField;
import app.ui.RoundTextField;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

public class ConnectDB extends JFrame {
    private JPanel panel;
    private JPanel welcome;
    private JPanel intro;
    private JLabel require;
    private JLabel lb1;
    private JPanel inputs;
    private JPanel infor;
    private JTextField tfUsername;
    private JTextField tfHostname;
    private JPanel btns;
    private JButton btnConnect;
    private JButton btnBack;
    private JLabel error;
    private JTextField tfServername;
    private JPasswordField tfPassword;
    private JCheckBox showCheckBox;

    public ConnectDB() {
        super("Connect to server");
        setSize(new Dimension(500, 500));
        setContentPane(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        showCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (showCheckBox.isSelected()) {
                    tfPassword.setEchoChar((char) 0);
                } else {
                    tfPassword.setEchoChar('*');
                }
            }
        });
        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                HomeUpdate homeUpdate = new HomeUpdate();
                homeUpdate.main(new String[] {"true"});
                setVisible(false);
            }
        });
        btnConnect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String hostname = tfHostname.getText();
                String username = tfUsername.getText();
                String password = tfPassword.getText();
                String servername = tfServername.getText();

                if (hostname.isEmpty() || username.isEmpty()  || servername.isEmpty()) {
                    error.setText("Please enter all information");
                    return;
                }

                try {
                    if (!error.getText().isEmpty()) {
                        error.setText("");
                    }

                    Server server = Server.getInstance();
                    Client client = HomeUpdate.getClient();
                    client.sendMessage("HOSTNAME:"+hostname);
                    client.sendMessage("USERNAME:"+username);
                    client.sendMessage("PASSWORD:"+password);
                    client.sendMessage("SERVER_NAME:"+servername);
                    if (server.connectDB()) {
                        System.out.println("Connect to "+ servername +" successfully");
                        InforPanel inforPanel = new InforPanel();
                        inforPanel.setVisible(true);
                    } else {
                        error.setText("Can't connect to database");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public static void main(String[] args) {
        ConnectDB connectDB = new ConnectDB();
        connectDB.setVisible(true);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        tfHostname = new RoundTextField("", 12);

        tfUsername = new RoundTextField("", 12);

        tfPassword = new RoundedPasswordField("", 12);
        tfPassword.setEchoChar('*');

        tfServername = new RoundTextField("", 12);

        btnBack = new ButtonGradient(
                Color.decode("#7895B2"),
                Color.decode("#7895B2"),
                Color.BLACK,
                new EmptyBorder(5, 20, 5, 20)
        );
        btnConnect = new ButtonGradient(
                Color.decode("#E8DFCA"),
                Color.decode("#E8DFCA"),
                Color.BLACK,
                new EmptyBorder(5, 20, 5, 20)
        );
    }
}
