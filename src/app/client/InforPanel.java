package app.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class InforPanel extends JFrame {
    private JPanel panel;
    private JButton button1;

    public InforPanel() {
        super("InforPanel");
        setSize(new Dimension(500, 500));
        setContentPane(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser(new File(".").getAbsoluteFile());
                fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                fileChooser.setMultiSelectionEnabled(false);
                fileChooser.setDialogTitle("Select a folder");
                int result = fileChooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    System.out.println(file.getAbsolutePath());
                } else {
                    System.out.println("No file selected");
                }
            }
        });
    }

    public static void main(String[] args) {
        InforPanel inforPanel = new InforPanel();
        inforPanel.setVisible(true);
    }
}
