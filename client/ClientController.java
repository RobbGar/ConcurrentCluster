package client;


import io.ipinfo.api.IPInfo;
import io.ipinfo.api.errors.RateLimitedException;
import io.ipinfo.api.model.IPResponse;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class ClientController {
    private ClientGUI view;
    private Client client;
    private String IP;
	private IPInfo ipInfo;

    ClientController(ClientGUI view){
        this.view = view;
		 ipInfo = IPInfo.builder().setToken("1771642d78e8e5").build();
        initController();
    }

    private void initController(){

        view.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                System.exit(0);
            }
        });
        
        view.getIpButton().addActionListener(arg0 -> {
			 String ip = view.getIpField().getText();
			 if (ip != null){
			 	if(validIP(ip)) {
					IP = ip;
					client = new Client(view, IP);
				}
			 }
			 else
				 JOptionPane.showMessageDialog(view, "Insert a Valid IP Address");

		});
        
        view.getLocateMeButton().addActionListener(e -> {
        		try {
					URL whatismyip = new URL("http://checkip.amazonaws.com");
					BufferedReader in = new BufferedReader(new InputStreamReader(
							whatismyip.openStream()));
					String ip = in.readLine();
					IPResponse response = ipInfo.lookupIP(ip);
					view.getPositionBox().setText(response.getCity());
				}
				catch (RateLimitedException | IOException e1)
				{
					e1.printStackTrace();
				}


        });
        
        view.getSearchButton().addActionListener(e -> {
			if (!validIP(IP)) return;
			String text = view.getSearchBox().getText();
			String position = view.getPositionBox().getText();
			(new MyWorker(view, "search", client, position, text)).execute();
		});
        
        view.getFrequentsButton().addActionListener(e -> {
			String s = (String)JOptionPane.showInputDialog(
					view,
					"Insert the location",
					"Show Frequent Words",
					JOptionPane.PLAIN_MESSAGE,
					null,
					null,
					"");

			if ((s != null) && (s.length() > 0)) {
				if (!validIP(IP)) return;
				(new MyWorker(view, "showFrequents", client,s,null)).execute();
				}
			else
				JOptionPane.showMessageDialog(view, "Insert a valid position");
			});

		view.getInfoItem().addActionListener(e ->
				JOptionPane.showMessageDialog(view,
						"Created by Roberto Garbarino, NotMyUid & Cristiano Olivari",
						"Info",
						JOptionPane.PLAIN_MESSAGE,
						new ImageIcon("server/resources/ico.png")
				));

		view.getExit().addActionListener(e -> System.exit(0));
    }

    private boolean validIP(String ip){
		if ((ip != null) && (ip.length() > 0)) {
		Pattern pattern = Pattern.compile("^\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}$|localhost");
		Matcher m = pattern.matcher(ip);
		return (m.matches());}
		return false;
	}
}
