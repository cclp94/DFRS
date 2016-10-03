package ca.concordia.ca_cor.servers;


import ca.concordia.ca_cor.models.FlightRecord;

public class DELServer extends FlightServer {
	static final int UDP_PORT = 2258;
	
	public DELServer(){
		super();
		super.acronym = "del";
		super.RMIPort = 1097;
		try {
			this.exportServer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]){
		Thread thread = null;
		try{
			DELServer server = new DELServer();
			System.out.println("New Delhi Server Running");
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
		f.add("del", "iad", "2016/10/03", 30, 10, 5);
		f.add("del", "mtl", "2016/10/03", 30, 10, 5);
		f.add("del", "iad", "2016/10/04", 30, 10, 5);
		f.add("del", "mtl", "2016/10/04", 30, 10, 5);
		f.add("del", "iad", "2016/10/05", 30, 10, 5);
		f.add("del", "mtl", "2016/10/05", 30, 10, 5);
	}

}
