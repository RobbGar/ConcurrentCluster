package client;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Controller {
    private ClientGUI view;
    private Client client;
    private String IP;


    public Controller(ClientGUI view){
        this.view = view;
        initController();
    }

    private void initController(){

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
                JOptionPane.showMessageDialog(view, "Insert a Valid location");
        });

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
    }

    private boolean validIP(String ip){
		if ((ip != null) && (ip.length() > 0)) {
		Pattern pattern = Pattern.compile("^\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}$|localhost");
		Matcher m = pattern.matcher(ip);
		return (m.matches());}
		return false;
	}
}
