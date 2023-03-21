package app.client;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class Client {
    private String hostname;
    private int port;
    private int serverPort;
    private Socket socket;
    private boolean is_started = false;
    private OutputStream outputStream;
    private DataOutputStream dataOutputStream;

    public Client(String hostname, int port, int serverPort) {
        this.hostname = hostname;
        this.port = port;
        this.serverPort = serverPort;
    }


    public boolean start() {
        try {
            if (this.port == this.serverPort) {
                connect();
                outputStream = socket.getOutputStream();
                dataOutputStream = new DataOutputStream(outputStream);
                is_started = true;
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isStarted() {
        return is_started;
    }
    public void connect() throws IOException {
        System.out.println("Connecting to " + hostname + ":" + port);
        socket = new Socket(hostname, port);
        System.out.println("Connected to " + socket.getInetAddress().getHostName());
    }

    public void send(String message) throws IOException {
        dataOutputStream.writeUTF(message);
        dataOutputStream.flush();
    }
}
