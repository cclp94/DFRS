package ca.concordia.ca_cor.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Itinerary{
	private static int recordBase = 1;
	private String recordID;
	private String firstName, lastName, address, telephone;
	private Date date;
	private String flightID;
	private int flightClass;
	private String departure, destination;
	
	
	public Itinerary(String firstName, String lastName, String address, String telephone, String flightID,
			Date date, int flightClass, String departure, String destination) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.telephone = telephone;
		this.flightID = flightID;
		this.date = date;
		this.flightClass = flightClass;
		this.departure = departure;
		this.destination = destination;
		this.recordID = generateRecordID();
	}
	
	public Itinerary(String firstName, String lastName, String address, String telephone, String flightID,
			String date, int flightClass, String departure, String destination) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.telephone = telephone;
		this.flightID = flightID;
		try {
			this.date = (new SimpleDateFormat("yyyy/MM/dd")).parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.flightClass = flightClass;
		this.departure = departure;
		this.destination = destination;
		this.recordID = generateRecordID();
	}
	
	
	public Itinerary(Itinerary i){
		super();
		this.firstName = i.firstName;
		this.lastName = i.lastName;
		this.address = i.address;
		this.telephone = i.telephone;
		this.flightID = i.flightID;
		this.date = i.date;
		this.flightClass = i.flightClass;
		this.departure = i.departure;
		this.destination = i.destination;
	}
	
	private String generateRecordID(){
		return Itinerary.recordBase++ + this.flightID+"-"+flightClass;
	}
	
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public int getFlightClass() {
		return flightClass;
	}
	public void setFlightClass(int flightClass) {
		this.flightClass = flightClass;
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
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append(this.flightID); sb.append(" | ");
		sb.append(this.departure + " ---> " + this.destination + " | ");
		sb.append((new SimpleDateFormat("yyyy/MM/dd")).format(this.date) + " | ");
		sb.append(this.lastName.toUpperCase() +", " + this.firstName.toUpperCase() + " | ");
		sb.append("Class: "+ this.flightClass);
		return sb.toString();
	}
	
	
}