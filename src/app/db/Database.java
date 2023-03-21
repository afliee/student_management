package app.db;

import javax.xml.transform.Result;
import java.sql.*;

public class Database {
    private String hostname;
    private String userName;
    private String password;
    private String databaseName;
    private Connection connection;
    private Statement statement;
    private boolean is_connected;
    public Database(String hostname, String userName, String password, String databaseName) {
        this.hostname = hostname;
        this.userName = userName;
        this.password = password;
        this.databaseName = databaseName;
    }

    public Connection getConnection() {
        return connection;
    }
    public Connection connect() {
        if (is_connected) {
            return this.connection;
        } else {
            try {
                this.connection = DriverManager.getConnection("jdbc:mysql://" + hostname + ":3306/" + databaseName, userName, password);
                this.statement = connection.createStatement();

                statement.executeUpdate("SET NAMES 'utf8'");
                statement.executeUpdate("SET CHARACTER SET 'utf8'");
                statement.executeUpdate("SET SESSION collation_connection = 'utf8_general_ci'");
                statement.executeUpdate("SET SESSION sql_mode = 'NO_ENGINE_SUBSTITUTION'");

                System.out.println("Connected to database " + databaseName);
                is_connected = true;
                return this.connection;
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public ResultSet draw(String query) {
        try {
            boolean status = this.statement.execute(query);
            if (status) {
                ResultSet resultSet = this.statement.getResultSet();
                return resultSet;
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
