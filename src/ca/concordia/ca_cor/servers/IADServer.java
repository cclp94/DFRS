package ca.concordia.ca_cor.servers;

import ca.concordia.ca_cor.models.FlightRecord;

public class IADServer extends FlightServer {
	static final int UDP_PORT = 2259;

	public IADServer(){
		super();
		super.acronym = "iad";
		super.RMIPort = 1098;
		try {
			this.exportServer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]){
		Thread thread = null;
		try{
			IADServer server = new IADServer();
			System.out.println("Washington Server Running");
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
		f.add("iad", "mtl", "2016/10/03", 30, 10, 5);
		f.add("iad", "del", "2016/10/03", 30, 10, 5);
		f.add("iad", "mtl", "2016/10/04", 30, 10, 5);
		f.add("iad", "del", "2016/10/04", 30, 10, 5);
		f.add("iad", "mtl", "2016/10/05", 30, 10, 5);
		f.add("iad", "del", "2016/10/05", 30, 10, 5);
	}

}
