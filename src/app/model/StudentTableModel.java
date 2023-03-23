package app.model;

import app.entity.Student;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class StudentTableModel extends AbstractTableModel {
    private ArrayList<Student> students = new ArrayList<>();
    private String[] HEADER = new String[] {
            "ID",
            "Name",
            "Math Score",
            "Literure Score",
            "English Score",
            "Average Score"
    };

    public StudentTableModel(ArrayList<Student> students) {
        this.students = students;
    }

    public void setStudents(ArrayList<Student> students) {
        this.students = students;
    }

    public void save(Student student) {
        students.add(student);
        fireTableRowsInserted(getRowCount() - 1, getRowCount() - 1);
    }

    public void update(Student student, int index) {
        students.set(index, student);
        fireTableDataChanged();
    }

    public void delete(int index) {
        students.remove(index);
        fireTableRowsDeleted(index, index);
    }

    public void edit(int index, Student product) {
        students.set(index, product);
        fireTableRowsUpdated(index, index);
    }

    public String getColumnName(int column) {
        return HEADER[column];
    }

    @Override
    public int getRowCount() {
        return students.size();
    }

    @Override
    public int getColumnCount() {
        return HEADER.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Student student = students.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return student.getId();
            case 1:
                return student.getName();
            case 2:
                return student.getMathScore();
            case 3:
                return student.getLiteratureScore();
            case 4:
                return student.getEnglishScore();
            case 5:
                return student.getAverageScore();
        }
        return null;
    }
}
