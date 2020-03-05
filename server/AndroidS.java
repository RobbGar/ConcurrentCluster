package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import server.Server;

public class AndroidS {
	private volatile boolean stop;
	private int numPort;
	private ServerSocket listener;
	private ExecutorService serverPool;
	private Server serverRMI;
	
	public AndroidS(int numPort, Server serverRMI){
		this.serverRMI = serverRMI;
		this.numPort = numPort;
		serverPool = Executors.newFixedThreadPool(5);
		this.stop = false;
		try {
			listener = new ServerSocket(this.numPort);
			listener.setSoTimeout(5000);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void runWebServer(){
		System.out.println("AndroidServer in ascolto sulla porta: " + numPort);
		
		while(!stop){
			
			Socket clientSocket;
			try {
				clientSocket = listener.accept();
				System.out.println("Ho appena eseguito workerRunnable da android server");
				this.serverPool.execute( new WRunnable(clientSocket, serverRMI));
				}
			catch(SocketTimeoutException e) {
				//TODO remove debug
			}
			catch (IOException e) { throw new RuntimeException("Errore", e);}
			
			}
		closeServer();
		System.out.println("AndroidServer terminato");
		
	}
	
	private void closeServer() {
		try {
			listener.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		serverPool.shutdown(); 
		try {
			if (!serverPool.awaitTermination(60, TimeUnit.SECONDS)) {
				serverPool.shutdownNow();
				if (!serverPool.awaitTermination(60, TimeUnit.SECONDS))
					System.err.println("PoolAndroid non terminato");
			}
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}	
	}
}
