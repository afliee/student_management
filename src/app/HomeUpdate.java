package app;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import app.client.Client;
import app.client.ConnectDB;
import app.midleware.DES;
import app.server.Server;
import app.ui.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class HomeUpdate extends JFrame{
    private Server server;
    private Client client;
    private boolean servered = false;
    private static Client external_client = null;
    public HomeUpdate() {
        super("Home");
        setSize(new Dimension(500, 500));
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                server.close();
                dispose();
                System.exit(0);
            }
        });
        btnConnect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String host = tfHostname.getText();
                String port = tfPort.getText();

                if (host.isEmpty() || port.isEmpty()) {
                    error.setText("Please enter hostname and port");
                    return;
                }

                try {
                    if (!error.getText().isEmpty()) {
                        error.setText("");
                    }

                    client = new Client(host, Integer.parseInt(port), getServer().getPort());
                    external_client = client;
                    boolean is_start = client.start();
                    if (is_start) {
                        ConnectDB connectFrame = new ConnectDB();
                        connectFrame.setVisible(true);
                        setVisible(false);
                    } else {
                        error.setText("Cannot connect to server");
                    }
                } catch (Exception er) {
                    error.setText("Cannot connect to server");
                    er.printStackTrace();
                }
            }
        });

        setContentPane(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    public static Client getClient() {
        return external_client;
    }

    public Server getServer() throws IOException {
        return Server.getInstance();
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public static void main(String[] args) {
        String isVisible;
        HomeUpdate home = new HomeUpdate();
//
//        try {
////            bytep encrypt = DES.getInstance().encrypt("1");
////            System.out.println(new String(encrypt));
////            System.out.println("decrypted: "+DES.getInstance().decrypt(encrypt.getBytes()));
//            String encrypted = DES.getInstance().encrypt("1");
//            System.out.println("encrypted: "+encrypted);
//            System.out.println("decrypted: "+DES.getInstance().decrypt(encrypted));
//        }catch (Exception e) {
//            e.printStackTrace();
//        }

        if (args.length > 0) {
            isVisible = args[0];
            if (Boolean.parseBoolean(isVisible)) {
                home.setVisible(true);
            }
        } else {
            try {
                Server server = home.getServer();
                server.start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        tfHostname = new RoundTextField("", 12);
        tfHostname.setUI(new HintTextFieldUI("Hostname", true));

        tfPort = new RoundTextField("", 12);
        tfPort.setUI(new HintTextFieldUI("Port", true));

        btnCancel = new ButtonGradient(
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
    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    // Generated using JFormDesigner Evaluation license - unknown
    private JPanel panel;
    private JPanel intro;
    private JLabel require;
    private JLabel lb1;
    private JPanel inputs;
    private JPanel infor;
    private JTextField tfPort;
    private JTextField tfHostname;
    private JPanel btns;
    private JButton btnConnect;
    private JButton btnCancel;
    private JLabel error;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
