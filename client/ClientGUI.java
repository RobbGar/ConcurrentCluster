package client;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.Color;
import java.awt.Font;
import java.awt.Panel;


public class ClientGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	protected JLabel messageField;
	private JTextField IpField;
	private JLabel IpAdress;
	private JButton IpButton;
	private JButton searchButton; //search button
	protected JTextField searchBox; //search box
	protected JTextField positionBox; //position box
	private JTextArea echoes; //echo box
	private JScrollPane scrollPane; //scroll
	private JButton frequentsButton; //Frequent-words button
	private Panel panel_1;
	private JButton btnLocateMe;
	private JMenuBar menuBar;
	private JMenu file;
	private JMenu settings;
	private JMenu info;
	private JMenuItem infoItem;
	private JMenuItem exit;


	private JCheckBoxMenuItem debugMode;

	/* Getters */
	public JButton getLocateMeButton() {
		return btnLocateMe;
	}
	public JButton getFrequentsButton() {
		return frequentsButton;
	}
	public JTextField getIpField() {
		return IpField;
	}
	public JButton getIpButton() {
		return IpButton;
	}
	public JTextField getPositionBox() {
		return positionBox;
	}
	public JTextField getSearchBox() {
		return searchBox;
	}
	public JButton getSearchButton() {
		return searchButton;
	}
	public JMenuItem getInfoItem() {
		return infoItem;
	}
	public JMenuItem getExit() {
		return exit;
	}
	public JCheckBoxMenuItem getDebugMode() {
		return debugMode;
	}


	public ClientGUI() {

		setBounds(100, 100, 709, 528);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		ImageIcon img = new ImageIcon("client/resources/ico.png");
		setIconImage(img.getImage());
		setResizable(false);

		/* Menu Bar*/

		menuBar = new JMenuBar();
		file = new JMenu("File");
		exit = new JMenuItem("Exit");
		file.add(exit);
		menuBar.add(file);

		settings = new JMenu("Settings");
		debugMode = new JCheckBoxMenuItem("Debug Mode");
		settings.add(debugMode);
		menuBar.add(settings);

		info = new JMenu("Info");
		infoItem = new JMenuItem("Info");
		info.add(infoItem);
		menuBar.add(Box.createHorizontalGlue());
		menuBar.add(info);

		setJMenuBar(menuBar);

		/*End of Menu Bar*/

		Panel panel = new Panel();
		panel.setBackground(Color.WHITE);
		panel.setBounds(624, 120, 46, 192);
		contentPane.add(panel);

		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setBackground(Color.WHITE);
		lblNewLabel.setIcon(new ImageIcon("client/resources/google.gif"));
		lblNewLabel.setBounds(150, 120, 480, 192);
		contentPane.add(lblNewLabel);

		IpAdress = new JLabel("Insert IP adress:");
		IpAdress.setFont(new Font("Dialog", Font.PLAIN, 15));
		IpAdress.setBounds(10, 50, 260, 14);
		contentPane.add(IpAdress);

		IpField = new JTextField();
		IpField.setBounds(178, 49, 371, 20);
		contentPane.add(IpField);
		IpField.setColumns(10);
		IpField.setText("localhost");
		IpButton = new JButton("Connect");

		IpButton.setBounds(559, 48, 111, 22);
		contentPane.add(IpButton);

		messageField = new JLabel("WAITING..");
		messageField.setBounds(54, 448, 210, 20);
		contentPane.add(messageField);

		JLabel textLabel = new JLabel("Search:");
		textLabel.setBounds(10, 325, 86, 16);
		contentPane.add(textLabel);

		//Search Box
		searchBox = new JTextField();
		searchBox.setBounds(116, 323, 554, 20);
		contentPane.add(searchBox);

		//Position Box
		positionBox = new JTextField();
		positionBox.setBounds(178, 89, 371, 20);
		contentPane.add(positionBox);

		//Send! Button
		searchButton = new JButton("Send");

		searchButton.setBounds(438, 345, 111, 25);
		contentPane.add(searchButton);

		JLabel locLabel = new JLabel("Where are you searching from?");
		locLabel.setFont(new Font("Tahoma", Font.PLAIN, 10));
		locLabel.setBounds(10, 91, 260, 16);
		contentPane.add(locLabel);

		//Frequent words
		frequentsButton = new JButton("Show frequent words");


		frequentsButton.setBounds(228, 345, 187, 25);
		contentPane.add(frequentsButton);

		//Echoes + Scrollbar
		echoes = new JTextArea(100, 10);
		echoes.setTabSize(1);
		echoes.setEditable(false);
		scrollPane = new JScrollPane(echoes);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setBounds(116, 380, 554, 50);
		contentPane.add(scrollPane);

		JLabel lblStatus = new JLabel("Status :");
		lblStatus.setBounds(10, 451, 46, 14);
		contentPane.add(lblStatus);

		panel_1 = new Panel();
		panel_1.setBackground(Color.WHITE);
		panel_1.setBounds(116, 120, 34, 192);
		contentPane.add(panel_1);

		btnLocateMe = new JButton("Locate me!");
		btnLocateMe.setBounds(559, 88, 111, 23);
		contentPane.add(btnLocateMe);
		setLocationRelativeTo(null);
	}

	public boolean Update(String msg) {
		SwingUtilities.invokeLater(() -> echoes.append(msg+"\n"));
		return true;
	}
}