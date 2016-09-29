package ca.concordia.ca_cor.models;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class FlightRecord {
	private static int idBase = 1234;
	private ArrayList<Flight> records;
	private static FlightRecord instance;
	
	private FlightRecord(){
		records = createDB();
	}
	
	public static FlightRecord getInstance(){
		if(instance == null){
			//instance = new FlightRecord();
			instance = new FlightRecord();
		}
		return instance;
	}
	
	public void add(String from, String to,
			Date date, int economicSeats,
			int businessSeats, int firstSeats){
		Flight f = new Flight(++idBase, from, to, date, economicSeats, businessSeats, firstSeats);
		records.add(f);
	}
	
	@Override
	public String toString(){
		StringBuilder s = new StringBuilder();
		for(Flight f : records){
			s.append(f.toString() + '\n');
		}
		return s.toString();
	}

	private class Flight{
		private int id;
		private String departure, destination;
		private Date date;
		private int economic, business, first;
		
		public Flight(int id, String from, String to,
				Date date, int economicSeats, int businessSeats, int firstSeats){
			this.id = id;
			this.departure = from;
			this.destination = to;
			this.date = new Date(date.toString());
			this.economic = economicSeats;
			this.business = businessSeats;
			this.first = firstSeats;
		}

		public String getDeparture() {
			return departure;
		}

		public void setDeparture(String departure) {
			this.departure = departure;
		}

		public String getDestination() {
			return destination;
		}

		public void setDestination(String destination) {
			this.destination = destination;
		}

		public Date getDate() {
			return date;
		}

		public void setDate(Date date) {
			this.date = date;
		}

		public int getEconomic() {
			return economic;
		}

		public void setEconomic(int economic) {
			this.economic = economic;
		}

		public int getBusiness() {
			return business;
		}

		public void setBusiness(int business) {
			this.business = business;
		}

		public int getFirst() {
			return first;
		}

		public void setFirst(int first) {
			this.first = first;
		}
		
		@Override
		public String toString(){
			StringBuilder s = new StringBuilder();
			s.append(departure+id + " | " + departure + " --> " + destination + " | " + 
			new SimpleDateFormat("yyyy/MM/dd 'at' HH:mm").format(date) + " | " + 
				"Econ: " + economic + ", Bus: " + business + ", First: " + first);
			return s.toString();
		}
		
		
	}
	
	private ArrayList<Flight> createDB(){
		ArrayList<Flight> flights = new ArrayList<Flight>();
		Flight f = new Flight(++idBase, "MTL", "IAD", new Date(), 30, 10, 5);
		flights.add(f);
		f = new Flight(++idBase, "IAD", "DEL", new Date(), 30, 10, 5);
		flights.add(f);
		f = new Flight(++idBase, "DEL", "MTL", new Date(), 30, 10, 5);
		flights.add(f);
		return flights;
	}
	
	

}
