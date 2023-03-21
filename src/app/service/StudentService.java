package app.service;

import app.db.Database;

import java.sql.Connection;

public class StudentService {
    private static StudentService instance = null;
    private Connection connection;
    private StudentService() {

    }

    public StudentService getInstance() {
        if (instance == null) {
            instance = new StudentService();
        }
        return instance;
    }
}
