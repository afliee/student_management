package app.server;

import app.db.Database;

import java.io.*;
import java.util.HashMap;
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
//                receive();
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
        new EchoClientHandler(socket).start();

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

    public boolean connectDB() {
        try {
//            change type here soon

            String hostname = contrains.get("HOSTNAME");
            String username = contrains.get("USERNAME");
            String password = contrains.get("PASSWORD");
            String databaseName = contrains.get("SERVER_NAME");

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


    private class EchoClientHandler extends Thread {
        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;

        public EchoClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                in = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            String inputLine;
            while (true) {
                try {
                    if (!((inputLine = in.readLine()) != null)) break;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                System.out.println(inputLine);
                out.println(inputLine);
                String[] arr = inputLine.split(":");
                if (contrains.containsKey(arr[0])) {
                    System.out.println("Contrains: " + contrains.get(arr[0]));
                } else {
                    out.println("No contrains");
                    addContrains(arr[0], arr[1]);
                }
            }

            try {
                in.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            out.close();
            try {
                clientSocket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

