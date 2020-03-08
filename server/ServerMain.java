package server;

import java.awt.*;

public class ServerMain {
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                ServerGUI frame = new ServerGUI();
                ServerController c = new ServerController(frame);
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
