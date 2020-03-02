package shared;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IServer extends Serializable, Remote {
	boolean research(String words, String location) throws RemoteException;
	String MostSearchedW() throws RemoteException;
}