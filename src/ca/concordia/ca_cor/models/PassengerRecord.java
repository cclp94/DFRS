package ca.concordia.ca_cor.models;

import java.util.ArrayList;
import java.util.LinkedList;

public class PassengerRecord {
	private static PassengerRecord record;
	private ItineraryEntry hashMap[];
	int size;
	
	private PassengerRecord(){
		hashMap = new ItineraryEntry[26];
		size = 0;
	}
	
	public static PassengerRecord getInstance(){
		if(record == null){
			record = new PassengerRecord();
		}
		return record;
	}
	
	public void put(char key, Itinerary value){
		int keyLocation = getKeyLocation(key);
		if(hashMap[keyLocation] == null){
			hashMap[keyLocation] = new ItineraryEntry(key);
		}
		hashMap[keyLocation].addValueToList(value);
		size++;
	}
	
	private int getKeyLocation(Character c){
		int asciiBase = 96;
		return ((int) Character.toLowerCase(c)) - asciiBase - 1;
	}
	
	public Integer getRecordByClass(int flightClass){
		int counter = 0;
		for(Itinerary i : this.getValues()){
			if(i.getFlightClass() == flightClass)
				counter++;
		}
		return counter;
	}
	
	public Itinerary[] getValues(){
		Itinerary[] values= new Itinerary[size];
		int tmpCounter =0;
		for(int i = 0; i < hashMap.length; i++){
			if(hashMap[i] != null){
				for(Itinerary value : hashMap[i].getValue()){
					values[tmpCounter++] = value;
				}
			}
		}
		return values;
	}
	
	
	private class ItineraryEntry{
		private char key;
		private LinkedList<Itinerary> value;
		
		public ItineraryEntry(char k){
			key = k;
			value = new LinkedList<Itinerary>();
		}
		
		public void addValueToList(Itinerary i){
			value.add(new Itinerary(i));
		}
		
		public char getKey() {
			return key;
		}
		public void setKey(char key) {
			this.key = key;
		}
		public LinkedList<Itinerary> getValue() {
			return value;
		}
		public void setValue(LinkedList<Itinerary> value) {
			this.value = value;
		}
		
		
	}

}
