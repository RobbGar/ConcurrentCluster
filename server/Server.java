package server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.*;

import shared.IServer;

import javax.swing.*;

public class Server implements IServer{

	private static final long serialVersionUID = 1L;
	private ExecutorService pool;
	private ServerGUI view;
	private Data data;

	boolean loadData(){
		return data.load();
	}

	boolean saveData(){
		return data.save();
	}

	boolean resetData(){
		return data.reset();
	}

	public Server(ServerGUI x) {
		pool = Executors.newFixedThreadPool(5);
		view = x;
		data = new Data();
		try {
			Registry r;
			try {
				r = LocateRegistry.createRegistry(8080);
			} catch (RemoteException e) {
				r = LocateRegistry.getRegistry(8080);
			}
			IServer stubRequest = (IServer) UnicastRemoteObject.exportObject(this,0);
			r.rebind("REG", stubRequest);

			AndroidS androidS = new AndroidS(5005,this);
			(new Thread(() -> androidS.runWebServer())).start();

			view.update("Listening...");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean search(String s, String location){
		Future<Boolean> f = pool.submit(new Search(s, location, data));
		Boolean res = false;
		try {
			res = f.get();
		} catch (InterruptedException | ExecutionException e) {
			view.update("Error searching for " + s);
			SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog (view,"Error Searching for " + s));
			//TODO remove debug trace
			e.printStackTrace();
		}
		view.update("Searched " + s + " from " + location);
		return res;
	}

	@Override
	public String getTopThree(String location) throws IllegalArgumentException{

		Future<String> f = pool.submit(new TopThree(data, location));

		String res = null;
		try {
			res = f.get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			view.update("Error requesting the most searched words");
		}
		catch (IllegalArgumentException e){
			view.update("Location requested not in the system");
		}

		view.update("Requested the most searched words");
		return res;
	}


}