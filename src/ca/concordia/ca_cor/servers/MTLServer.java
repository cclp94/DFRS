package ca.concordia.ca_cor.servers;

import ca.concordia.ca_cor.models.FlightRecord;

public class MTLServer extends FlightServer {
	static final int UDP_PORT = 2257;
	
	public MTLServer(){
		super();
		super.acronym = "mtl";
		super.RMIPort = 1096;
		try {
			this.exportServer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]){
		Thread thread = null;
		try{
			MTLServer server = new MTLServer();
			System.out.println("Montreal Server Running");
			System.out.println("Flights Available: ");
			System.out.println(server.flightRecord);
			thread = new Thread(new Runnable() {
				@Override
				public void run() {
					server.setupUDPServer(UDP_PORT);
				}
			});
			thread.start();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void createInitialRecord(FlightRecord f){
		f.add("mtl", "iad", "2016/10/03", 30, 10, 5);
		f.add("mtl", "del", "2016/10/03", 30, 10, 5);
		f.add("mtl", "iad", "2016/10/04", 30, 10, 5);
		f.add("mtl", "del", "2016/10/04", 30, 10, 5);
		f.add("mtl", "iad", "2016/10/05", 30, 10, 5);
		f.add("mtl", "del", "2016/10/05", 30, 10, 5);
	}

}
