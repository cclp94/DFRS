package ca.concordia.ca_cor.clients;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.Scanner;

import ca.concordia.ca_cor.servers.FlightServerInterface;

@SuppressWarnings("resource")
public class PassengerClient {
	static final String servers[] = {"mtl", "iad", "del"};
	
	public static void main(String args[]){
		try{
			System.setSecurityManager(new SecurityManager());
			int option = mainMenu();
			switch(option){
			case 1:
				passengerMenu();
				break;
			case 2:
				managerMenu();
				break;
			case 3:
				System.out.println("Thank you, and we hope to see you again soon!");
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/*===================================MAIN MENU=================================================*/
	
	public static int mainMenu(){
		Scanner scan = new Scanner(System.in);
		int option;
		do{
			System.out.println("Hello, Welcome to Distributred Airlines!");
			System.out.println("1. PASSENGER");
			System.out.println("2. MANAGER");
			System.out.println("3. Exit");
			option = Integer.parseInt(scan.nextLine());
		}while(option < 1 || option > 3);
		return option;
		
	}
	
	/*===========================================PASSENGER==========================================*/
	
	public static void passengerMenu(){
		Scanner scan = new Scanner(System.in);
		boolean done = false;
		do{
			System.out.println("Choose one of the following options:");
			System.out.println("1. Book a Flight");
			System.out.println("2. Exit");
			int option = Integer.parseInt(scan.nextLine());
			switch(option){
			case 1:
				done = bookFlightMenu();
				break;
			case 2:
				done = true;
			}
		}while(!done);
	}
	
	public static boolean bookFlightMenu(){
		Scanner scan = new Scanner(System.in);
		int departure, destination;
		int flightClass;
		String name, address, telephone, date;
		boolean done = false;
		do{
			// Choose Departure
			System.out.println("Where From?");
			System.out.println("1. MONTREAL (MTL)");
			System.out.println("2. WASHINGTON (IAD)");
			System.out.println("3. New Dehli (DEL)");
			System.out.println("4. Back");
			int option = Integer.parseInt(scan.nextLine());
			if(option > 0 && option < 4){
				do{
					departure = option;
					// Choose Destination
					System.out.println("Where To?");
					switch(option){
					case 1:
						System.out.println("2. WASHINGTON (IAD)");
						System.out.println("3. New Dehli (DEL)");
						break;
					case 2:
						System.out.println("1. MONTREAL (MTL)");
						System.out.println("3. New Dehli (DEL)");
						break;
					case 3:
						System.out.println("1. MONTREAL (MTL)");
						System.out.println("2. WASHINGTON (IAD)");
					}
					System.out.println("4. Back");
					option = Integer.parseInt(scan.nextLine());
				}while(option == departure || option < 1 || option > 4);
				if(option != 4){
					destination = option;
					// Colect information
					System.out.println("Please provide us with your information:");
					System.out.print("Full Name: ");
					name = scan.nextLine();
					System.out.print("\nAddress: ");
					address = scan.nextLine();
					System.out.print("\nTelefone: ");
					telephone = scan.nextLine();
					System.out.print("\nDesired Travel Date(YYYY/MM/DD): ");
					date = scan.nextLine();
					System.out.println("\nClass:\n\t1. Economic\n\t2. Business\n\t3. First CLass: ");
					flightClass = Integer.parseInt(scan.nextLine());
					
					String confirmationCode = bookFlightForPassenger(departure, destination, name,
							address, telephone, date, flightClass);
					
					 showConfirmation(confirmationCode);
					 return false;
				}
			}else if(option == 4)
				return false;
		}while(done);
		return done;
	}
	
	public static String bookFlightForPassenger(int departure, int destination, String name,
			String address, String telephone, String date, int flightClass ){
		try {
			FlightServerInterface server = 
					(FlightServerInterface) Naming.lookup("rmi://localhost:1099/"+servers[departure-1]);
			String nameBreakdown[]= name.split(" ");
			String fName = nameBreakdown[0];
			String lName = nameBreakdown[nameBreakdown.length-1];
			
			return server.bookFlight(fName, lName, address, telephone, servers[destination-1], date, flightClass);
		} catch (Exception e){
			return "ERR-UNKNOWN";
		}
		
	}
	
	public static void showConfirmation(String code){
		String header = code.substring(0, 3);
		String body = code.substring(4, code.length());
		if(header.equalsIgnoreCase("ERR")){
			System.err.println("ERROR-There was a problem with our order: " + body);
		}else if(header.equalsIgnoreCase("OKK")){
			System.out.println("HOORAY - Here is your itenerary: "+ body);
			System.out.println("\n\nThank you for flying with us!");
		}
	}
	
	/*======================================= MANAGER ===========================================*/
	
	public static void managerMenu(){
		String credential;
		int option;
		Scanner scan = new Scanner(System.in);
		System.out.print("Login: ");
		credential = scan.nextLine();
		String managerLocation = credential.substring(0, 3);
		try {
			FlightServerInterface server = 
					(FlightServerInterface) Naming.lookup("rmi://localhost:1099/"+managerLocation.toLowerCase());
			System.out.println("WELCOME");
			do{
				System.out.println("What would like to do?");
				System.out.println("1. Manage Flight Records");
				System.out.println("2. Get Number of reserved flights per class");
				System.out.println("3. Exit");
				option = Integer.parseInt(scan.nextLine());
				switch(option){
				case 1:
					manageFlightRecords(server, managerLocation);
					break;
				case 2:
					getNumberOfBookedFlights(server);
				}
			}while(option != 3);
		} catch (MalformedURLException e) {
			System.err.println("ERROR - Manager Credentials are invalid");
		} catch (Exception e) {
			System.err.println("ERROR - Manager Credentials are invalid");
		}		
	}
	
	public static void getNumberOfBookedFlights(FlightServerInterface server){
		int option;
		Scanner scan = new Scanner(System.in);
		
		System.out.println("Get Number of Flights for:");
		System.out.println("1. Economic Class");
		System.out.println("2. Business Class");
		System.out.println("3. First Class");
		System.out.println("4. All");
		option = Integer.parseInt(scan.nextLine());
		try{
			String reply = server.getBookledFlightCount(option);
			System.out.println(reply);
		}catch(RemoteException e){
			e.printStackTrace();
		}
	}
	
	public static void manageFlightRecords(FlightServerInterface server, String managerLocation){
		Scanner scan = new Scanner(System.in);
		int option = 0;
		do{
			System.out.println("Select the desired operation:");
			System.out.println("1. Check all Flight Records");
			System.out.println("2. Add a new Flight");
			System.out.println("3. Delete Existing Flight");
			System.out.println("4. Edit Flight Information");
			System.out.println("5. Back");
			option = Integer.parseInt(scan.nextLine());
			switch(option){
			case 1:
				managerCheckFlight(server);
				break;
			case 2:
				ManagerAddFlight(server, managerLocation);
				break;
			case 3:
				managerDeleteFlight(server);
				break;
			case 4:
				managerEditFlight(server, managerLocation);
			}
		}while(option != 5);
	}
	
	public static void managerCheckFlight(FlightServerInterface server){
		String code = "";
		try {
			code = server.editFlightRecord(null, "getall", null);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		String header = code.substring(0, 3);
		String body = code.substring(4, code.length());
		if(header.equalsIgnoreCase("ERR")){
			System.err.println("ERROR-There was a problem with our order: " + body);
		}else if(header.equalsIgnoreCase("OKK")){
			System.out.println(body);
		}
	}
	
	public static void ManagerAddFlight(FlightServerInterface server, String managerLocation){
		Scanner scan = new Scanner(System.in);
		System.out.println("Select a destionation:");
		if(managerLocation.equalsIgnoreCase(servers[0])){
			System.out.println("\t2. WASHINGTON (IAD)");
			System.out.println("\t3. New Dehli (DEL)");
		} else if(managerLocation.equalsIgnoreCase(servers[1])){
			System.out.println("\t1. MONTREAL (MTL)");
			System.out.println("\t3. New Dehli (DEL)");
		} else if(managerLocation.equalsIgnoreCase(servers[2])){
			System.out.println("\t1. MONTREAL (MTL)");
			System.out.println("\t2. WASHINGTON (IAD)");
		}
		int destination = Integer.parseInt(scan.nextLine());
		
		System.out.print("Select a date (yyyy/mm/dd): ");
		String date = scan.nextLine();
		
		System.out.print("Enter the number of economic seats available: ");
		int econ = Integer.parseInt(scan.nextLine());
		
		System.out.print("Enter the number of Business class seats available: ");
		int bus = Integer.parseInt(scan.nextLine());
		
		System.out.print("Enter the number of first class seats available: ");
		int first = Integer.parseInt(scan.nextLine());
		
		StringBuilder sb = new StringBuilder();
		sb.append(servers[destination-1]).append("&")
		  .append(date).append("&")
		  .append(econ).append("&")
		  .append(bus).append("&")
		  .append(first);
		
		String code = "";
		try {
			code = server.editFlightRecord(null, "add", sb.toString());
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		String header = code.substring(0, 3);
		String body = code.substring(4, code.length());
		if(header.equalsIgnoreCase("ERR")){
			System.err.println("ERROR-There was a problem with our order: " + body);
		}else if(header.equalsIgnoreCase("OKK")){
			System.out.println("FLight add successifully: "+body);
		}
		
	}
		
	
	public static void managerDeleteFlight(FlightServerInterface server){
		Scanner scan = new Scanner(System.in);
		
		System.out.println("Enter the flight id of the flight you want to delete: ");
		String flightID = scan.nextLine();
		
		String code = "";
		try {
			code = server.editFlightRecord(flightID, "delete", null);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		String header = code.substring(0, 3);
		String body = code.substring(4, code.length());
		if(header.equalsIgnoreCase("ERR")){
			System.err.println("ERROR-There was a problem: " + body);
		}else if(header.equalsIgnoreCase("OKK")){
			System.out.println("FLight deleted successifully!");
		}
	}
	
	public static void managerEditFlight(FlightServerInterface server, String managerLocation){
		Scanner scan = new Scanner(System.in);
		
		System.out.println("Enter the flight id of the flight you want to delete: ");
		String flightID = scan.nextLine();
		int option =0;
		StringBuilder requestHeader = new StringBuilder();
		requestHeader.append("edit?");
		StringBuilder requestBody = new StringBuilder();
		System.out.println("Select the properties you would like to change and enter 6 to submit:");
		do{
			System.out.println("1. Destination");
			System.out.println("2. Date");
			System.out.println("3. Number of economic seats available");
			System.out.println("4. Number of Business class seats available");
			System.out.println("5. Number of First class seats available");
			System.out.println("6. Submit");
			option = Integer.parseInt(scan.nextLine());
			int destination, newSeats;
			String date;
			switch(option){
			case 1:
				System.out.println("Select a destionation:");
				if(managerLocation.equalsIgnoreCase(servers[0])){
					System.out.println("\t2. WASHINGTON (IAD)");
					System.out.println("\t3. New Dehli (DEL)");
				} else if(managerLocation.equalsIgnoreCase(servers[1])){
					System.out.println("\t1. MONTREAL (MTL)");
					System.out.println("\t3. New Dehli (DEL)");
				} else if(managerLocation.equalsIgnoreCase(servers[2])){
					System.out.println("\t1. MONTREAL (MTL)");
					System.out.println("\t2. WASHINGTON (IAD)");
				}
				destination = Integer.parseInt(scan.nextLine());
				requestHeader.append("dest&");
				requestBody.append(servers[destination-1]).append(",");
				break;
			case 2:
				System.out.print("What is the new date: ");
				date = scan.nextLine();
				requestHeader.append("date&");
				requestBody.append(date).append(",");
				break;
			case 3:
				System.out.print("Enter the new number of economic seats: ");
				newSeats = Integer.parseInt(scan.nextLine());
				requestHeader.append("econ&");
				requestBody.append(newSeats).append(",");
				break;
			case 4:
				System.out.print("Enter the new number of business class seats: ");
				newSeats = Integer.parseInt(scan.nextLine());
				requestHeader.append("bus&");
				requestBody.append(newSeats).append(",");
				break;
			case 5:
				System.out.print("Enter the new number of first class seats: ");
				newSeats = Integer.parseInt(scan.nextLine());
				requestHeader.append("first&");
				requestBody.append(newSeats).append(",");
				break;
			case 6:
				if(requestBody.length() == 0){
					System.out.println("You have not edited anything yet!");
					option = 0;
					break;
				}else{
					requestBody.setLength(requestBody.length()-1);
					requestHeader.setLength(requestHeader.length()-1);
				}
			}
		}while(option != 6);
		String code = "";
		try {
			code = server.editFlightRecord(flightID, requestHeader.toString(), requestBody.toString());
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		String header = code.substring(0, 3);
		String body = code.substring(4, code.length());
		if(header.equalsIgnoreCase("ERR")){
			System.err.println("ERROR-There was a problem with our order: " + body);
		}else if(header.equalsIgnoreCase("OKK")){
			System.out.println("FLight edited successfully: "+body);
		}
	}

}








