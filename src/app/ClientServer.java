//package app;
//
//import app.server.Server;
//import app.ui.RoundTextField;
//
//import javax.swing.*;
//import javax.swing.border.EmptyBorder;
////import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.io.IOException;
//import java.net.URL;
//
//public class ClientServer {
//	public static void main(String [] args){
//
//		Object[] selectioValues = { "Server","Client"};
//		String initialSection = "Server";
//
//		Object selection = JOptionPane.showInputDialog(null, "Login as : ", "Management System", JOptionPane.QUESTION_MESSAGE, null, selectioValues, initialSection);
//
//		if(selection.equals("Server")){
//		   String[] arguments = new String[] {};
//		   System.out.println("Server");
//			try {
//				Server server = new Server();
//				server.start();
//			} catch (IOException e) {
//				throw new RuntimeException(e);
//			}
//		}else if(selection.equals("Client")){
//			HomeUpdate.main(args);
//		}
//	}
//
//	public static class Home extends JFrame {
//
//		private JPanel contentPane;
//		private JTextField tfHostname;
//		private JTextField tfPort;
//
//		/**
//		 * Launch the application.
//		 */
//		public static void main(String[] args) {
//			EventQueue.invokeLater(new Runnable() {
//				public void run() {
//					try {
//						Home frame = new Home();
//						frame.setVisible(true);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//			});
//		}
//
//		/**
//		 * Create the frame.
//		 */
//		public Home() {
//			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//			setBounds(100, 100, 450, 300);
//			contentPane = new JPanel();
//			contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
//
//			setContentPane(contentPane);
//			SpringLayout sl_contentPane = new SpringLayout();
//			contentPane.setLayout(sl_contentPane);
//
//			Label title = new Label("Student Management System");
//			sl_contentPane.putConstraint(SpringLayout.NORTH, title, 10, SpringLayout.NORTH, contentPane);
//			sl_contentPane.putConstraint(SpringLayout.WEST, title, 24, SpringLayout.WEST, contentPane);
//			sl_contentPane.putConstraint(SpringLayout.EAST, title, -21, SpringLayout.EAST, contentPane);
//			title.setForeground(new Color(60, 179, 113));
//			title.setFont(new Font("JetBrains Mono", Font.BOLD | Font.ITALIC, 24));
//			title.setAlignment(Label.CENTER);
//			contentPane.add(title);
//
//			Label subTitle = new Label("Connect To Server");
//			sl_contentPane.putConstraint(SpringLayout.NORTH, subTitle, 47, SpringLayout.NORTH, contentPane);
//			sl_contentPane.putConstraint(SpringLayout.WEST, subTitle, 116, SpringLayout.WEST, contentPane);
//			sl_contentPane.putConstraint(SpringLayout.EAST, subTitle, -116, SpringLayout.EAST, contentPane);
//			subTitle.setAlignment(Label.CENTER);
//			subTitle.setFont(new Font("JetBrains Mono", Font.BOLD | Font.ITALIC, 15));
//			contentPane.add(subTitle);
//
//			JLabel hostLabel = new JLabel();
//			sl_contentPane.putConstraint(SpringLayout.WEST, hostLabel, 0, SpringLayout.WEST, title);
//			try {
//				URL url = new URL(ClassLoader.getSystemClassLoader().getResource("cloud.png").toString());
//				hostLabel.setIcon(new ImageIcon(url));
//				hostLabel.setText("Hostname");
//				hostLabel.setFont(new Font("JetBrains Mono", Font.ITALIC, 12));
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			contentPane.add(hostLabel);
//
//			tfHostname = new RoundTextField("", 20);
//			sl_contentPane.putConstraint(SpringLayout.NORTH, hostLabel, 3, SpringLayout.NORTH, tfHostname);
//			sl_contentPane.putConstraint(SpringLayout.EAST, hostLabel, -6, SpringLayout.WEST, tfHostname);
//			sl_contentPane.putConstraint(SpringLayout.NORTH, tfHostname, 17, SpringLayout.SOUTH, subTitle);
//			sl_contentPane.putConstraint(SpringLayout.WEST, tfHostname, 124, SpringLayout.WEST, contentPane);
//			sl_contentPane.putConstraint(SpringLayout.EAST, tfHostname, -30, SpringLayout.EAST, contentPane);
//			contentPane.add(tfHostname);
//			tfHostname.setColumns(10);
//
//			JLabel portLabel = new JLabel();
//			sl_contentPane.putConstraint(SpringLayout.WEST, portLabel, 0, SpringLayout.WEST, title);
//			portLabel.setText("Port");
//			portLabel.setFont(new Font("JetBrains Mono", Font.ITALIC, 12));
//			try {
//				URL url = new URL(ClassLoader.getSystemClassLoader().getResource("doorway.png").toString());
//				portLabel.setIcon(new ImageIcon(url));
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			contentPane.add(portLabel);
//
//			tfPort = new RoundTextField("", 20);
//			sl_contentPane.putConstraint(SpringLayout.NORTH, portLabel, 3, SpringLayout.NORTH, tfPort);
//			sl_contentPane.putConstraint(SpringLayout.EAST, portLabel, -31, SpringLayout.WEST, tfPort);
//			sl_contentPane.putConstraint(SpringLayout.NORTH, tfPort, 134, SpringLayout.NORTH, contentPane);
//			sl_contentPane.putConstraint(SpringLayout.SOUTH, tfHostname, -20, SpringLayout.NORTH, tfPort);
//			sl_contentPane.putConstraint(SpringLayout.WEST, tfPort, 124, SpringLayout.WEST, contentPane);
//			sl_contentPane.putConstraint(SpringLayout.EAST, tfPort, -144, SpringLayout.EAST, contentPane);
//			tfPort.setColumns(10);
//			contentPane.add(tfPort);
//
//			JButton btnCancel = new JButton("Cancel");
//			// add action cancel
//			btnCancel.addActionListener(new ActionListener() {
//				public void actionPerformed(ActionEvent e) {
//					System.exit(0);
//				}
//			});
//			sl_contentPane.putConstraint(SpringLayout.SOUTH, tfPort, -25, SpringLayout.NORTH, btnCancel);
//			sl_contentPane.putConstraint(SpringLayout.NORTH, btnCancel, 184, SpringLayout.NORTH, contentPane);
//			contentPane.add(btnCancel);
//
//			JButton btnConnect = new JButton("Connect");
//			// add action connect to server
//			btnConnect.addActionListener(new ActionListener() {
//
//				public void actionPerformed(ActionEvent e) {
//				}
//			});
//			sl_contentPane.putConstraint(SpringLayout.EAST, btnConnect, -51, SpringLayout.EAST, contentPane);
//			sl_contentPane.putConstraint(SpringLayout.EAST, btnCancel, -26, SpringLayout.WEST, btnConnect);
//			sl_contentPane.putConstraint(SpringLayout.NORTH, btnConnect, 0, SpringLayout.NORTH, btnCancel);
//			contentPane.add(btnConnect);
//
//
//		}
//	}
//}
