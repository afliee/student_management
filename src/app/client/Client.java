package app.client;

import java.io.IOException;
import java.net.Socket;

public class Client {
    private String hostname;
    private int port;
    private int serverPort;
    private Socket socket;
    public Client(String hostname, int port, int serverPort) {
        this.hostname = hostname;
        this.port = port;
        this.serverPort = serverPort;
    }


    public boolean start() {
        try {
            if (this.port == this.serverPort) {
                connect();
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void connect() throws IOException {
        System.out.println("Connecting to " + hostname + ":" + port);
        socket = new Socket(hostname, port);
        System.out.println("Connected to " + socket.getInetAddress().getHostName());
    }
}
