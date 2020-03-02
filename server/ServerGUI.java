package server;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class ServerGUI extends JFrame {
	private static final long serialVersionUID = 1L;
	private ServerGUI guiReference = this;
	protected Server server;
	private JPanel contentPane;
	private JTextArea textArea;
    private JLabel lblIndirizzoIpServer;
    private JLabel lblAddr;
	private JButton exitBtn;

	protected JButton getExitBtn() {
		return exitBtn;
	}

	public ServerGUI() throws UnknownHostException {
		setBounds(100, 100, 512, 497);

		ImageIcon img = new ImageIcon("ico.png");
		setIconImage(img.getImage());
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		lblAddr = new JLabel("IP Address:");
		lblAddr.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
		lblAddr.setBounds(22, 6, 172, 14);
		contentPane.add(lblAddr);

		InetAddress inetAddress = InetAddress.getLocalHost();
        String iA=inetAddress.getHostAddress();
		lblIndirizzoIpServer = new JLabel(iA);
		lblIndirizzoIpServer.setFont(new Font("Lucida Grande", Font.BOLD, 15));
		lblIndirizzoIpServer.setBounds(42, 26, 172, 14);
		contentPane.add(lblIndirizzoIpServer);

		exitBtn = new JButton("QUIT");
		exitBtn.setBounds(360, 16, 100, 30);
		contentPane.add(exitBtn);
		
		textArea = new JTextArea(160, 250);
		textArea.setBounds(22, 60, 458, 390);
		contentPane.add(textArea);
		textArea.setEditable(false);

		server  = new Server(guiReference);
    }
		
    protected void update(String msg) {
		SwingUtilities.invokeLater(() -> textArea.append(msg+"\n"));
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				ServerGUI frame = new ServerGUI();
				Controller c = new Controller(frame);
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
}
    