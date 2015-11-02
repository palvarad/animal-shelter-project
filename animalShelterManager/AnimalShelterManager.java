package animalShelterManager;

import javax.swing.JOptionPane;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.PrintWriter;

public class AnimalShelterManager {

	public static void main(String[] args) {
		final String TITLE = "Animal Shelter Manager";
		final String[] LOGIN_OPTIONS = {"Manager", "Employee"};
		final String LOGIN_PROMPT = "Select User Type:";
		
		//Arrays to store the different person objects.
		ShelterPerson shelterManager[] = new Manager[Manager.getMaxManagers()];
		//Loop to initialize all objects inside the array to avoid errors.
		for(int x = 0; x < Manager.getMaxManagers(); x++){
			shelterManager[x] = new Manager();
		}
		
		ShelterPerson shelterEmployees[] = new Employee[Employee.getMaxEmployees()];
		//Loop to initialize all objects inside the array to avoid errors.
		for(int x = 0; x < Employee.getMaxEmployees(); x++){
			shelterEmployees[x] = new Employee();
		}
		
		ShelterPerson shelterCustomers[] = new Customer[10];
		for(int x = 0; x < 10; x++){
			shelterCustomers[x] = new Customer();
		}
		
		Inventory shelterInventory[] = new Inventory[12];
		for(int x = 0; x < 12; x++){
			shelterInventory[x] = new Inventory();
		}
		
		//Method call that reads the file. The array and file path are passed.
		readPersonFile(shelterManager, Manager.toFile());
		readPersonFile(shelterEmployees, Employee.toFile());
		readPersonFile(shelterCustomers, Customer.toFile());
		readInventoryFile(shelterInventory);
		
		//For loop to print the contents of the arrays. For testing and can be deleted in the future.
		for(int x = 0; x < Manager.getMaxManagers(); x++){
			System.out.println(shelterManager[x].toString() + "\n");
		}
		
		for(int x = 0; x < Employee.getEmployeeCount(); x++){
			System.out.println(shelterEmployees[x].toString() + "\n");
		}
		
		System.out.println(shelterCustomers[0].toString());
		
		for(int x = 0; x < 12; x++){
			System.out.println(shelterInventory[x].toString() + "\n");
		}
		
		System.out.println(Inventory.getInventoryCount());
		
		int choice = JOptionPane.showOptionDialog(null, LOGIN_PROMPT, TITLE, 0, JOptionPane.QUESTION_MESSAGE, null, LOGIN_OPTIONS, null);
		if(choice == 0){
			//Example how to use the exceptions being thrown by the DDCs.
			try{
				Manager currentManager = (Manager)shelterManager[0];
				currentManager.validatePassword(JOptionPane.showInputDialog(null, "Enter Password", TITLE, JOptionPane.QUESTION_MESSAGE));
			}catch(IllegalArgumentException e){
				JOptionPane.showMessageDialog(null, e.getMessage(), TITLE, JOptionPane.ERROR_MESSAGE);
			}
		}
		else{
			JOptionPane.showMessageDialog(null, "Add Employee Menu Here");
		}

	}
	
	private static void readPersonFile(ShelterPerson[] persons, String filePath){
		//String for the title of all the messages for this method
		final String TITLE = "Animal Shelter Manager";
		//Variable to keep track of which line is added to which object in the array.
		int personIndex = 0;
		//Variable to store the last position of the semicolon used for dividing attributes.
		int lastInfoDiv;
		//Variable to store the position where to start to search for the next element.
		int divPosition;
		
		//Try and catch to prevent the program from crashing if the file is missing or other I/O errors.
		try{
			//Scanner object to read the file information
			Scanner reader = new Scanner(new FileInputStream(filePath));
			
			//While loop to iterate until there are no more lines to read from the file.
			while(reader.hasNextLine()){
				lastInfoDiv = 0;
				divPosition = 0;
				//String to hold a line from the file.
				String line = reader.nextLine();
				
				//Finds the first semicolon and cuts the unnecessary parts to only have the first name.
				divPosition = line.indexOf(';') + lastInfoDiv;
				persons[personIndex].setFirstName(line.substring(lastInfoDiv, divPosition));
				lastInfoDiv = line.indexOf(';') + 1;

				//Finds the next semicolon and cuts the unnecessary parts to only have the last name.
				divPosition = line.indexOf(';', lastInfoDiv);
				persons[personIndex].setLastName(line.substring(lastInfoDiv, divPosition));
				lastInfoDiv = line.indexOf(';', divPosition);
				
				if(persons[personIndex] instanceof Manager){
					Manager currentManager = (Manager)persons[personIndex];
					divPosition = line.indexOf(';', lastInfoDiv);
					currentManager.setPassword(line.substring(divPosition + 1).trim());
				}
				else if(persons[personIndex] instanceof Employee){
					Employee currentEmployee = (Employee)persons[personIndex];
					divPosition = line.indexOf(';', lastInfoDiv);
					currentEmployee.setEmployeeId(line.substring(divPosition + 1).trim());
					Employee.setEmployeeCount(personIndex + 1);
				}
				else if(persons[personIndex] instanceof Customer){
					Customer currentCustomer = (Customer)persons[personIndex];
					divPosition = line.indexOf(';', lastInfoDiv);
					currentCustomer.setPhoneNumber(line.substring(divPosition + 1).trim());
					//TODO Add a way to read the animals adopted from the file.
				}
				personIndex++;
			}
			//After reading the file close the Scanner object.
			reader.close();
		//File not found exception to display an error message to the user.
		}catch(FileNotFoundException e){
			JOptionPane.showMessageDialog(null, "File was not found.", TITLE, JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private static void readInventoryFile(Inventory[] items){
		//String for the title of all the messages for this method
		final String TITLE = "Animal Shelter Manager";
		//Variable to store the last position of the semicolon used for dividing attributes.
		int lastInfoDiv;
		//Variable to store the position where to start to search for the next element.
		int divPosition;
		int itemIndex = 0;
		
		//Try and catch to prevent the program from crashing if the file is missing or other I/O errors.
		try{
			//Scanner object to read the file information
			Scanner reader = new Scanner(new FileInputStream(Inventory.toFile()));
					
			//While loop to iterate until there are no more lines to read from the file.
			while(reader.hasNextLine()){
				lastInfoDiv = 0;
				divPosition = 0;
				//String to hold a line from the file.
				String line = reader.nextLine();
						
				//Finds the first semicolon and cuts the unnecessary parts to only have the first name.
				divPosition = line.indexOf(';') + lastInfoDiv;
				items[itemIndex].setItemName(line.substring(lastInfoDiv, divPosition));
				lastInfoDiv = line.indexOf(';') + 1;

				//Finds the next semicolon and cuts the unnecessary parts to only have the last name.
				divPosition = line.indexOf(';', lastInfoDiv);
				items[itemIndex].setItemPurchasePrice(((Double.parseDouble(line.substring(lastInfoDiv, divPosition).trim()))));
				lastInfoDiv = line.indexOf(';', divPosition) + 1;
				
				divPosition = line.indexOf(';', lastInfoDiv);
				items[itemIndex].setItemSellPrice(((Double.parseDouble(line.substring(lastInfoDiv, divPosition).trim()))));
				lastInfoDiv = line.indexOf(';', divPosition);
				
				divPosition = line.indexOf(';', lastInfoDiv);
				items[itemIndex].setItemCount(((Integer.parseInt(line.substring(divPosition + 1).trim()))));
				Inventory.setInventoryCount(items[itemIndex].getItemCount());
				itemIndex++;
			}
			//After reading the file close the Scanner object.
			reader.close();
			//File not found exception to display an error message to the user.
			}catch(FileNotFoundException e){
				JOptionPane.showMessageDialog(null, "File was not found.", TITLE, JOptionPane.ERROR_MESSAGE);
			}
	}
}
