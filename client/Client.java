package client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import shared.IClient;
import shared.IServer;

import javax.swing.*;

public class Client implements IClient{

	private static final long serialVersionUID = 1L;
	private ClientGUI GUI;
    private IServer server;
    private String serverIP;
    	
    public Client(ClientGUI guiReference, String IP) {
    	try {
    			this.GUI = guiReference;
    			this.serverIP = IP;
    			Registry r = LocateRegistry.getRegistry(serverIP,8080);
    			server = (IServer) r.lookup("REG");	
    		}
    	catch (RemoteException | NotBoundException e) {
			SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog (guiReference,"Server Offline"));
			System.out.println("Server offline");
    	}
    }
    	
    @Override
    public boolean send(String text, String position) throws RemoteException {
    	if (text.equals("") || position.equals("")) return false;
    	return server.search(text, position);
    }
    	
    @Override
    public boolean showFrequents(String location) throws RemoteException{
    	return(GUI.Update(server.MostSearchedW(location)));
    }
    	
}