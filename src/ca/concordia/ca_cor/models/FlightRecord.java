package ca.concordia.ca_cor.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FlightRecord {
	private static int idBase = 1234;
	private ArrayList<Flight> records;
	private static FlightRecord instance;
	
	private FlightRecord(){
		records = new ArrayList<Flight>();
	}
	
	public static FlightRecord getInstance(){
		if(instance == null){
			//instance = new FlightRecord();
			instance = new FlightRecord();
		}
		return instance;
	}
	
	public String attemptFlightReservation(String departure, String destination, String date, int flightClass){
		for(Flight f : records){
			if(departure.equalsIgnoreCase(f.getDeparture()) && destination.equalsIgnoreCase(f.getDestination())){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
				if(date.equalsIgnoreCase(sdf.format(f.getDate()))){
					switch (flightClass) {
						case 1:
							if (f.economic > 0) f.economic--;
							else throw new RuntimeException("ERR-NO SEATS AVAILABLE");
							break;
						case 2:
							if (f.business > 0) f.business--;
							else throw new RuntimeException("ERR-NO SEATS AVAILABLE");							
							break;
						case 3:
							if (f.first > 0) f.first--;
							else throw new RuntimeException("ERR-NO SEATS AVAILABLE");
					}
					return f.getFlightNumber();
				}
			}
		}
		throw new RuntimeException("ERR-FLIGHT NOT FOUND");
	}
	
	public String add(String from, String to,
			String date, int economicSeats,
			int businessSeats, int firstSeats){
		Date d;
		try {
			d = (new SimpleDateFormat("yyyy/MM/dd")).parse(date);
			Flight f = new Flight(++idBase, from, to, d, economicSeats, businessSeats, firstSeats);
			if(records.add(f))
				return records.get(records.size()-1).toString();
			else
				return "ERR-Flight could not be added";
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "ERR-UNKNOWN";
	}
	
	public Boolean deleteById(String flightID){
		for(Flight f : records){
			if(f.getFlightNumber().equalsIgnoreCase(flightID)){
				return records.remove(f);
			}
		}
		return null;
	}
	
	public String changeFlightAttribute(String flightID, String field, String value){
		for(Flight f : records){
			if(f.getFlightNumber().equalsIgnoreCase(flightID)){
				if(field.equalsIgnoreCase("dest")){
					f.setDestination(value);
				}else if(field.equalsIgnoreCase("date")){
					Date d;
					try {
						d = (new SimpleDateFormat("yyyy/MM/dd")).parse(value);
					} catch (ParseException e) {
						return "ERR-INVALID DATE CHANGE";
					}
					f.setDate(d);
				}else if(field.equalsIgnoreCase("econ")){
					f.setEconomic(Integer.parseInt(value));
				}else if(field.equalsIgnoreCase("bus")){
					f.setBusiness(Integer.parseInt(value));
				}else if(field.equalsIgnoreCase("first")){
					f.setFirst(Integer.parseInt(value));
				}else{
					return "ERR-INVALID FIELD TO EDIT";
				}
				return f.toString();
			}
		}
		return "ERR-FLIGHT NOT FOUND";
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
		
		

		public String getFlightNumber(){
			return departure.toUpperCase()+id;
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
		
		@Override
		public boolean equals(Object obj) {
			try{
				Flight flight = (Flight) obj;
				return (flight.id == this.id) ? true : false;
			}catch(Exception e){
				return false;
			}
		}
		
		
	}
	
	/*private ArrayList<Flight> createDB(){
		ArrayList<Flight> flights = new ArrayList<Flight>();
		Flight f = new Flight(++idBase, "MTL", "IAD", new Date(), 30, 10, 5);
		flights.add(f);
		f = new Flight(++idBase, "IAD", "DEL", new Date(), 30, 10, 5);
		flights.add(f);
		f = new Flight(++idBase, "DEL", "MTL", new Date(), 30, 10, 5);
		flights.add(f);
		return flights;
	}*/
	
	

}
