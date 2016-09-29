package ca.concordia.ca_cor.clients;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

import ca.concordia.ca_cor.servers.FlightServerInterface;

@SuppressWarnings("deprecation")
public class PassengerClient {
	static final String servers[] = {"mtl", "iad", "del"};
	
	public static void main(String args[]){
		try{
			System.setSecurityManager(new SecurityManager());
			System.out.println(Naming.list("rmi://localhost:1099")[1]);
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
	
	public static int mainMenu(){
		Scanner scan = new Scanner(System.in);
		int option;
		do{
			System.out.println("Hello, Welcome to Distributred Airlines!");
			System.out.println("1. PASSENGER");
			System.out.println("2. MANAGER");
			System.out.println("3. Exit");
			option = scan.nextInt();
		}while(option < 1 || option > 3);
		return option;
		
	}
	
	public static void passengerMenu(){
		Scanner scan = new Scanner(System.in);
		boolean done = false;
		do{
			System.out.println("Choose one of the following options:");
			System.out.println("1. Book a Flight");
			System.out.println("2. Exit");
			int option = scan.nextInt();
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
			int option = scan.nextInt();
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
					flightClass = scan.nextInt();
					
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
					(FlightServerInterface) Naming.lookup("rmi:localhost:1099/"+servers[departure-1]);
			String nameBreakdown[]= name.split(" ");
			String fName = nameBreakdown[0];
			String lName = nameBreakdown[nameBreakdown.length-1];
			
			return server.bookFlight(fName, lName, address, telephone, servers[destination-1], date, flightClass);
		} catch (Exception e){
			return "ERR-UNKNOWN";
		}
		
	}
	
	public static void showConfirmation(String code){
		String header = code.substring(0, 2);
		String body = code.substring(4, code.length()-1);
		if(header.equalsIgnoreCase("ERR")){
			System.err.println("ERROR-There was a problem with our order: " + body);
		}else if(header.equalsIgnoreCase("OKK")){
			System.out.println("HOORAY - Here is your itenerary: "+ body);
			System.out.println("\n\nThank you for flying with us!");
		}
	}
	
	public static void managerMenu(){
		
	}
}
