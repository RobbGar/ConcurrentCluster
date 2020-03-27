package server;
import java.net.Socket;

import static java.util.Objects.requireNonNull;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class WRunnable implements Runnable {
	
	private Socket clientSocket ;
	private Server serverRMI;
	
	public WRunnable(Socket clientSocket, Server serverRMI) {

	this.clientSocket = requireNonNull(clientSocket);
	this.serverRMI = requireNonNull(serverRMI) ;
	}

	@Override
	public void run() {
		try (PrintWriter output =  new PrintWriter( clientSocket.getOutputStream() , true );
			 BufferedReader input = new BufferedReader (new InputStreamReader (clientSocket.getInputStream()))
			 ){
				String command = input.readLine();
				switch(command){
					case "search":{
						manageSearch(input);
						break;
					}

 					case "showFrequents":{
 						manageTopThreeWords(input, output);
 						break;
 					}
 				}
 			} catch (IOException e) {
				e.printStackTrace();
			}
			
	}
	private void manageSearch(BufferedReader input) throws IOException{

		String location = input.readLine();
		String searchedW = input.readLine();
		serverRMI.search(searchedW, location);

	}

 	private void manageTopThreeWords(BufferedReader input, PrintWriter output){
		try {
			String location = input.readLine();
			output.println(serverRMI.getTopThree(location));
		}
 		catch(IOException e){
 			output.println("FAIL");
 		}

 	}
 }