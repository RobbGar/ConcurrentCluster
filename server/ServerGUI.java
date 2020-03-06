package server;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.KeyEvent;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class ServerGUI extends JFrame {
	private static final long serialVersionUID = 1L;
	protected Server server;
	private JPanel contentPane;
	private JTextArea textArea;
	private JLabel lblIndirizzoIpServer;
	private JLabel lblAddr;


	public JMenuBar getMenuBarr() {
		return menuBar;
	}

	private JMenuBar menuBar;
	private JMenu file;
	private JMenu settings;
	private JMenu view;
	private JMenu info;
	private JMenuItem infoItem;
	private JMenuItem exit;
	private JMenuItem save;
	private JMenuItem load;
	private JMenuItem reset;
	private JMenuItem settingsItem;
	private ButtonGroup bg;
	private JRadioButtonMenuItem lightMode;
	private JRadioButtonMenuItem darkMode;

	/*Getters*/
	public JTextArea getTextArea() {
		return textArea;
	}

	public JLabel getLblIndirizzoIpServer() {
		return lblIndirizzoIpServer;
	}

	public JLabel getLblAddr() {
		return lblAddr;
	}

	public JMenuItem getInfoItem() {
		return infoItem;
	}

	public JMenuItem getExit() {
		return exit;
	}

	public JMenuItem getSave() {
		return save;
	}

	public JMenuItem getLoad() {
		return load;
	}

	public JMenuItem getReset() {
		return reset;
	}

	public JRadioButtonMenuItem getLightMode() {
		return lightMode;
	}

	public JRadioButtonMenuItem getDarkMode() {
		return darkMode;
	}


	public ServerGUI() throws UnknownHostException {
		setBounds(100, 100, 512, 497);
		getContentPane().setBackground(Color.WHITE);

		/*Beginning Menu Bar*/
		menuBar = new JMenuBar();
			//File menu with save load and reset options
		file = new JMenu("File");
		save = new JMenuItem("Save data", new ImageIcon("server/resources/save.png"));
		load = new JMenuItem("Load data", new ImageIcon("server/resources/load.png"));
		reset = new JMenuItem("Reset data", new ImageIcon("server/resources/reset.png"));

		exit = new JMenuItem("Exit", new ImageIcon("server/resources/exit.png"));
		file.setMnemonic(KeyEvent.VK_F);
		exit.setMnemonic(KeyEvent.VK_E);
		exit.setToolTipText("Exit application");

		file.add(save);
		file.add(load);
		file.add(reset);
		file.addSeparator();
		file.add(exit);

		menuBar.add(file);

		//Settings Menu
		settings = new JMenu("Settings");
		settingsItem = new JMenuItem("Preferences", new ImageIcon("server/resources/settings.png"));

		settings.setMnemonic(KeyEvent.VK_F);
		settingsItem.setMnemonic(KeyEvent.VK_E);

		settings.add(settingsItem);
		menuBar.add(settings);

		//Light and Dark mode settings
		view = new JMenu("View");
		bg = new ButtonGroup();
		lightMode  = new JRadioButtonMenuItem("Light Mode");
		lightMode.setSelected(true);
		bg.add(lightMode);

		darkMode  = new JRadioButtonMenuItem("Dark Mode");
		bg.add(darkMode);
		view.add(lightMode);
		view.add(darkMode);

		menuBar.add(view);

		//Info Menu
		info = new JMenu("Info");
		info.setMnemonic(KeyEvent.VK_F);
		infoItem = new JMenuItem("Info");
		infoItem.setMnemonic(KeyEvent.VK_E);
		info.add(infoItem);
		menuBar.add(Box.createHorizontalGlue());
		menuBar.add(info);

		setJMenuBar(menuBar);

		/*End of Menu bar*/

		ImageIcon icon = new ImageIcon("ico.png");
		setIconImage(icon.getImage());
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		contentPane.setBackground(Color.decode("#f5f5f5"));

		lblAddr = new JLabel("IP Address:");
		lblAddr.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
		lblAddr.setBounds(22, 13, 172, 14);
		contentPane.add(lblAddr);

		InetAddress inetAddress = InetAddress.getLocalHost();
        String iA=inetAddress.getHostAddress();
		lblIndirizzoIpServer = new JLabel(iA);
		lblIndirizzoIpServer.setFont(new Font("Lucida Grande", Font.BOLD, 15));
		lblIndirizzoIpServer.setBounds(110, 14, 172, 14);
		contentPane.add(lblIndirizzoIpServer);

		textArea = new JTextArea(160, 250);
		textArea.setBounds(22, 40, 458, 390);
		contentPane.add(textArea);
		textArea.setEditable(false);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    void update(String msg) {
		SwingUtilities.invokeLater(() -> textArea.append(msg+"\n"));
	}

}
    