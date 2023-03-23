import app.HomeUpdate;
import app.server.Server;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        String isVisible;
        HomeUpdate home = new HomeUpdate();

        if (args.length > 0) {
            isVisible = args[0];
            if (Boolean.parseBoolean(isVisible)) {
                home.setVisible(true);
            }
        } else {
            try {
                home.setVisible(true);
                Server server = home.getServer();
                server.start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}