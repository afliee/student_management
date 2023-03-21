package app.client;

import app.ui.HintTextFieldUI;
import app.ui.RoundTextField;
import app.ui.KeepSortIconHeaderRenderer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Enumeration;

public class InforPanel extends JFrame {
    private JPanel panel;
    private JPanel header;
    private JLabel title;
    private JPanel inputs;
    private JPanel buttons;
    private JTextField tfId;
    private JTextField tfname;
    private JTextField tfMathscore;
    private JTextField tfLiterurescore;
    private JTextField tfEnglishscore;
    private JPanel left_inputs;
    private JButton button1;
    private JTextField tfFileinfor;
    private JButton btnDelete;
    private JButton btnUpdate;
    private JButton btnSubmit;
    private JButton btnClear;
    private JButton browseButton;
    private JTable tableDataset;
    private JButton nextButton;
    private JComboBox comboBox1;
    private JButton firstButton;
    private JButton previousButton;
    private JScrollPane scrollpanel;
    private JTextField tfSearch;
    private JButton btnSearch;
    private JTableHeader tableHeader;
    private String[] HEADER = new String[] {
            "ID",
            "Name",
            "Math Score",
            "Literure Score",
            "English Score"
    };

    public InforPanel() {
        super("InforPanel");
        setSize(new Dimension(500, 500));
        setContentPane(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        btnClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tfId.setText("");
                tfname.setText("");
                tfMathscore.setText("");
                tfLiterurescore.setText("");
                tfEnglishscore.setText("");
            }
        });
        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser(new File(".").getAbsoluteFile());
                fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
//                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                fileChooser.setMultiSelectionEnabled(false);
                fileChooser.setDialogTitle("Select a folder");
                int result = fileChooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    System.out.println(file.getAbsolutePath());
                    tfFileinfor.setText(file.getName());
                } else {
                    System.out.println("No file selected");
                }

            }
        });
        comboBox1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                init pagination
                System.out.println(comboBox1.getSelectedItem());
            }
        });
    }

    public static void main(String[] args) {
        InforPanel inforPanel = new InforPanel();
        inforPanel.setVisible(true);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        title = new JLabel("Student Record Management System");
        title.setBorder(new EmptyBorder(10, 50 ,0 ,0));

        tfId = new RoundTextField("", 15);
        tfId.setUI(new HintTextFieldUI("ID", true));

        tfname = new RoundTextField("", 15);
        tfname.setUI(new HintTextFieldUI("Name", true));

        tfMathscore = new RoundTextField("", 15);
        tfMathscore.setUI(new HintTextFieldUI("Math Score", true));

        tfLiterurescore = new RoundTextField("", 15);
        tfLiterurescore.setUI(new HintTextFieldUI("Literure Score", true));

        tfEnglishscore = new RoundTextField("", 15);
        tfEnglishscore.setUI(new HintTextFieldUI("English Score", true));

        String[][] rowData = new String[][] {
                {"1", "Manchester United", "28", "5", "5"},
                {"2", "Manchester City", "23", "9", "6"},
                {"3", "Chelsea", "22", "9", "7"},
                {"4", "Arsenal", "21", "10", "7"},
                {"1", "Manchester United", "28", "5", "5"},
                {"2", "Manchester City", "23", "9", "6"},
                {"1", "Manchester United", "28", "5", "5"},
                {"2", "Manchester City", "23", "9", "6"},
                {"3", "Chelsea", "22", "9", "7"},
                {"4", "Arsenal", "21", "10", "7"},
                {"1", "Manchester United", "28", "5", "5"},
                {"2", "Manchester City", "23", "9", "6"},
        };

        scrollpanel  = new JScrollPane();
        tableDataset = new JTable(rowData, HEADER);
        tableDataset.setFillsViewportHeight(true);
        tableDataset.setRowHeight(30);
        scrollpanel.add(tableDataset);
        scrollpanel.setViewportView(tableDataset);

        tableDataset.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tableDataset.setPreferredScrollableViewportSize(new Dimension(500, 70));
        tableDataset.setFillsViewportHeight(true);
        tableDataset.setRowHeight(30);
        tableDataset.setRowSelectionAllowed(true);
        tableDataset.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableDataset.setShowGrid(true);

        JTableHeader header = tableDataset.getTableHeader();
        header.setDefaultRenderer(new KeepSortIconHeaderRenderer(header.getDefaultRenderer()));

        comboBox1 = new JComboBox();
        comboBox1.addItem("5");
        comboBox1.addItem("10");
        comboBox1.addItem("25");
        comboBox1.addItem("50");

        tfFileinfor = new JTextField(15);
        tfFileinfor.setEditable(false);

        tfSearch = new RoundTextField("", 15);
        tfSearch.setUI(new HintTextFieldUI("Search", true));
    }

    private void autoResizeColumn(JTable jTable1) {
        JTableHeader header = jTable1.getTableHeader();
        int rowCount = jTable1.getRowCount();

        final Enumeration columns = jTable1.getColumnModel().getColumns();
        while (columns.hasMoreElements()) {
            TableColumn column = (TableColumn) columns.nextElement();
            int col = header.getColumnModel().getColumnIndex(column.getIdentifier());
            int width = (int) jTable1.getTableHeader().getDefaultRenderer()
                    .getTableCellRendererComponent(jTable1, column.getIdentifier(), false, false, -1, col).getPreferredSize().getWidth();

            for (int row = 0; row < rowCount; row++) {
                int preferedWidth = (int) jTable1.getCellRenderer(row, col).getTableCellRendererComponent(jTable1,
                        jTable1.getValueAt(row, col), false, false, row, col).getPreferredSize().getWidth();
                width = Math.max(width, preferedWidth);
            }
            header.setResizingColumn(column); // this line is very important
            column.setWidth(width + jTable1.getIntercellSpacing().width);
        }
    }
}
