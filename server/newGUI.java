package server;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class newGUI extends JFrame{
    private static final long serialVersionUID = 1L;
    //protected ServerGUI guiReference = this;
    private JLabel lblAddr;
    private JLabel lblIndirizzoIpServer;
    private JScrollPane actions;
    private JPanel contentPane;
    private JTextArea textArea;

    public newGUI() throws UnknownHostException {
        setBounds(100, 100, 512, 497);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        lblAddr = new JLabel("Indirizzo IP:");
        lblAddr.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
        lblAddr.setBounds(22, 6, 172, 14);
        contentPane.add(lblAddr);
        InetAddress inetAddress = InetAddress.getLocalHost();
        String iA = inetAddress.getHostAddress();
        lblIndirizzoIpServer = new JLabel(iA);
        lblIndirizzoIpServer.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
        lblIndirizzoIpServer.setBounds(22, 25, 172, 14);
        contentPane.add(lblIndirizzoIpServer);

        textArea = new JTextArea(160, 250);
        textArea.setBounds(20, 50, 470, 400);
        textArea.setEditable(false);

        actions = new JScrollPane(textArea);
        actions.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        actions.setBounds(20, 300, 470, 150);
        //server = new Server(guiReference);
    }

    public void update(String msg) {
        SwingUtilities.invokeLater(() -> textArea.append(msg+"\n"));
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                newGUI frame = new newGUI();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


}
