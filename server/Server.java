package server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.concurrent.*;

import shared.IServer;

import javax.swing.*;

public class Server implements IServer{

	private static final long serialVersionUID = 1L;
	private ExecutorService pool;

	private ConcurrentHashMap<String,HashMap<String,Integer>> searches; //hashMap che funziona da "database" in cui salviamo le parole cercate nei vari luoghi
	private ConcurrentHashMap<String,HashMap<String,Integer>> mostSearchedWords;      //hashmap per le 3 parole più cercate di ogni città M(ost)S(earched)W(ords)

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
		searches = new ConcurrentHashMap<>();
		mostSearchedWords = new ConcurrentHashMap<>();
		data = new Data();
		try {
			Registry r;
			try {
				r = LocateRegistry.createRegistry(8080);
			} catch (RemoteException e) {
				r = LocateRegistry.getRegistry(8080);
			}
			IServer stubRequest = (IServer) UnicastRemoteObject.exportObject( this,0);
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
	public boolean search(String words, String location){
		Future<Boolean> f = pool.submit(new Search(words,location,data));
		Boolean res = false;
		try {
			res = f.get();
		} catch (InterruptedException | ExecutionException e) {
			SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog (view,"Error Searching for " + words));
			//TODO remove debug trace
			e.printStackTrace();
		}
		view.update("Searched " + words + " from " + location);
		return res;
	}

	@Override
	public String MostSearchedW(String location){
		Future<String> f = pool.submit(new CallPrint(data, location));
		String res = null;
		try {
			res = f.get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog (view,"Error Requesting the most searched words "));
		}
		catch (IllegalArgumentException e){
			view.update(e.getMessage());
		}
		view.update("Requested the most searched words");
		return res;
	}


}