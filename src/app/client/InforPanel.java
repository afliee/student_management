package app.client;

import app.HomeUpdate;
import app.entity.Student;
import app.midleware.DES;
import app.model.StudentTableModel;
import app.service.StudentService;
import app.ui.HintTextFieldUI;
import app.ui.RoundTextField;
import app.ui.KeepSortIconHeaderRenderer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

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
    private JTextField tfFileinfor;
    private JButton btnDelete;
    private JButton btnUpdate;
    private JButton btnSubmit;
    private JButton btnClear;
    private JButton browseButton;
    private JTable tableDataset;
    private JButton nextButton;
    private JComboBox comboBox;
    private JButton firstButton;
    private JButton previousButton;
    private JScrollPane scrollpanel;
    private JTextField tfSearch;
    private JButton btnSearch;
    private JLabel lbStatus;
    private JTextField tfAverage;
    private JLabel lbNotification;
    private JCheckBox unBlockCheckBox;
    private JTableHeader tableHeader;
    private StudentService studentService;
    private StudentTableModel studentTableModel;
    Integer page = 1;
    Integer rowCountPerPage = 5;
    Integer totalPage = 1;
    Integer totalData = 0;
    public InforPanel() {
        super("InforPanel");
        setSize(new Dimension(500, 500));
        setContentPane(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        btnClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearText();
            }
        });

        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser(new File(".").getAbsoluteFile());
                fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
                fileChooser.setMultiSelectionEnabled(false);
                fileChooser.setDialogTitle("Select a folder");
                int result = fileChooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    System.out.println(file.getAbsolutePath());
                    tfFileinfor.setText(file.getName());

                    Client client = HomeUpdate.getClient();
                    try {
                        String pathEncrypted = DES.getInstance().encrypt(file.getAbsolutePath());
                        client.sendMessage("PATH->"+pathEncrypted);
                        String studentToCSV = client.sendMessage("file");
                        System.out.println(studentToCSV);

                        String[] inforFields = studentToCSV.split(",");
                        clearText();

                        List<String> infors = Arrays.asList(inforFields);

                        String id = infors.get(0);
                        String name = infors.get(1);
                        Double mathScore = Double.parseDouble(infors.get(2));
                        Double literatureScore = Double.parseDouble(infors.get(3));
                        Double englishScore = Double.parseDouble(infors.get(3));
                        Double averageScore = Math.round((mathScore + literatureScore + englishScore ) / 3 * 100) / 100.0;

                        tfId.setText(id);
                        tfname.setText(name);
                        tfMathscore.setText(mathScore.toString());
                        tfLiterurescore.setText(literatureScore.toString());
                        tfEnglishscore.setText(englishScore.toString());
                        tfAverage.setText(averageScore.toString());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else {
                    System.out.println("No file selected");
                }

            }
        });

        comboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // init pagination
                initPagination();
                System.out.println(comboBox.getSelectedItem());
            }
        });

        comboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    rowCountPerPage = Integer.parseInt(comboBox.getSelectedItem().toString());
                    initPagination();
                }
            }
        });

        initPagination();

        previousButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (page > 1) {
                    page--;
                    initPagination();
                }
            }
        });

        firstButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                page = 1;
                initPagination();
            }
        });

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (page < totalPage) {
                    page++;
                    initPagination();
                }
            }
        });

        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String keyword = tfSearch.getText();
                if (keyword.isEmpty()) {
                    initPagination();
                } else {
                    try {
                        ArrayList<Student> students = studentService.search(keyword);
                        studentTableModel = new StudentTableModel(students);
                        tableDataset.setModel(studentTableModel);
                        tableDataset.repaint();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            }
        });

        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = tfId.getText().toString();
                String name = tfname.getText().toString();
                String mathScore = tfMathscore.getText().toString();
                String literarureScore = tfLiterurescore.getText().toString();
                String englishScore = tfEnglishscore.getText().toString();

                if (id.isEmpty() || name.isEmpty() || mathScore.isEmpty() || literarureScore.isEmpty() || englishScore.isEmpty()) {
                    JOptionPane.showConfirmDialog(null, "Please, must be fill all fields");
                    return;
                }

                try {
                    Client client = HomeUpdate.getClient();
                    DES instance = DES.getInstance();
                    String idEncrypted = instance.encrypt(id);
                    String nameEncrypted = instance.encrypt(name);
                    String mathScoreEncrypted = instance.encrypt(mathScore);
                    String literatureScoreEncrypted = instance.encrypt(literarureScore);
                    String engScoreEncrypted = instance.encrypt(englishScore);

                    client.sendMessage("ID->"+idEncrypted);
                    client.sendMessage("NAME->"+nameEncrypted);
                    client.sendMessage("MATH->"+mathScoreEncrypted);
                    client.sendMessage("LITERATURE->"+literatureScoreEncrypted);
                    client.sendMessage("ENGLISH->"+engScoreEncrypted);
                    String result = client.sendMessage("update");

                    boolean is_updated = Boolean.parseBoolean(result);
                    if (is_updated) {
                        lbNotification.setText("Update Successfully");
                        lbNotification.setForeground(Color.decode("#539165"));
                        initPagination();
                        clearText();
                    } else {
                        lbNotification.setText("Update failure");
                        lbNotification.setForeground(Color.RED);
                        clearText();
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });
        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = tableDataset.getSelectedRow();
                if (row == -1) {
                    JOptionPane.showMessageDialog(null, "Please select a row to delete");
                } else {
                    String id = tableDataset.getValueAt(row, 0).toString();
                    int confirm = JOptionPane.showConfirmDialog(null, "Are you sure to delete this student?");
                    if (confirm == JOptionPane.YES_OPTION) {
                        try {
                            String idEncrypt = DES.getInstance().encrypt(id);
                            Client client = HomeUpdate.getClient();
                            client.sendMessage("ID->"+ idEncrypt);
                            String result = client.sendMessage("delete");
                            boolean is_deleted = Boolean.parseBoolean(result);
                            if (is_deleted) {
                                lbNotification.setText("Delete successfully");
                                lbNotification.setForeground(Color.decode("#539165"));
                                toggleEditable(false);
                                initPagination();
                                clearText();
                            } else {
                                lbNotification.setText("Delete failed");
                                lbNotification.setForeground(Color.RED);
                                clearText();
                            }
                        } catch (Exception ioException) {
                            ioException.printStackTrace();
                        }
                    }
                }
            }
        });
        btnSubmit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = tfId.getText();
                String name = tfname.getText();
                String mathScore = tfMathscore.getText();
                String literureScore = tfLiterurescore.getText();
                String englishScore = tfEnglishscore.getText();
                String average = tfAverage.getText();
                if (id.isEmpty() || name.isEmpty() || mathScore.isEmpty() || literureScore.isEmpty() || englishScore.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please fill all the fields");
                } else {
                    int confirm = JOptionPane.showConfirmDialog(null, "Are you sure to submit this student?");
                    if (confirm == JOptionPane.YES_OPTION) {
                        try {
                            boolean is_valid = validateScore();
                            if (!is_valid) {
                                JOptionPane.showMessageDialog(null, "Please fill all the fields");
                                return;
                            }
                            Client client = HomeUpdate.getClient();

                            String idEncrypt = DES.getInstance().encrypt(id);
                            String nameEncrypt = DES.getInstance().encrypt(name);
                            String mathScoreEncrypt = DES.getInstance().encrypt(mathScore);
                            String literureScoreEncrypt = DES.getInstance().encrypt(literureScore);
                            String englishScoreEncrypt = DES.getInstance().encrypt(englishScore);

                            client.sendMessage("ID->"+ idEncrypt);
                            client.sendMessage("NAME->"+ nameEncrypt);
                            client.sendMessage("MATH->"+ mathScoreEncrypt);
                            client.sendMessage("LITERURE->"+ literureScoreEncrypt);
                            client.sendMessage("ENGLISH->"+ englishScoreEncrypt);
                            String result = client.sendMessage("submit");
                            System.out.println("Result : "+result);

                            boolean is_success = Boolean.parseBoolean(result);
                            if (is_success) {
                                lbNotification.setText("Submit successfully");
                                lbNotification.setForeground(Color.decode("#539165"));
                                initPagination();
                                clearText();
                            } else {
                                lbNotification.setText("Submit failed");
                                lbNotification.setForeground(Color.RED);
                                clearText();
                            }
//                            initPagination();
                        } catch (Exception ioException) {
                            ioException.printStackTrace();
                        }
                    }
                }
            }
        });

        tableDataset.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tableDataset.getSelectedRow();
                String id = tableDataset.getValueAt(row, 0).toString();
                String name = tableDataset.getValueAt(row, 1).toString();
                String mathScore = tableDataset.getValueAt(row, 2).toString();
                String literureScore = tableDataset.getValueAt(row, 3).toString();
                String englishScore = tableDataset.getValueAt(row, 4).toString();
                String average = tableDataset.getValueAt(row, 5).toString();
                tfId.setText(id);
                tfname.setText(name);
                tfMathscore.setText(mathScore);
                tfLiterurescore.setText(literureScore);
                tfEnglishscore.setText(englishScore);
                tfAverage.setText(average);
            }
        });

        unBlockCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (unBlockCheckBox.isSelected()) {
                    unBlockCheckBox.setText("Block");
                    toggleEditable(true);
                } else {
                    toggleEditable(false);
                    unBlockCheckBox.setText("UnBlock");
                }
            }
        });
        tableDataset.addComponentListener(new ComponentAdapter() {
        });
        tableDataset.addComponentListener(new ComponentAdapter() {
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

        lbStatus = new JLabel();
        lbNotification = new JLabel();

        // init text field custom
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

        tfAverage = new RoundTextField("", 15);
        tfAverage.setUI(new HintTextFieldUI("Average Score", true));
        tfAverage.setEditable(false);

        tfFileinfor = new JTextField(15);
        tfFileinfor.setEditable(false);

        tfSearch = new RoundTextField("", 15);
        tfSearch.setUI(new HintTextFieldUI("Search", true));

        // init buttons
        firstButton = new JButton("First");
        previousButton = new JButton("Previous");
        nextButton = new JButton("Next");

//        init combo box for pagination
        comboBox = new JComboBox();
        comboBox.addItem("5");
        comboBox.addItem("10");
        comboBox.addItem("25");
        comboBox.addItem("50");

        unBlockCheckBox = new JCheckBox();

        scrollpanel  = new JScrollPane();
        try {
            studentService = StudentService.getInstance();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        tableDataset = new JTable();

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
        tableDataset.setTableHeader(header);

        initPagination();
        toggleEditable(false);

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

    private void initPagination() {
        totalData = studentService.count();
        rowCountPerPage = Integer.valueOf(comboBox.getSelectedItem().toString());
        Double totalPageD = Math.ceil(totalData.doubleValue() / rowCountPerPage.doubleValue());
        totalPage = totalPageD.intValue();

        if (this.page == null) {
            this.page = 1;
        }

        if (page > totalPage) {
            page = 1;
        }

        if (page.equals(1)) {
            firstButton.setEnabled(false);
            previousButton.setEnabled(false);
            nextButton.setEnabled(true);
        } else {
            firstButton.setEnabled(true);
            previousButton.setEnabled(true);
        }

        if (page.equals(totalPage)) {
            nextButton.setEnabled(false);
        } else {
            nextButton.setEnabled(true);
        }

        ArrayList<Student> students = studentService.findAll(page, rowCountPerPage);
        System.out.println(students.toString());
        StudentTableModel studentTableModel = new StudentTableModel(students);

        tableDataset.setModel(studentTableModel);

        lbStatus.setText("Page " + page + " of " + totalPage);
    }

    private void clearText() {
        tfId.setText("");
        tfname.setText("");
        tfMathscore.setText("");
        tfLiterurescore.setText("");
        tfEnglishscore.setText("");
        tfAverage.setText("");
    }

    private boolean validateScore() {
//        check three field score is numberic
        String mathScore = tfMathscore.getText();
        String literureScore = tfLiterurescore.getText();
        String englishScore = tfEnglishscore.getText();

        if (!mathScore.matches("\\d+(\\.\\d+)?")) {
            lbNotification.setText("Math score must be numberic");
            lbNotification.setForeground(Color.RED);
            return false;
        }

        if (!literureScore.matches("\\d+(\\.\\d+)?")) {
            lbNotification.setText("Literure score must be numberic");
            lbNotification.setForeground(Color.RED);
            return false;
        }

        if (!englishScore.matches("\\d+(\\.\\d+)?")) {
            lbNotification.setText("English score must be numberic");
            lbNotification.setForeground(Color.RED);
            return false;
        }
        return true;
    }

    public void toggleEditable(boolean is_edit) {
        tfId.setEditable(is_edit);
        tfname.setEditable(is_edit);
        tfMathscore.setEditable(is_edit);
        tfLiterurescore.setEditable(is_edit);
        tfEnglishscore.setEditable(is_edit);
    }
}
