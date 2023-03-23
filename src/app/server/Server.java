package app.server;

import app.db.Database;
import app.entity.Student;
import app.midleware.DES;
import app.service.StudentService;

import java.io.*;
import java.sql.Connection;
import java.util.HashMap;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static final int PORT = 8080;
    private static final int BACKLOG = 100;
    private ServerSocket server;
    private Socket socket;
    private static Server instance = null;
    private Connection connection;
    private StudentService studentService;
    private HashMap<String, String> contrains = new HashMap<>();

    private Server() throws IOException {
        this.server = new ServerSocket(PORT, BACKLOG);
//        studentService = StudentService.getInstance();
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

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }

    public void waitCall() {
        try {
            socket = server.accept();
            System.out.println("Connection accepted from " +
                    socket.getInetAddress().getHostName());
            new EchoClientHandler(socket).start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
            String hostname = new String(contrains.get("HOSTNAME"));
            String username = new String(contrains.get("USERNAME"));
            String password = new String(contrains.get("PASSWORD"));
            String databaseName = new String(contrains.get("SERVER_NAME"));
            System.out.println(contrains.toString());
            System.out.println("Server perform connect : " + hostname + " " + username + " " + password + " " + databaseName);
            Database database = new Database(hostname, username, password, databaseName);
            Connection connect = database.connect();
            setConnection(connect);
            return getConnection() != null;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void addContrains(String key, String value) {
        contrains.put(key, value);
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
//                if (inputLine.equals("submit")) {
//                    boolean is_success = submit();
//                    if (is_success) {
//                        out.println("true");
//                    } else {
//                        out.println("false");
//                    }
//                }
                switch (inputLine) {
                    case "submit": {
                        boolean is_success = submit();
                        if (is_success) {
                            out.println("true");
                        } else {
                            out.println("false");
                        }
                        break;
                    }
                    case "delete": {
                        boolean is_deleted = delete();
                        if (is_deleted) {
                            out.println("true");
                        } else {
                            out.println("false");
                        }
                        break;
                    }
                    case "update": {
                        boolean is_updated = update();
                        if (is_updated) {
                            out.println("true");
                        } else {
                            out.println("false");
                        }
                        break;
                    }
                    case "file": {
                        String infors = hanlde_file();
                        if (infors != null) {
                            String[] args = infors.split(" ");
                            Student student = new Student(args[0], args[1], Double.parseDouble(args[2]), Double.parseDouble(args[3]), Double.parseDouble(args[4]));
                            out.println(student.toCSV());
//                            out.println("true");
                        } else {
                            out.println("false");
                        }
                        break;
                    }
                }

                int prefixPosition = inputLine.indexOf("->");
                if (prefixPosition != -1) {
                    out.println(inputLine);

                    String prefix = inputLine.substring(0, prefixPosition);
                    String value = inputLine.substring(prefixPosition + 2);
                    System.out.println(prefix);
                    System.out.println(value);
                    addContrains(prefix, value);
                }
            }
            try {
                in.close();
                out.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                clientSocket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public void close() {
            try {
                in.close();
                out.close();
                clientSocket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean submit() {
        try {
            String id = DES.getInstance().decrypt(contrains.get("ID"));
            String name = DES.getInstance().decrypt(contrains.get("NAME"));
            String mathScore = DES.getInstance().decrypt(contrains.get("MATH"));
            String literatureScore = DES.getInstance().decrypt(contrains.get("LITERURE"));
            String englishScore = DES.getInstance().decrypt(contrains.get("ENGLISH"));

            System.out.println("Server perform submit : " + id + " " + name + " " + mathScore + " " + literatureScore + " " + englishScore);
            contrains.clear();

            Student student = new Student(id, name, Double.parseDouble(mathScore), Double.parseDouble(literatureScore), Double.parseDouble(englishScore));

            studentService = StudentService.getInstance();
            if (studentService.findOne(id) != null) {
                return false;
            }
            studentService.save(student);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean delete() {
        try {
            String id = DES.getInstance().decrypt(contrains.get("ID"));
            contrains.clear();
            studentService = StudentService.getInstance();
            studentService.delete(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean update() {
        try {
            DES instance = DES.getInstance();

            String id = instance.decrypt(contrains.get("ID"));
            String name = instance.decrypt(contrains.get("NAME"));
            String mathScore = instance.decrypt(contrains.get("MATH"));
            String literatureScore = instance.decrypt(contrains.get("LITERATURE"));
            String englishScore = instance.decrypt(contrains.get("ENGLISH"));

            contrains.clear();

            StudentService studentService = StudentService.getInstance();
            if (studentService.findOne(id) == null) {
                return false;
            }
            studentService.update(id, name, mathScore, literatureScore, englishScore);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private String hanlde_file() {
        try {
            String path = DES.getInstance().decrypt(contrains.get("PATH"));
            File file = new File(path);

            FileReader reader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(reader);

            int row = 0;
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                row++;
                if (row == 2) {
                    return null;
                }

                System.out.println(line);
                break;
            }
            return line;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

