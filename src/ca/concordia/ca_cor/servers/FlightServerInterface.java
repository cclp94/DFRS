package ca.concordia.ca_cor.servers;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface FlightServerInterface extends Remote {
	
	public String bookFlight(String firstName, String lastName,
			String address, String tel, String dest, String date,
			int flightClass) throws RemoteException;
	
	public String getBookledFlightCount(int recordType)  throws RemoteException;
	
	public String editFlightRecord(String recordID, String fildName, String newValue)  throws RemoteException;

}
