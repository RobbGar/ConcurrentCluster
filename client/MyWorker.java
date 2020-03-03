package client;

import java.util.concurrent.ExecutionException;
import javax.swing.SwingWorker;
import shared.IClient;


public class MyWorker extends SwingWorker<Boolean, Void> {

	private ClientGUI gui;
	private String pressed;
	private IClient client;
	private String position;
	private String text;
	
	public MyWorker(ClientGUI gui, String pressed, IClient client, String position, String text) {
		this.gui = gui;
		this.pressed = pressed;
		this.client = client;
		this.position = position;
		this.text = text;
	}
	
	@Override
	protected Boolean doInBackground() throws Exception {
		switch(pressed) {
			case "search": return client.send(text, position);
			case "showFrequents": return client.showFrequents(position);
			default: throw new AssertionError(); 
		}
	}
	
	@Override
	protected void done() {
		boolean result = false;
		try {
			result = get();
		} catch (InterruptedException e) {
			System.out.println("Thread has been interrupted!");
			e.printStackTrace();
		} catch (ExecutionException e) {
			gui.messageField.setText("SERVER OFFLINE");
			e.printStackTrace();
			return;
			}
		
		switch(pressed) {
		case "search": {
			if (!(gui.messageField.getText()=="CONNECTED..")) gui.messageField.setText("WRONG IP!"); 
			else if (!result) gui.messageField.setText("A FIELD IS MISSING!");
				else{
					gui.messageField.setText("HERE WE ARE ;)");
					try {
				         String search = "#q="+gui.searchBox.getText().toString().trim();
				         search = search.replaceAll(" ","+");
				         String url = "http:////www.google.com//"+search;
				         java.awt.Desktop.getDesktop().browse(java.net.URI.create(url));
				       }
				       catch (java.io.IOException e) {
				           System.out.println(e.getMessage());
				       }
					gui.searchBox.setText("");
					gui.positionBox.setText("");
				}
			break;
		}
		
		case "showFrequents": {
			if (!result) gui.messageField.setText("COULDN'T FIND FREQUENT WORDS :(");
				else{
					gui.messageField.setText("Echoing..");
				}
			break;
		}
		
		default: throw new AssertionError();
		}
	}
	
}
	