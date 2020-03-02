package shared;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.io.Serializable;

public interface IClient extends Remote,Serializable {
	boolean send(String text, String position) throws RemoteException;
	boolean showFrequents() throws RemoteException;
}