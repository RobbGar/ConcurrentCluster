package client;
import javax.swing.*;

public class ClientMain {
    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                ClientGUI frame = new ClientGUI();
                new ClientController(frame);
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
