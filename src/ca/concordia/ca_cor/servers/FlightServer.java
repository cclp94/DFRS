package ca.concordia.ca_cor.servers;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;
import java.rmi.server.UnicastRemoteObject;

import ca.concordia.ca_cor.models.FlightRecord;
import ca.concordia.ca_cor.models.Itinerary;
import ca.concordia.ca_cor.models.PassengerRecord;

public abstract class FlightServer implements FlightServerInterface {
	protected FlightRecord flightRecord;
	protected PassengerRecord passengerRecord;
	protected static final int serverPorts[] = {2257, 2258,2259};
	protected String acronym;
	protected int RMIPort;
	private static final int REGISTRY_PORT = 1099;
	
	public FlightServer(){
		this.flightRecord = FlightRecord.getInstance();
		this.passengerRecord = PassengerRecord.getInstance();
		createInitialRecord(this.flightRecord);
	}
	
	public abstract void createInitialRecord(FlightRecord f);
	
	public void exportServer() throws Exception {
		Remote obj = UnicastRemoteObject.exportObject(this, RMIPort);
		Registry reg = null;
		try{
			reg = LocateRegistry.createRegistry(REGISTRY_PORT);
		}catch(ExportException e){
			reg = LocateRegistry.getRegistry(REGISTRY_PORT);
		}
		reg.bind(acronym, obj);
	}
	
	@Override
	public String bookFlight(String firstName, String lastName, String address, String tel, String dest, String date,
			int flightClass) {
		Itinerary i = null;
		try {
			String flightNumber = this.flightRecord.attemptFlightReservation(this.acronym, dest, date, flightClass);
			i = new Itinerary(firstName, lastName, address, tel, flightNumber, date, flightClass, acronym, dest);
			passengerRecord.put(lastName.charAt(0), i);
		} catch(RuntimeException e){
			return e.getMessage();
		}
		return "OKK-"+i.toString();
	}

	@Override
	public String getBookledFlightCount(int recordType) {
		DatagramSocket socket = null;
		String reply = "";
		try{
			socket = new DatagramSocket();
			byte[] message = Integer.toString(recordType).getBytes("UTF-8");
			InetAddress host = InetAddress.getByName("localhost");
			for(int s : serverPorts){
				DatagramPacket req = new DatagramPacket(message, message.length, host, s);
				socket.send(req);
				byte buffer[] = new byte[100];
				DatagramPacket p = new DatagramPacket(buffer, buffer.length);
				socket.receive(p);
				reply += ((new String(p.getData())).trim() +"\t");
			}
		}catch(SocketException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			if(socket != null){
				socket.close();
			}
		}
		return reply.toString();
	}

	@Override
	public String editFlightRecord(String recordID, String fieldName, String newValues) {
		String reply = "";
		if(fieldName.equalsIgnoreCase("getall")){
			reply = this.flightRecord.toString();
		} else if(fieldName.equalsIgnoreCase("add")){
			String values[] = newValues.split("&");
			reply = this.flightRecord.add(acronym, values[0], values[1],
					Integer.parseInt(values[2]), Integer.parseInt(values[3]),
					Integer.parseInt(values[4]));
		} else if(fieldName.equalsIgnoreCase("delete")){
			Boolean success = this.flightRecord.deleteById(recordID);
			if(success == null){
				return "ERR-Flight ID not found";
			}else if(!success.booleanValue()){
				return "ERR-There was a problem deleting the flight";
			}else{
				reply="Flight Deleted Successfully";
			}
		}else if(fieldName.contains("edit?")){
			String changedFields[] = fieldName.substring(5).split("&");
			String changedValues[] = newValues.split(",");
			if(changedFields.length == changedValues.length){
				String result="";
				for(int i = 0; i < changedFields.length; i++){
					result = this.flightRecord.changeFlightAttribute(recordID, changedFields[i], changedValues[i]);
				}
				if(result.contains("ERR"))
					return result;
				else
					reply = result;
			}else{
				
			}
		}
		return "OKK-"+reply;
	}
	
	public void setupUDPServer(int port){
		DatagramSocket socket = null;
		try{
			socket = new DatagramSocket(port);
			System.out.println("UDP Server Running...");
			while(true){
				byte[] buffer = new byte[100];
				DatagramPacket request = new DatagramPacket(buffer, buffer.length);
				socket.receive(request);
				int requestData = Integer.parseInt((new String(request.getData()).trim()).toString());
				buffer = (acronym.toUpperCase()+" " +this.passengerRecord.getRecordByClass(
								  requestData))
								  .toString()
						.getBytes("UTF-8");
				DatagramPacket reply = new DatagramPacket(buffer, buffer.length, request.getAddress(), request.getPort());
				socket.send(reply);
			}
		}catch(SocketException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			if(socket != null){
				socket.close();
				System.out.println("UDP Server Closed");
			}
		}
	}

}
