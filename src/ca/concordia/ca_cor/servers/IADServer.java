package ca.concordia.ca_cor.servers;

import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;
import java.rmi.server.UnicastRemoteObject;

import ca.concordia.ca_cor.models.FlightRecord;

public class IADServer implements FlightServerInterface {

	@Override
	public String bookFlight(String firstName, String lastName, String address, String tel, String dest, String date,
			int flightClass) {
				return date;
		// TODO Auto-generated method stub

	}

	@Override
	public String getBookledFlightCount(int recordType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void editFlightRecord(String recordID, String fildName, String newValue) {
		// TODO Auto-generated method stub

	}
	
	public void exportServer() throws Exception {
		Remote obj = UnicastRemoteObject.exportObject(this, 1098);
		Registry reg = null;
		try{
			reg = LocateRegistry.createRegistry(1099);

		}catch(ExportException e){
			reg = LocateRegistry.getRegistry(1099);
		}
		reg.rebind("iad", obj);
	}
	
	public static void main(String args[]){
		try{
			(new IADServer()).exportServer();
			FlightRecord record = FlightRecord.getInstance();
			System.out.println("Washington Server Running");
			System.out.println("Flights Available: ");
			System.out.println(record);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
