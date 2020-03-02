package client;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;



import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import java.awt.Font;

import shared.IClient;
import javax.swing.ImageIcon;

public class ClientGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	protected ClientGUI guiReference = this;
	protected IClient client;
	protected String IP ;
	protected JPanel contentPane;
	protected JLabel messageField;
	protected JTextField IpField;
    protected JLabel IpAdress;
    protected JButton IpButton;
    protected JButton searchButton; //search button
    protected JTextField searchBox; //search box
    protected JTextField positionBox; //position box
    protected JTextArea echoes; //echo box
    protected JScrollPane scrollPane; //scroll
    protected JButton frequentsButton; //Frequent-words button
    	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			try {
				ClientGUI frame = new ClientGUI();
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
	
	public ClientGUI() {
	
		setBounds(100, 100, 709, 497);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		IpAdress = new JLabel("Please, insert your server IP adress:");
		IpAdress.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
		IpAdress.setBounds(10, 50, 260, 14);
		contentPane.add(IpAdress);
		
		IpField = new JTextField();
		IpField.setBounds(269, 49, 175, 20);
		contentPane.add(IpField);
		IpField.setColumns(10);
		IpField.setText("localhost");
		IpButton = new JButton("Insert");
		IpButton.addActionListener(arg0 -> {
			 String ip = IpField.getText();
			 IP = ip ;
			 client = new Client(guiReference ,IP);
		});
		IpButton.setBounds(454, 48, 111, 22);
		contentPane.add(IpButton);
		
		messageField = new JLabel("");
		messageField.setBounds(425, 4, 258, 34);
		contentPane.add(messageField);
				
		JLabel textLabel = new JLabel("Search:");
		textLabel.setBounds(20, 325, 86, 16);
		contentPane.add(textLabel);
				
		//creazione casella per inserire il testo
		searchBox = new JTextField();
		searchBox.setBounds(116, 323, 528, 20);
		contentPane.add(searchBox);

		//creazione casella per inserire il testo
		positionBox = new JTextField();
		positionBox.setBounds(298, 89, 146, 20);
		contentPane.add(positionBox);

		//creazione bottone di ricerca
		searchButton = new JButton("Send!");
		searchButton.addActionListener(e -> {
			if (!checkIp()) return;
			String text = searchBox.getText();
			String position = positionBox.getText();
			(new MyWorker(guiReference, "search", client, position, text)).execute();
		});
		searchButton.setBounds(533, 354, 111, 25);
		contentPane.add(searchButton);

		JLabel locLabel = new JLabel("Where are you searching from?");
		locLabel.setBounds(10, 91, 260, 16);
		contentPane.add(locLabel);

		//creazione bottone per stampa parole più frequenti
		frequentsButton = new JButton("Show frequent words!");
		frequentsButton.addActionListener(e -> {
			if (!checkIp()) return;
			(new MyWorker(guiReference, "showFrequents", client,null,null)).execute();
		});
		frequentsButton.setBounds(20, 364, 187, 25);
		contentPane.add(frequentsButton);
				
		//creazione area di testo con scrollbar
		echoes = new JTextArea(1, 5);
		echoes.setTabSize(1);
		echoes.setEditable(false);
		scrollPane = new JScrollPane(echoes);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setBounds(20, 400, 650, 50);
		contentPane.add(scrollPane);
		
		JLabel lblStatus = new JLabel("Status :");
		lblStatus.setBounds(369, 4, 46, 14);
		contentPane.add(lblStatus);
		
		JLabel lblNewLabel = new JLabel("New label");
		lblNewLabel.setIcon(new ImageIcon(ClientGUI.class.getResource("images/google.png")));
		lblNewLabel.setBounds(20, 120, 566, 192);
		contentPane.add(lblNewLabel);
		setLocationRelativeTo(null);
				
	}
			
	public boolean Update(String msg) {
		SwingUtilities.invokeLater(() -> echoes.append(msg+"\n"));
		return true;
	}
	
	private boolean checkIp(){
		if (IP == null || IP.equals("")){
			JOptionPane.showMessageDialog (guiReference,"IP ADRESS IS MISSING :|");
			return false;
		}
		return true;
	}
}