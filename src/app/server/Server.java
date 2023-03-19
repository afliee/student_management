package app.server;

import app.db.Database;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static final int PORT = 8080;
    private static final int BACKLOG = 100;
    private ServerSocket server;
    private Socket socket;
    private static Server instance = null;

    private Server() throws IOException {
        this.server = new ServerSocket(PORT, BACKLOG);
    }

    public static synchronized Server getInstance() throws IOException {
        if (instance == null) {
            instance = new Server();
        }
        return instance;
    }

    public void start() throws IOException {
        System.out.println("Server started on port " + PORT);
        while (true) {
            try {
                waitCall();
            } catch (Exception err) {
                err.printStackTrace();
            }
        }
    }

    public int getPort() {
        return PORT;
    }
    public void waitCall() throws IOException {
        socket = server.accept();
        System.out.println("Connection accepted from " +
                socket.getInetAddress().getHostName());
    }

    public void close() {
        try {
            server.close();
            System.out.println("Server closed");
        } catch (Exception e) {

        }
    }

    public boolean connectDB(String hostname, String username, String password, String databaseName) {
        try {
            Database database = new Database(hostname, username, password, databaseName);
            return database.connect();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
