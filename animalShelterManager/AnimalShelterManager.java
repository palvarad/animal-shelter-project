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
		int choice = 0, terminate = 0;
		
		//Arrays to store the different person objects.
		ShelterPerson[] shelterManager = new Manager[Manager.getMaxManagers()];
		//Loop to initialize all objects inside the array to avoid errors.
		for(int x = 0; x < Manager.getMaxManagers(); x++){
			shelterManager[x] = new Manager();
		}
		
		ShelterPerson[] shelterEmployees = new Employee[Employee.getMaxEmployees()];
		//Loop to initialize all objects inside the array to avoid errors.
		for(int x = 0; x < Employee.getMaxEmployees(); x++){
			shelterEmployees[x] = new Employee();
		}
		
		ShelterPerson[] shelterCustomers = new Customer[10];
		for(int x = 0; x < 10; x++){
			shelterCustomers[x] = new Customer();
		}
		
		Inventory[] shelterInventory = new Inventory[Inventory.getMaxItemsCount()];
		for(int x = 0; x < Inventory.getMaxItemsCount(); x++){
			shelterInventory[x] = new Inventory();
		}
		
		Animal[] shelterAnimals = new Animal[Animal.getMaxAnimals()];
		for(int x = 0; x < Animal.getMaxAnimals(); x++){
			shelterAnimals[x] = new Animal();
		}
		
		//Method call that reads the file. The array and file path are passed.
		readPersonFile(shelterManager, Manager.fileLocation());
		readPersonFile(shelterEmployees, Employee.fileLocation());
		readPersonFile(shelterCustomers, Customer.fileLocation());
		readInventoryFile(shelterInventory);
		readAnimalFile(shelterAnimals);
		
		do{
			choice = JOptionPane.showOptionDialog(null, LOGIN_PROMPT, TITLE, 0, JOptionPane.QUESTION_MESSAGE, null, LOGIN_OPTIONS, null);
			
			if(choice == 0){
				//Example how to use the exceptions being thrown by the DDCs.
				try{
					Manager currentManager = (Manager)shelterManager[0];
					currentManager.validatePassword(JOptionPane.showInputDialog(null, "Enter the manager password:", TITLE, JOptionPane.QUESTION_MESSAGE));
					terminate = managerMenu(shelterInventory, shelterAnimals);
					
				}catch(IllegalArgumentException e){
					JOptionPane.showMessageDialog(null, e.getMessage(), TITLE, JOptionPane.ERROR_MESSAGE);
				}
			}
			else if(choice == 1){
				employeeMenu(shelterCustomers, shelterInventory, shelterAnimals);
			}
		}while(terminate != -1);
	}
	
	private static int managerMenu(Inventory[] items, Animal[] animals){
		final String TITLE = "Animal Shelter Manager Menu";
		final String PROMPT = "Select an option from the menu:\n";
		final String[] OPTIONS = {"  1) Check Inventory\n", "  2) Order Supplies\n",
							"  3) Update Employees\n", "  4) Expense Report\n", "  5) Exit\n", "  6) Terminate System\n"};
		final String ERROR_NFE = "Please enter a number to select an option.";
		final String ERROR_MENU = "Please select an option from the menu.";
		int managerChoice = 0, terminationChoice = 0;
		
		do{
			try{
				managerChoice = Integer.parseInt(JOptionPane.showInputDialog(null, PROMPT + OPTIONS[0] + OPTIONS[1] + OPTIONS[2]
						+ OPTIONS[3] + OPTIONS[4] + OPTIONS[5] , TITLE, JOptionPane.QUESTION_MESSAGE));
				
				switch(managerChoice){
					case 1:
						checkInventory(items);
						break;
					case 2:
						//TODO
						break;
					case 3:
						//TODO
						break;
					case 4:
						//TODO
						break;
					case 5:
						JOptionPane.showMessageDialog(null, "Returning to Login Screen", TITLE, JOptionPane.INFORMATION_MESSAGE);
						break;
					case 6:
						JOptionPane.showMessageDialog(null, "Shutting Down System", TITLE, JOptionPane.INFORMATION_MESSAGE);
						terminationChoice = -1;
						managerChoice = 5;
						break;
					default:
						JOptionPane.showMessageDialog(null, ERROR_MENU, TITLE, JOptionPane.ERROR_MESSAGE);
				}
			}catch(NumberFormatException e){
				JOptionPane.showMessageDialog(null, ERROR_NFE, TITLE, JOptionPane.ERROR_MESSAGE);
			}
			
		}while(managerChoice != 5);
		
		return terminationChoice;
	}
	
	private static void employeeMenu(ShelterPerson[] customers, Inventory[] items, Animal[] animals){
		final String TITLE = "Animal Shelter Employee Menu";
		final String PROMPT = "Select an option from the menu:\n";
		final String[] OPTIONS = {"  1) Adopt an animal\n", "  2) Add an animal\n",
							"  3) Make a purchase\n", "  4) Animal Population\n", "  5) Exit\n"};
		final String ERROR_NFE = "Please enter a number to select an option.";
		final String ERROR_MENU = "Please select an option from the menu.";
		int employeeChoice = 0;
		
		do{
			try{
				employeeChoice = Integer.parseInt(JOptionPane.showInputDialog(null, PROMPT + OPTIONS[0] + OPTIONS[1] + OPTIONS[2]
						+ OPTIONS[3] + OPTIONS[4] , TITLE, JOptionPane.QUESTION_MESSAGE));
				
				switch(employeeChoice){
					case 1:
						animalAdoption(customers, animals);
						break;
					case 2:
						addAnimal(animals);
						break;
					case 3:
						purchaseItem(items);
						break;
					case 4:
						animalPopulation();
						break;
					case 5:
						JOptionPane.showMessageDialog(null, "Returning to Login Screen", TITLE, JOptionPane.INFORMATION_MESSAGE);
						break;
					default:
						JOptionPane.showMessageDialog(null, ERROR_MENU, TITLE, JOptionPane.ERROR_MESSAGE);
				}
			}catch(NumberFormatException e){
				JOptionPane.showMessageDialog(null, ERROR_NFE, TITLE, JOptionPane.ERROR_MESSAGE);
			}
			
		}while(employeeChoice != 5);
	}
	
	private static void animalAdoption(ShelterPerson[] customers, Animal[] animals){
		final String TITLE = "Animal Shelter Adooption Menu";
		int foundIndex = 0;
		String phoneNum = "";
		boolean customerFound = false;
		Customer tempCustomer = null;
		
		//TODO add loop to re-prompt
		try{
			phoneNum = JOptionPane.showInputDialog(null, "Enter the customer's phone number:", TITLE, JOptionPane.QUESTION_MESSAGE);
			Customer.validatePhoneNumber(phoneNum);
			for(int x = 0; x < customers.length; x++){
				if(customers[x] instanceof Customer){
					tempCustomer = (Customer)customers[x];
					
					if(tempCustomer.getPhoneNumber().equals(phoneNum)){
						foundIndex = x;
						customerFound = true;
					}
				}
			}
			if(customerFound){
				JOptionPane.showMessageDialog(null, "Welcome Back " + customers[foundIndex].getFirstName() + "!");
				adoptAnimal((Customer)customers[foundIndex], animals);
			}
			else if(!customerFound){
				JOptionPane.showMessageDialog(null, "Customer information not found. You must create a new account.");
				createNewCustomer((Customer)customers[Customer.getCustomerCount()], phoneNum);
			}
			
			writeShelterPersonToFile(customers);
		}catch(IllegalArgumentException e){
			JOptionPane.showMessageDialog(null, e.getMessage(), TITLE, JOptionPane.ERROR_MESSAGE);
		}
		
	}
	
	private static void createNewCustomer(Customer newCustomer, String phone){
		final String TITLE = "Animal Shelter Adooption Menu";
		final String FNAME_PROMPT = "Enter the customer's first name:";
		final String LNAME_PROMPT = "Enter the customer's last name:";
		
		//TODO add loops to re-prompt
		try{
			newCustomer.setPhoneNumber(phone);
			newCustomer.setFirstName(JOptionPane.showInputDialog(null, FNAME_PROMPT, TITLE, JOptionPane.QUESTION_MESSAGE));
			newCustomer.setLastName(JOptionPane.showInputDialog(null, LNAME_PROMPT, TITLE, JOptionPane.QUESTION_MESSAGE));
			Customer.setCustomerCount(1);
		}catch(IllegalArgumentException e){
			JOptionPane.showMessageDialog(null, e.getMessage(), TITLE, JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private static void adoptAnimal(Customer oldCustomer, Animal[] animals){
		final String TITLE = "Animal Shelter Adooption Menu";
		int choice = animalMenu();
		String animalType = "", adoptionMessage = "The following ";
		
		switch(choice){
			case 1:
				animalType = "Dog";
				break;
			case 2:
				animalType = "Cat";
				break;
			case 3:
				animalType = "Rodent";
				break;
			case 4:
				animalType = "Bird";
				break;
		}
		
		adoptionMessage += animalType + " is available for adoption.\n ID: ";
		
		for(int x = 0; x < animals.length; x++){
			if(animals[x].getAnimalType().equals(animalType)){
				oldCustomer.adoptAnimal(animals[x]);
				JOptionPane.showMessageDialog(null, adoptionMessage + animals[x].getAnimalID(), TITLE, JOptionPane.INFORMATION_MESSAGE);
				removeAnimal(animals, x);
				break;
			}
		}
	}
	
	private static void addAnimal(Animal[] animals){
		final String TITLE = "Animal Shelter Employee Menu";
		String message = "", animalType = "";
		int animalChoice = animalMenu();
		
		switch(animalChoice){
			case 1:
				animalType = "Dog";
				break;
			case 2:
				animalType = "Cat";
				break;
			case 3:
				animalType = "Rodent";
				break;
			case 4:
				animalType = "Bird";
				break;
		}
		
		animals[Animal.getAnimalCount()].setAnimalType(animalType);
		
		message = " A new " + animalType + " has been added!\n ID: " + animals[Animal.getAnimalCount() - 1].getAnimalID() +
				"\n New " + animalType + " Population: " + Animal.getTypeCount(animalType);
		
		writeAnimalToFile(animals);
		
		JOptionPane.showMessageDialog(null, message, TITLE, JOptionPane.INFORMATION_MESSAGE);
	}
	
	private static void purchaseItem(Inventory[] items){
		final String TITLE = "Animal Shelter Purchase Menu";
		int purchaseCount = 0, buyMore = 0, item = 0;
		String receipt = "", quantityPrompt = "", morePurchasesPrompt = "Would you like to make another purchase?", header = "";
		double total = 0;
		String[] itemNames = new String[Inventory.getMaxItemsCount()];
		int[] itemQuan = new int[Inventory.getMaxItemsCount()];
		double[] itemPrices = new double[Inventory.getMaxItemsCount()];
		
		do{
			item = inventoryMenu();
			quantityPrompt = "Enter the number of " + items[item].getItemName() + " to purchase:";
			itemNames[purchaseCount] = items[item].getItemName();
		
			try{
				itemQuan[purchaseCount] = Integer.parseInt(JOptionPane.showInputDialog(null, quantityPrompt, TITLE, JOptionPane.QUESTION_MESSAGE));
				itemPrices[purchaseCount] = items[item].inventorySell(itemQuan[purchaseCount]);
				
				purchaseCount++;
				header = "Receipt Preview:\n";
				
				for(int x = 0; x < purchaseCount; x++){
					receipt += "  Item " + (x + 1) + ": " + itemNames[x] + " || Qty: " + itemQuan[x] 
							+ " || Cost: " + String.format("$%,.2f", itemPrices[x]) + "\n";
					total += itemPrices[x];
				}
				
			
				JOptionPane.showMessageDialog(null, header + receipt + "  Total: " + String.format("$%,.2f", total));
			}catch(NumberFormatException e){
				JOptionPane.showMessageDialog(null, "The quantity must a numeric value.", TITLE, JOptionPane.ERROR_MESSAGE);
				
			}catch(IllegalArgumentException e){
				JOptionPane.showMessageDialog(null, e.getMessage(), TITLE, JOptionPane.ERROR_MESSAGE);
			}
			
			buyMore = JOptionPane.showConfirmDialog(null, morePurchasesPrompt, TITLE, JOptionPane.YES_NO_OPTION);
			if(buyMore == JOptionPane.YES_OPTION){
				receipt = "";
				total = 0;
			}
		}while(buyMore != JOptionPane.NO_OPTION);
		
		header = "Final Receipt:\n";
		JOptionPane.showMessageDialog(null, header + receipt + "  Total: " + String.format("$%,.2f", total));
		writeInventoryToFile(items);
	}
	
	private static void animalPopulation(){
		final String TITLE = "Animal Shelter Population Menu";
		String header = "Animal Population:\n";
		
		String dogCount = "  Dog Population: " + Animal.getTypeCount("Dog") + "\n";
		String catCount = "  Cat Population: " + Animal.getTypeCount("Cat") + "\n";
		String rodentCount = "  Small Rodent Population: " + Animal.getTypeCount("Rodent") + "\n";
		String birdCount = "  Bird Population: " + Animal.getTypeCount("Bird") + "\n";
		String currentPop = "  Current Population: " + Animal.getAnimalCount() + "\n";
		String maxPop = "  Max Population: " + Animal.getMaxAnimals();
		
		JOptionPane.showMessageDialog(null, header + dogCount + catCount + rodentCount +
				birdCount + currentPop + maxPop, TITLE, JOptionPane.INFORMATION_MESSAGE);
	}
	
	private static void checkInventory(Inventory[] items){
		final String TITLE = "Animal Shelter Inventory Menu";
		String header = "Inventory Screen 1/2:\n";
		String dogInv = "Dog Inventory:\n   Food: " + items[0].getItemCount() + "\n   Medicine: " + items[1].getItemCount() + "\n";
		String catInv = "Cat Inventory:\n   Food: " + items[2].getItemCount() + "\n   Medicine: " + items[3].getItemCount() + "\n";
		String rodInv = "Rodent Inventory:\n   Food: " + items[4].getItemCount() + "\n   Medicine: " + items[5].getItemCount() + "\n";
		String birdInv = "Bird Inventory:\n   Food: " + items[6].getItemCount() + "\n   Medicine: " + items[7].getItemCount() + "\n";
		
		JOptionPane.showMessageDialog(null, header + dogInv + catInv + rodInv + birdInv, TITLE, JOptionPane.INFORMATION_MESSAGE);
		
		header = "Inventory Screen 2/2:\n";
		String lInv = "  " + items[8].getItemName() + " : " + items[8].getItemCount() + "\n";
		String cInv = "  " + items[9].getItemName() + " : " + items[9].getItemCount() + "\n";
		String fInv = "  " + items[10].getItemName() + " : " + items[10].getItemCount() + "\n";
		String tInv = "  " + items[11].getItemName() + " : " + items[11].getItemCount() + "\n";
		String total = "Total: " + Inventory.getInventoryCount() + "\n";
		String max = "Max: " + Inventory.getMaxInventory() + "\n";
		
		JOptionPane.showMessageDialog(null, header + lInv + cInv + fInv + tInv + total + max, TITLE, JOptionPane.INFORMATION_MESSAGE);
	}
	
	private static int animalMenu(){
		final String TITLE = "Animal Shelter Employee Menu";
		final String PROMPT = "Select the type of animal:\n";
		final String[] OPTIONS = {"  1) Dog\n", "  2) Cat\n", "  3) Small Rodent\n", "  4) Bird\n"};
		final String ERROR_NFE = "Please enter a number to select an option.";
		final String ERROR_MENU = "Please select an option from the menu.";
		int choice = 0;
		boolean validChoice = false;
		
		do{
			try{
				choice = Integer.parseInt(JOptionPane.showInputDialog(null, PROMPT + OPTIONS[0] + OPTIONS[1] + OPTIONS[2]
						+ OPTIONS[3], TITLE, JOptionPane.QUESTION_MESSAGE));
				if(choice <= 0 || choice > 4){
					JOptionPane.showMessageDialog(null, ERROR_MENU, TITLE, JOptionPane.ERROR_MESSAGE);
					validChoice = false;
				}else{
					validChoice = true;
				}
			}catch(NumberFormatException e){
				JOptionPane.showMessageDialog(null, ERROR_NFE, TITLE, JOptionPane.ERROR_MESSAGE);
			}
			
		}while(!validChoice);
		
		return choice;
	}
	
	private static int inventoryMenu(){
		final String TITLE = "Animal Shelter Employee Menu";
		final String PROMPT = "Select an item:\n";
		final String[] OPTIONS = {"  1) Food\n", "  2) Medicine\n", "  3) Food Bowl\n", "  4) Leashes\n", "  5) Collars\n",
				"  6) Toys\n"};
		final String ERROR_NFE = "Please enter a number to select an option.";
		final String ERROR_MENU = "Please select an option from the menu.";
		int choice = 0, animalChoice = 0, modifier = 5;
		boolean validChoice = false;
		
		do{
			try{
				choice = Integer.parseInt(JOptionPane.showInputDialog(null, PROMPT + OPTIONS[0] + OPTIONS[1] + OPTIONS[2]
						+ OPTIONS[3] + OPTIONS[4] + OPTIONS[5], TITLE, JOptionPane.QUESTION_MESSAGE));
				
				if(choice <= 0 || choice > 6){
					JOptionPane.showMessageDialog(null, ERROR_MENU, TITLE, JOptionPane.ERROR_MESSAGE);
					validChoice = false;
				}else{
					if(choice == 1 || choice == 2){
						animalChoice = animalMenu();
					}
					switch(choice){
						case 1:
							if(animalChoice == 1){
								choice = 0;
							}else if(animalChoice == 2){
								choice = 2;
							}else if(animalChoice == 3){
								choice = 4;
							}else if(animalChoice == 4){
								choice = 6;
							}
							break;
						case 2:
							if(animalChoice == 1){
								choice = 1;
							}else if(animalChoice == 2){
								choice = 3;
							}else if(animalChoice == 3){
								choice = 5;
							}else if(animalChoice == 4){
								choice = 7;
							}
							break;
						default:
							choice += modifier;
						}
					
					validChoice = true;
				}
			}catch(NumberFormatException e){
				JOptionPane.showMessageDialog(null, ERROR_NFE, TITLE, JOptionPane.ERROR_MESSAGE);
			}
			
		}while(!validChoice);
		
		return choice;
	}
	
	private static void removeAnimal(Animal[] animals, int adoptedIndex){
		animals[adoptedIndex] = new Animal();
		
		for(int x = adoptedIndex; x < Animal.getAnimalCount(); x++){
			animals[x] = animals[x+1];
		}
		
		writeAnimalToFile(animals);
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
				persons[personIndex].setFirstName(line.substring(lastInfoDiv, divPosition).trim());
				lastInfoDiv = line.indexOf(';') + 1;

				//Finds the next semicolon and cuts the unnecessary parts to only have the last name.
				divPosition = line.indexOf(';', lastInfoDiv);
				persons[personIndex].setLastName(line.substring(lastInfoDiv, divPosition).trim());
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
					lastInfoDiv = line.indexOf(';', divPosition + 1);
					currentCustomer.setPhoneNumber(line.substring(divPosition + 1, lastInfoDiv).trim());
					Customer.setCustomerCount(1);
					
					while(line.lastIndexOf(';') != lastInfoDiv){
						divPosition = line.indexOf(';', lastInfoDiv);
						lastInfoDiv = line.indexOf(';', divPosition + 1);
						currentCustomer.setAdoption(line.substring(divPosition + 1, lastInfoDiv).trim());
					}
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
			Scanner reader = new Scanner(new FileInputStream(Inventory.fileLocation()));
					
			//While loop to iterate until there are no more lines to read from the file.
			while(reader.hasNextLine()){
				lastInfoDiv = 0;
				divPosition = 0;
				//String to hold a line from the file.
				String line = reader.nextLine();
						
				//Finds the first semicolon and cuts the unnecessary parts to only have the first name.
				divPosition = line.indexOf(';') + lastInfoDiv;
				items[itemIndex].setItemName(line.substring(lastInfoDiv, divPosition).trim());
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
	
	private static void readAnimalFile(Animal[] animals){
		//String for the title of all the messages for this method
		final String TITLE = "Animal Shelter Manager";
		//Variable to store the last position of the semicolon used for dividing attributes.
		int lastInfoDiv;
		//Variable to store the position where to start to search for the next element.
		int divPosition;
		int animalIndex = 0;
		
		//Try and catch to prevent the program from crashing if the file is missing or other I/O errors.
		try{
			//Scanner object to read the file information
			Scanner reader = new Scanner(new FileInputStream(Animal.fileLocation()));
					
			//While loop to iterate until there are no more lines to read from the file.
			while(reader.hasNextLine()){
				lastInfoDiv = 0;
				divPosition = 0;
				//String to hold a line from the file.
				String line = reader.nextLine();
						
				//Finds the first semicolon and cuts the unnecessary parts to only have the first name.
				divPosition = line.indexOf(';') + lastInfoDiv;
				animals[animalIndex].setAnimalID((line.substring(lastInfoDiv, divPosition).trim()));
				lastInfoDiv = line.indexOf(';') + 1;

				//Finds the next semicolon and cuts the unnecessary parts to only have the last name.
				animals[animalIndex].setAnimalType((line.substring(lastInfoDiv).trim()));
				Animal.setTypeCount(animals[animalIndex].getAnimalType());
				animalIndex++;
			}
			//After reading the file close the Scanner object.
			reader.close();
			Animal.setAnimalCount(animalIndex);
			
			//File not found exception to display an error message to the user.
			}catch(FileNotFoundException e){
				JOptionPane.showMessageDialog(null, "File was not found.", TITLE, JOptionPane.ERROR_MESSAGE);
			}
	}
	
	private static void writeShelterPersonToFile(ShelterPerson[] persons){
		final String TITLE = "Animal Shelter Menu";
		PrintWriter writer = null;
		int firstIndex = 0;
		
		try{
			if(persons[firstIndex] instanceof Manager){
				writer = new PrintWriter(new FileOutputStream(Manager.fileLocation()));
				
				for(int x = 0; x < Manager.getMaxManagers(); x++){
					Manager currentManager = (Manager)persons[x];
					writer.println(currentManager.toFile());
				}	
			}
			else if(persons[firstIndex] instanceof Employee){
				writer = new PrintWriter(new FileOutputStream(Employee.fileLocation()));
				
				for(int x = 0; x < Employee.getEmployeeCount(); x++){
					Employee currentEmployee = (Employee)persons[x];
					writer.println(currentEmployee.toFile());
				}
			}
			else if(persons[firstIndex] instanceof Customer){
				writer = new PrintWriter(new FileOutputStream(Customer.fileLocation()));
				
				for(int x = 0; x < Customer.getCustomerCount(); x++){
					Customer currentCustomer = (Customer)persons[x];
					writer.println(currentCustomer.toFile());
				}
			}
			
			writer.close();
		}catch(FileNotFoundException e){
			JOptionPane.showMessageDialog(null, "File was not found.", TITLE, JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private static void writeAnimalToFile(Animal[] animals){
		final String TITLE = "Animal Shelter Menu";
		PrintWriter writer = null;
		
		try{
			writer = new PrintWriter(new FileOutputStream(Animal.fileLocation()));
			
			for(int x = 0; x < Animal.getAnimalCount(); x++){
				writer.println(animals[x].toFile());
			}
			
			writer.close();
		}catch(FileNotFoundException e){
			JOptionPane.showMessageDialog(null, "File was not found.", TITLE, JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private static void writeInventoryToFile(Inventory[] items){
		final String TITLE = "Animal Shelter Menu";
		PrintWriter writer = null;
		
		try{
			writer = new PrintWriter(new FileOutputStream(Inventory.fileLocation()));
			
			for(int x = 0; x < Inventory.getMaxItemsCount(); x++){
				writer.println(items[x].toFile());
			}
			
			writer.close();
		}catch(FileNotFoundException e){
			JOptionPane.showMessageDialog(null, "File was not found.", TITLE, JOptionPane.ERROR_MESSAGE);
		}
	}
}