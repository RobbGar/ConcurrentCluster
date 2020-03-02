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
	protected ExecutorService pool;
	ConcurrentHashMap<String,HashMap<String,Integer>> searches; //hashMap che funziona da "database" in cui salviamo le parole cercate nei vari luoghi
	ConcurrentHashMap<String,HashMap<String,Integer>> mostResearchedWords;      //hashmap per le 3 parole più cercate di ogni città M(ost)S(earched)W(ords)
	protected ServerGUI gui;
	private Data data;

	public Server(ServerGUI x) {
		pool = Executors.newFixedThreadPool(5);
		gui = x;
		searches = new ConcurrentHashMap<>();
		mostResearchedWords = new ConcurrentHashMap<>();
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

			gui.update("Server REG in ascolto");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean research(String words, String location){
		Future<Boolean> f = pool.submit(new CallResearch(words,location,data));
		Boolean res = false;
		try {
			res = f.get();
		} catch (InterruptedException | ExecutionException e) {
			SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog (gui,"Error Researching " + words));
			e.printStackTrace();
		}
		gui.update("Searched " + words + " from " + location);
		return res;
	}

	@Override
	public String MostSearchedW(){
		Future<String> f = pool.submit(new CallPrint(data));
		String res = null;
		try {
			res = f.get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog (gui,"Error Requesting the most searched wordss "));
		} 
		gui.update("Requested the most searched words");
		return res;
	}


}