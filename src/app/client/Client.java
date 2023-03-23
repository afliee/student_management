package app.client;

import java.io.*;
import java.net.Socket;

public class Client {
    private String hostname;
    private int port;
    private int serverPort;
    private Socket socket;
    private boolean is_started = false;
    private OutputStream outputStream;
    private DataOutputStream dataOutputStream;

    private PrintWriter out;
    private BufferedReader in;

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
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        System.out.println("Connected to " + socket.getInetAddress().getHostName());
    }

    public Client send(String message) throws IOException {
        dataOutputStream.writeUTF(message);
        return this;
    }

    public String sendMessage(String msg) {
        String resp = null;
        try {
            out.println(msg);
            resp = in.readLine();
            System.out.println("Client received: " + resp);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resp;
    }

    public void flush() throws IOException {
        dataOutputStream.flush();
    }

    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
