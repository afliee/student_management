package app.server;

import app.db.Database;

import java.io.DataInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static final int PORT = 8080;
    private static final int BACKLOG = 100;
    private ServerSocket server;
    private Socket socket;
    private static Server instance = null;

    private HashMap<String ,String> contrains = new HashMap<>();

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
                receive();
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
//            change type here soon
            Database database = new Database(hostname, username, password, databaseName);
            return database.connect() != null;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void addContrains(String key, String value) {
        contrains.put(key, value);
    }

    public void receive() {
        try {
            InputStream inputStream = socket.getInputStream();
            DataInputStream dataInputStream = new DataInputStream(inputStream);

            String message = dataInputStream.readUTF();
            System.out.println("The message sent from the socket was: " + message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
