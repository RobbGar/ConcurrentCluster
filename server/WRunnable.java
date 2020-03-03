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
						manageResearch(output, input);
						break;
					}

 					case "mostSearchedW":{
 						manageMostSearchedW(output, input);
 						break;
 					}
 				}

 			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
	}
	private void manageResearch (PrintWriter output , BufferedReader input) throws IOException{

		String location = input.readLine();
		String searchedW = input.readLine();
		boolean result = serverRMI.search(searchedW, location);
	}

 	private void manageMostSearchedW (PrintWriter output , BufferedReader input){
		try {
			String location = input.readLine();
			String str = serverRMI.MostSearchedW(location);
			output.println(str);
		}
 		catch(IOException e){
 			e.printStackTrace();
 		}

 	}
 }