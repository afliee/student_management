package app.service;

import app.entity.Student;
import app.server.Server;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class StudentService {
    private static StudentService instance = null;
    private Connection connection;
    private PreparedStatement preparedStatement;
    private StudentService() throws IOException {
        this.connection = Server.getInstance().getConnection();
    }

    public static StudentService getInstance() throws IOException {
        if (instance == null) {
            instance = new StudentService();
        }
        return instance;
    }

    public void save(Student student) throws SQLException {
        try {
            this.connection.setAutoCommit(false);
            String query = "INSERT INTO `students` VALUES (?, ?, ?, ?, ?)";
            preparedStatement = this.connection.prepareStatement(query);

            preparedStatement.setString(1, student.getId());
            preparedStatement.setString(2, student.getName());
            preparedStatement.setDouble(3, student.getMathScore());
            preparedStatement.setDouble(4, student.getLiteratureScore());
            preparedStatement.setDouble(5, student.getEnglishScore());

            preparedStatement.executeLargeUpdate();

            this.connection.commit();
            this.connection.setAutoCommit(true);
        } catch (SQLException ex) {
            ex.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException exRollBack) {
                exRollBack.printStackTrace();
            }
        }
    }

    public int count() {
        int record = 0;
        try {
//            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement("SELECT count(mssv) from students");
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                record = rs.getInt("count(mssv)");
            }
//
//            connection.commit();
//            connection.setAutoCommit(true);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException exRollBack) {
                exRollBack.printStackTrace();
            }
        }
        return record;
    }

    public Student findOne(String id) {
        try {

            String query = "SELECT * FROM `students` WHERE `mssv` = ? LIMIT 1";
            this.preparedStatement = this.connection.prepareStatement(query);

            preparedStatement.setString(1, id);

            ResultSet resultSet = this.preparedStatement.executeQuery();
            if (!resultSet.isBeforeFirst()) {
                return null;
            }
            Student student = new Student();
            while (resultSet.next()) {
                student.setId(resultSet.getString("mssv"));
                student.setName(resultSet.getString("name"));
                student.setMathScore(resultSet.getDouble("math_score"));
                student.setLiteratureScore(resultSet.getDouble("literure_score"));
                student.setEnglishScore(resultSet.getDouble("eng_score"));
            }
            System.out.println("Student exist : " + student.toString());
            return student;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public ArrayList<Student> findAll(int page, int pageSize) {
        ArrayList<Student> students = new ArrayList<>();

        if (count() == 0) {
            return students;
        }

        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM students limit ?,?");
            preparedStatement.setInt(1, pageSize * (page - 1));
            preparedStatement.setInt(2, pageSize);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Student p = new Student();
                p.setId(rs.getString("mssv"));
                p.setName(rs.getString("name"));
                p.setMathScore(rs.getDouble("math_score"));
                p.setLiteratureScore(rs.getDouble("literure_score"));
                p.setEnglishScore(rs.getDouble("eng_score"));
                students.add(p);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException exRollBack) {
                exRollBack.printStackTrace();
            }
        }
        return students;
    }

    public ArrayList<Student> search(String keyword) throws IOException {
        ArrayList<Student> students = new ArrayList<>();

        if (count() == 0) {
            return students;
        }

        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM students WHERE mssv LIKE ? OR name LIKE ?");
            preparedStatement.setString(1, "%" + keyword + "%");
            preparedStatement.setString(2, "%" + keyword + "%");
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Student p = new Student();
                p.setId(rs.getString("mssv"));
                p.setName(rs.getString("name"));
                p.setMathScore(rs.getDouble("math_score"));
                p.setLiteratureScore(rs.getDouble("literure_score"));
                p.setEnglishScore(rs.getDouble("eng_score"));
                students.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return students;
    }

    public void delete(String id) throws SQLException {
        try {
            String query = "DELETE FROM `students` WHERE mssv = ?";
            preparedStatement = this.connection.prepareStatement(query);

            preparedStatement.setString(1, id);

            preparedStatement.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void update(String id, String name, String mathScore, String literatureScore, String englishScore) {
        try {
            this.connection.setAutoCommit(false);

            String query = "UPDATE `students` SET `mssv`= ?, `name` = ?, `math_score` = ?, `literure_score` = ?, `eng_score` = ? WHERE `mssv` = ?";

            this.preparedStatement = this.connection.prepareStatement(query);
            preparedStatement.setString(1, id);
            preparedStatement.setString(2, name);
            preparedStatement.setDouble(3, Double.parseDouble(mathScore));
            preparedStatement.setDouble(4, Double.parseDouble(literatureScore));
            preparedStatement.setDouble(5, Double.parseDouble(englishScore));
            preparedStatement.setString(6, id);

            preparedStatement.executeLargeUpdate();

            this.connection.commit();
            this.connection.setAutoCommit(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
