package animalShelterManager;

import javax.swing.JOptionPane;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.util.*;
import java.io.PrintWriter;

public class AnimalShelterManager {

	public static void main(String[] args) {
		//Variables for the login prompt.
		final String TITLE = "Animal Shelter Manager";
		final String[] LOGIN_OPTIONS = {"Manager", "Employee"};
		final String LOGIN_PROMPT = "Select User Type:";
		//Variables to store the user decisions and to control the login prompt loop.
		int choice = 0, terminate = 0;
		
		//Array list set up of for all the objects that will be store inside an array list.
		List<ShelterPerson> shelterManager = new ArrayList<ShelterPerson>();
		List<ShelterPerson> shelterEmployees = new ArrayList<ShelterPerson>();
		List<ShelterPerson> shelterCustomers = new ArrayList<ShelterPerson>();
		List<Animal> shelterAnimals = new ArrayList<Animal>();
		List<Inventory> shelterInventory = new ArrayList<Inventory>();
		
		//ExpenseReport object to store all the transactions done in the current session.
		ExpenseReport currentExpenses = new ExpenseReport("Current Report", 0, 0);
		
		//Method call that reads the file. The array, file path, and an integer to represent type are passed.
		readPersonFile(shelterManager, Manager.fileLocation(), 0);
		readPersonFile(shelterEmployees, Employee.fileLocation(), 1);
		readPersonFile(shelterCustomers, Customer.fileLocation(), 2);
		readInventoryFile(shelterInventory);
		readAnimalFile(shelterAnimals);
		//Set the animalHistory based on the size of the current animals and its own value. Variable used for unique IDs.
		Animal.setAnimalHistory(shelterAnimals.size() + Animal.getAnimalHistory());
		
		//Loop to control the login screen. Only the manager can terminate this loop and thus the program.
		do{
			choice = JOptionPane.showOptionDialog(null, LOGIN_PROMPT, TITLE, 0, JOptionPane.QUESTION_MESSAGE, null, LOGIN_OPTIONS, null);
			//If the user enters to log in as a manager they are prompted for the password.
			if(choice == 0){
				try{
					//If the password is validated correctly the manager password appears.
					((Manager)shelterManager.get(0)).validatePassword(JOptionPane.showInputDialog(null, "Enter the manager password:", TITLE, JOptionPane.QUESTION_MESSAGE));
					//The manager's choice is returned to check if they decided to terminate the system.
					terminate = managerMenu(shelterInventory, shelterAnimals, shelterEmployees, currentExpenses);
				//If the user enters something invalid the error is caught and an appropriate message is displayed.	
				}catch(IllegalArgumentException e){
					JOptionPane.showMessageDialog(null, e.getMessage(), TITLE, JOptionPane.ERROR_MESSAGE);
				}
			}
			//If the user picks to log in as an employee then the employee menu appears.
			else if(choice == 1){
				employeeMenu(shelterCustomers, shelterInventory, shelterAnimals, currentExpenses);
			}
			//If the user tries to terminate the program by pressing X then this error appears.
			else if(choice == -1){
				JOptionPane.showMessageDialog(null, "Only the manager can terminate the system", TITLE, JOptionPane.ERROR_MESSAGE);
			}
			//After the manager or the employee return to the login screen then all the changes are saved to the file.
			writeInventoryToFile(shelterInventory);
			writeAnimalToFile(shelterAnimals);
		}while(terminate != -1);
		//If the manager decides to exit the system then this message is displayed and the program is terminated.
		JOptionPane.showMessageDialog(null, "System Successfully Shutdown", TITLE, JOptionPane.INFORMATION_MESSAGE);
	}
	
	/**
	 * Method to display and control the flow of the manager menu. Most arrays are passed to this method to be passed to other
	 * methods that need the objects to work.
	 * @param items array list of Inventory objects
	 * @param animals array list of Animal objects
	 * @param employees array list of Employees objects
	 * @param current ExpenseReport object
	 * @return
	 */
	private static int managerMenu(List<Inventory> items, List<Animal> animals, List<ShelterPerson> employees, ExpenseReport current){
		//Variables that contain the messages to be display to the user. Including the menu and error messages.
		final String TITLE = "Animal Shelter Manager Menu";
		final String PROMPT = "Select an option from the menu:\n";
		final String[] OPTIONS = {"  1) Check Inventory\n", "  2) Order Supplies\n",
							"  3) Update Employees\n", "  4) Expense Report\n", "  5) Exit\n", "  6) Terminate System\n"};
		final String ERROR_NFE = "Please enter a number to select an option.";
		final String ERROR_MENU = "Please select an option from the menu.";
		//Variables to control the managerMenu loop and the system loop.
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
						orderSupplies(items, current);
						break;
					case 3:
						employeeManagement(employees);
						break;
					case 4:
						expenseManagement(items, animals, employees, current);
						break;
					case 5:
						JOptionPane.showMessageDialog(null, "Returning to Login Screen", TITLE, JOptionPane.INFORMATION_MESSAGE);
						break;
					case 6:
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
	
	/**
	 * Method to display the current inventory to the manager. A for loop goes through all the elements
	 * inside the Inventory array list and gets the name and count of each to be put in a String. Finally,
	 * the a message is displayed with the name and count of all the items in the animal shelter. 
	 * @param items
	 */
	private static void checkInventory(List<Inventory> items){
		final String TITLE = "Animal Shelter Inventory Menu";
		String header = "Inventory Screen:\n";
		String inventoryPrint = "";
		
		for(Inventory i : items){
			inventoryPrint += "  " + i.getItemName() + ": " + i.getItemCount() + "\n";
		}
		
		String total = "Total: " + Inventory.getInventoryCount() + "\n";
		String max = "Max: " + Inventory.getMaxInventory() + "\n";
		JOptionPane.showMessageDialog(null, header + inventoryPrint + total + max, TITLE, JOptionPane.INFORMATION_MESSAGE);
	}
	
	/**
	 * Method to allow the manager to order supplies. 
	 * @param items
	 * @param current
	 */
	private static void orderSupplies(List<Inventory> items, ExpenseReport current){
		//TODO Compare with purchase item and maybe only use 1 method to handle the order/purchasing of items.
		//Main differences are the methods use different prices and one adds to the inventory and the other subtracts.
		final String TITLE = "Animal Shelter Purchase Menu";
		int purchaseCount = 0, buyMore = 0, item = 0;
		String receipt = "", quantityPrompt = "", morePurchasesPrompt = "Would you like to make another purchase?", header = "";
		double total = 0;
		String[] itemNames = new String[Inventory.getMaxItemsCount()];
		int[] itemQuan = new int[Inventory.getMaxItemsCount()];
		double[] itemPrices = new double[Inventory.getMaxItemsCount()];
		
		do{
			item = inventoryMenu(items);
			quantityPrompt = "Enter the number of " + items.get(item).getItemName() + " to purchase:";
			itemNames[purchaseCount] = items.get(item).getItemName();
		
			try{
				itemQuan[purchaseCount] = Integer.parseInt(JOptionPane.showInputDialog(null, quantityPrompt, TITLE, JOptionPane.QUESTION_MESSAGE));
				itemPrices[purchaseCount] = items.get(item).inventoryPurchase((itemQuan[purchaseCount]));
				
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
		current.setMonthExpense(total);
	}
	
	private static void employeeManagement(List<ShelterPerson> employees){
		final String TITLE = "Animal Shelter Manager Menu";
		final String PROMPT = "Select an option from the menu:\n";
		final String[] OPTIONS = {"  1) List Employees\n", "  2) Add Employee\n",
				"  3) Remove Employee\n", "  4) Exit\n"};
		final String ERROR_NFE = "Please enter a number to select an option.";
		final String ERROR_MENU = "Please select an option from the menu.";
		final String MAX_EMPLOYEES = "The max number of employees has been reached.\n No more employees can be added.";
		int choice = 0;
		
		do{
			try{
				choice = Integer.parseInt(JOptionPane.showInputDialog(null, PROMPT + OPTIONS[0] + OPTIONS[1] + OPTIONS[2]
						+ OPTIONS[3], TITLE, JOptionPane.QUESTION_MESSAGE));
				
				switch(choice){
					case 1:
						displayEmployees(employees);
						break;
					case 2:
						if(Employee.getEmployeeCount() == Employee.getMaxEmployees()){
							JOptionPane.showMessageDialog(null, MAX_EMPLOYEES, TITLE, JOptionPane.ERROR_MESSAGE);
						}
						else{
							addEmployee(employees);
						}
						break;
					case 3:
						removeEmployee(employees);
						break;
					case 4:
						break;
					default:
						JOptionPane.showMessageDialog(null, ERROR_MENU, TITLE, JOptionPane.ERROR_MESSAGE);
				}
			}catch(NumberFormatException e){
				JOptionPane.showMessageDialog(null, ERROR_NFE, TITLE, JOptionPane.ERROR_MESSAGE);
			}
			writeShelterPersonToFile(employees);
		}while(choice != 4);
	}
	
	private static void expenseManagement(List<Inventory> items, List<Animal> animals, List<ShelterPerson> employees, ExpenseReport current){
		final String TITLE = "Animal Shelter Expense Menu";
		final String PROMPT = "Select an option from the menu:\n";
		final String[] OPTIONS = {"  1) Display current report\n", "  2) Display 12 month simulation\n", "  3) Display cost of one animal\n"
				, "  4) Display cost of a group of animals\n", "  5) Exit"};
		final String ERROR_NFE = "Please enter a number to select an option.";
		final String ERROR_MENU = "Please select an option from the menu.";
		int choice = 0;
		
		do{
			try{
				choice = Integer.parseInt(JOptionPane.showInputDialog(null, PROMPT + OPTIONS[0] + OPTIONS[1] + OPTIONS[2]
						+ OPTIONS[3] + OPTIONS[4], TITLE, JOptionPane.QUESTION_MESSAGE));
					
				switch(choice){
					case 1:
						JOptionPane.showMessageDialog(null, current.toString(), TITLE, JOptionPane.INFORMATION_MESSAGE);
						break;
					case 2:
						yearSimulation(current, items);
						break;
					case 3:
						animalExpense(false, items, animals);
						break;
					case 4:
						animalExpense(true, items, animals);
						break;
					case 5:
						break;
					default:
						JOptionPane.showMessageDialog(null, ERROR_MENU, TITLE, JOptionPane.ERROR_MESSAGE);
				}
			}catch(NumberFormatException e){
				JOptionPane.showMessageDialog(null, ERROR_NFE, TITLE, JOptionPane.ERROR_MESSAGE);
			}
		}while(choice != 5);
	}
	
	private static void employeeMenu(List<ShelterPerson> customers, List<Inventory> items, List<Animal> animals, ExpenseReport current){
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
						animalAdoption(customers, animals, items, current);
						break;
					case 2:
						addAnimal(animals);
						break;
					case 3:
						purchaseItem(items, current);
						break;
					case 4:
						animalPopulation(animals);
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
	
	private static void animalAdoption(List<ShelterPerson> customers, List<Animal> animals,  List<Inventory> items, ExpenseReport current){
		final String TITLE = "Animal Shelter Adooption Menu";
		int foundIndex = 0;
		String phoneNum = "";
		boolean customerFound = false;
		boolean valid = true;
		
		do{
			try{
				valid = true;	
				phoneNum = JOptionPane.showInputDialog(null, "Enter the customer's phone number:", TITLE, JOptionPane.QUESTION_MESSAGE);
				Customer.validatePhoneNumber(phoneNum);
				
				for(ShelterPerson c : customers){
					if(c instanceof Customer){
						if(((Customer)c).getPhoneNumber().equals(phoneNum)){
							foundIndex = customers.indexOf(c);
							customerFound = true;
						}
					}
				}
				
				if(customerFound){
					JOptionPane.showMessageDialog(null, "Welcome Back " + customers.get(foundIndex).getFirstName() + "!");
					adoptAnimal((Customer)customers.get(foundIndex), animals, items, current);
				}
				else if(!customerFound){
					JOptionPane.showMessageDialog(null, "Customer information not found.\n A new account must be created.");
					createNewCustomer(customers, phoneNum);
					adoptAnimal((Customer)customers.get(foundIndex), animals, items, current);
				}
			
				writeShelterPersonToFile(customers);
				}catch(IllegalArgumentException e){
					JOptionPane.showMessageDialog(null, e.getMessage(), TITLE, JOptionPane.ERROR_MESSAGE);
					valid = false;
				}
		}while(!valid);
	}
	
	private static void addAnimal(List<Animal> animals){
		final String TITLE = "Animal Shelter Employee Menu";
		String message = ""; 
		Animal newAnimal = new Animal();
		
		Animal.setAnimalHistory(Animal.getAnimalHistory() + 1);
		newAnimal.setAnimalType(Animal.getAnimalTypes(animalMenu()));
		newAnimal.setAnimalID(Animal.createAnimalID(newAnimal.getAnimalType()));
		
		message = " A new " + newAnimal.getAnimalType() + " has been added!\n ID: " + newAnimal.getAnimalID();
		animals.add(newAnimal);
		JOptionPane.showMessageDialog(null, message, TITLE, JOptionPane.INFORMATION_MESSAGE);
	}
	
	private static void purchaseItem( List<Inventory> items, ExpenseReport current){
		final String TITLE = "Animal Shelter Purchase Menu";
		int purchaseCount = 0, buyMore = 0, item = 0;
		String receipt = "", quantityPrompt = "", morePurchasesPrompt = "Would you like to make another purchase?", header = "";
		double total = 0;
		String[] itemNames = new String[Inventory.getMaxItemsCount()];
		int[] itemQuan = new int[Inventory.getMaxItemsCount()];
		double[] itemPrices = new double[Inventory.getMaxItemsCount()];
		
		do{
			item = inventoryMenu(items);
			quantityPrompt = "Enter the number of " + items.get(item).getItemName() + " to purchase:";
			itemNames[purchaseCount] = items.get(item).getItemName(); 
		
			try{
				itemQuan[purchaseCount] = Integer.parseInt(JOptionPane.showInputDialog(null, quantityPrompt, TITLE, JOptionPane.QUESTION_MESSAGE));
				itemPrices[purchaseCount] = items.get(item).inventorySell(itemQuan[purchaseCount]);
				
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
		current.setMonthProfit(total);
	}
	
	private static void animalPopulation(List<Animal> animals){
		final String TITLE = "Animal Shelter Population Menu";
		String header = "Animal Population:\n";
		String population = "";
		
		for(int x = 0; x < Animal.getAnimalTypesCount(); x++){
			population += "  " + Animal.getAnimalTypes(x) + " Population: " + animalTypeCount(animals, x) + "\n"; 
		}
		
		String currentPop = "  Current Population: " + animals.size() + "\n";
		String maxPop = "  Max Population: " + Animal.getMaxAnimals();
		
		JOptionPane.showMessageDialog(null, header + population + currentPop + maxPop, TITLE, JOptionPane.INFORMATION_MESSAGE);
	}
	
	private static void createNewCustomer(List<ShelterPerson> customers, String phone){
		final String TITLE = "Animal Shelter Adooption Menu", FNAME_PROMPT = "Enter the customer's first name:",
						LNAME_PROMPT = "Enter the customer's last name:";
		boolean valid = true;
		int promptLevel = 0;
		customers.add(new Customer());
		
		do{
			valid = true;
			try{
				((Customer)customers.get(customers.size()-1)).setPhoneNumber(phone);
				if(promptLevel == 0){
					((Customer)customers.get(customers.size()-1)).setFirstName(JOptionPane.showInputDialog(null, FNAME_PROMPT, TITLE, JOptionPane.QUESTION_MESSAGE));
					promptLevel = 1;
				}
				if(promptLevel == 1){
					((Customer)customers.get(customers.size()-1)).setLastName(JOptionPane.showInputDialog(null, LNAME_PROMPT, TITLE, JOptionPane.QUESTION_MESSAGE));
				}
				
			}catch(IllegalArgumentException e){
				JOptionPane.showMessageDialog(null, e.getMessage(), TITLE, JOptionPane.ERROR_MESSAGE);
				valid = false;
			}
		}while(!valid);
	}
	
	private static void adoptAnimal(Customer oldCustomer, List<Animal> animals,  List<Inventory> items, ExpenseReport current){
		final String TITLE = "Animal Shelter Adooption Menu";
		final int PURCHASE_REQUIREMENT = 2;
		int choice = animalMenu();
		double foodCost = 0;
		double medicineCost = 0;
		double total = 0;
		String animalType = Animal.getAnimalTypes(choice), adoptionMessage = "The following ", adoptionMessageCont = "";
		
		for(Inventory i : items){
			if(i.getItemName().contains(animalType + " F")){
				foodCost = (i.getItemSellCost() * PURCHASE_REQUIREMENT);
			}
			if(i.getItemName().contains(animalType + " M")){
				medicineCost = (i.getItemSellCost() * PURCHASE_REQUIREMENT);
				break;
			}
		}
		
		total = foodCost + medicineCost;
		adoptionMessage += animalType + " is available for adoption.\n ID: ";
		adoptionMessageCont = "\n Food Cost: " + String.format("$%,.2f", foodCost) + "\n"
				+ " Medicine Cost: " + String.format("$%,.2f", medicineCost) + "\n Total: " + String.format("$%,.2f", total);
		
		for(Animal a : animals){
			if(a.getAnimalType().equals(animalType)){
				oldCustomer.adoptAnimal(a);
				JOptionPane.showMessageDialog(null, adoptionMessage + a.getAnimalID() + adoptionMessageCont,
						TITLE, JOptionPane.INFORMATION_MESSAGE);
				animals.remove(a);
				break;
			}
		}
		current.setMonthProfit(total);
	}
	
	private static int animalTypeCount(List<Animal> animals, int index){
		int[] counts = {0, 0, 0, 0};
		
		for(Animal a : animals){
			if(a.getAnimalType().equals(Animal.getAnimalTypes(0))){
				counts[0]++;
			}
			else if(a.getAnimalType().equals(Animal.getAnimalTypes(1))){
				counts[1]++;
			}
			else if(a.getAnimalType().equals(Animal.getAnimalTypes(2))){
				counts[2]++;
			}
			else if(a.getAnimalType().equals(Animal.getAnimalTypes(3))){
				counts[3]++;
			}
		}
		return counts[index];
	}
	
	private static void displayEmployees(List<ShelterPerson> employees){
		final String TITLE = "Animal Shelter Manager Menu";
		String employeeList = "List of Employees:\n";
		
		if(Employee.getEmployeeCount() == 0){
			employeeList = "There are no employees.";
		}
		else{
			for(ShelterPerson e : employees){
				if(e instanceof Employee){
					employeeList += "  " + "- " + ((Employee)e).toString() + "\n"; 
				}
			}
		}
		
		JOptionPane.showMessageDialog(null, employeeList, TITLE, JOptionPane.INFORMATION_MESSAGE);
	}
	
	private static void addEmployee(List<ShelterPerson> employees){
		final String TITLE = "Animal Shelter Manager Menu";
		final String FNAME_PROMPT = "Enter the new employee's first name:";
		final String LNAME_PROMPT = "Enter the new employee's last name:";
		final String ID_PROMPT = "Enter the ID of the new employee:";
		final int INCREASE_COUNT = 1, EMPLOYEE_COUNT = Employee.getEmployeeCount();
		boolean valid = true;
		int promptLevel = 0;
		employees.add(new Employee());
		
		do{
			valid = true;
			try{
				if(promptLevel == 0){
					employees.get(EMPLOYEE_COUNT).setFirstName(JOptionPane.showInputDialog(null, FNAME_PROMPT, TITLE, JOptionPane.QUESTION_MESSAGE));
					promptLevel = 1;
				}
				if(promptLevel == 1){
					employees.get(EMPLOYEE_COUNT).setLastName(JOptionPane.showInputDialog(null, LNAME_PROMPT, TITLE, JOptionPane.QUESTION_MESSAGE));
					promptLevel = 2;
				}
				if(promptLevel == 2){
					((Employee)employees.get(EMPLOYEE_COUNT)).setEmployeeId(JOptionPane.showInputDialog(null, ID_PROMPT, TITLE, JOptionPane.QUESTION_MESSAGE));
					Employee.setEmployeeCount(EMPLOYEE_COUNT + INCREASE_COUNT);
				}
			}catch(IllegalArgumentException e){
				JOptionPane.showMessageDialog(null, e.getMessage(), TITLE, JOptionPane.ERROR_MESSAGE);
				valid = false;
			}
		}while(!valid);
	}
	
	private static void removeEmployee(List<ShelterPerson> employees){
		final String TITLE = "Animal Shelter Manager Menu";
		final String ERROR_NFE = "Please enter a number to select an option.";
		final String ERROR_MENU = "Please select an option from the menu.";
		String employeeList = "Select an employee to remove:\n";
		int removeThis = 0, counter = 0;
		boolean valid = true;
		if(Employee.getEmployeeCount() == 0){
			employeeList = "There are no employees.";
		}
		else{
			for(ShelterPerson e : employees){
				if(e instanceof Employee){
					employeeList += "  " + (counter+1) + ") " + e.getFirstName() + " " + e.getLastName() +"\n";
				}
				counter++;
			}
			counter++;
			employeeList += "  " + (counter) + ") Exit";
			do{
				try{
					removeThis = Integer.parseInt(JOptionPane.showInputDialog(null, employeeList, TITLE, JOptionPane.INFORMATION_MESSAGE));
				
					if(removeThis > 0 && removeThis <= Employee.getEmployeeCount()){
						employees.remove(removeThis - 1);
						JOptionPane.showMessageDialog(null, "Employee List Updated!", TITLE, JOptionPane.INFORMATION_MESSAGE);
						Employee.setEmployeeCount(Employee.getEmployeeCount() - 1);
						valid = true;
					}
					else if(removeThis == counter){
						valid = true;
					}
					else{
						JOptionPane.showMessageDialog(null, ERROR_MENU, TITLE, JOptionPane.ERROR_MESSAGE);
						valid = false;
					}
				}catch(NumberFormatException e){
					JOptionPane.showMessageDialog(null, ERROR_NFE, TITLE, JOptionPane.ERROR_MESSAGE);
				}
			}while(!valid);
		}
	}
	
	private static void yearSimulation(ExpenseReport current, List<Inventory> items){
		final String TITLE = "Animal Shelter 12-Month Simulation";
		final String ITEM_PROMPT = "Enter the expected percentage of merchandise to be sold:";
		final String ANIMAL_PROMPT = "Enter the expected number of animals in the shelter for the year:";
		final String ANIMAL_PROMPT2 = "Enter the expected number of animals to be adopted for the year:";
		final String ERROR_NFE = "Please enter a positive number as the percentage.";
		final int MAX_YEARLY_ANIMALS = 1100;
		final int MONTHS_NUM = 12;
		
		String simulation = "12 Month Simulation:\n";
		double inventoryAverageExpenses = 0;
		double inventoryAverageProfit = 0;
		double employeeExpense = 0;
		double percentToSell = 0;
		double monthTotalExpense = 0;
		double monthTotalProfit = 0;
		double averageAnimalExpense = 0;
		double averageAnimalProfit = 0;
		int expectedAnimals = 0, expectedAdoptions = 0, promptLevel = 0;;
		boolean valid = true;
		ExpenseReport[] yearSimulation = new ExpenseReport[MONTHS_NUM];
		
		for(int i = 0; i < Inventory.getMaxItemsCount(); i++){
			if(i >= 0 && i <= 7){
				averageAnimalExpense += items.get(i).getItemPurchaseCost();
				averageAnimalProfit += items.get(i).getItemSellCost();
			}
			inventoryAverageExpenses += items.get(i).getItemPurchaseCost();
			inventoryAverageProfit += items.get(i).getItemSellCost();
		}
		
		inventoryAverageExpenses = (inventoryAverageExpenses / Inventory.getMaxItemsCount());
		inventoryAverageProfit = (inventoryAverageProfit / Inventory.getMaxItemsCount());
		employeeExpense = ExpenseReport.calculateEmployeeExpense(Employee.getEmployeeCount(), Employee.getEmployeePay()) * 2;
		averageAnimalExpense = (averageAnimalExpense / 8); 
		averageAnimalProfit = (averageAnimalProfit / 8); 
		
		
		for(int x = 0; x < MONTHS_NUM; x++){
			monthTotalExpense = 0;
			monthTotalProfit = 0;

			if(x == 0){
				yearSimulation[x] = new ExpenseReport("Month " + (x+1), current.getMonthProfit(), current.getMonthExpense());
			}
			else{
				do{
					valid = true;
					try{
						if(promptLevel == 0){
							expectedAnimals = Integer.parseInt(JOptionPane.showInputDialog(null, ANIMAL_PROMPT, TITLE, JOptionPane.QUESTION_MESSAGE));
							if(expectedAnimals < 0 || expectedAnimals <= MAX_YEARLY_ANIMALS){
								averageAnimalExpense = (averageAnimalExpense * expectedAnimals);
								promptLevel = 1;
							}else{
								JOptionPane.showMessageDialog(null, "The number of expected animals must be positive and less than " +  MAX_YEARLY_ANIMALS,
											TITLE, JOptionPane.ERROR_MESSAGE);
							}
						}
						
						if(promptLevel == 1){
							expectedAdoptions = Integer.parseInt(JOptionPane.showInputDialog(null, ANIMAL_PROMPT2, TITLE, JOptionPane.QUESTION_MESSAGE));
							if(expectedAnimals < 0 || expectedAnimals <= MAX_YEARLY_ANIMALS){
								averageAnimalProfit = (averageAnimalProfit * expectedAdoptions);
								promptLevel = 2;
							}else{
								JOptionPane.showMessageDialog(null, "The number of expected adoptions must be positive and less than " +  MAX_YEARLY_ANIMALS,
											TITLE, JOptionPane.ERROR_MESSAGE);
							}
						}
						
						if(promptLevel == 2){
							percentToSell = Double.parseDouble(JOptionPane.showInputDialog(null, ITEM_PROMPT, TITLE, JOptionPane.QUESTION_MESSAGE));
						}
						
						monthTotalExpense =  (ExpenseReport.simulateMonthProft(percentToSell, inventoryAverageProfit) + (employeeExpense + averageAnimalExpense));
						monthTotalProfit = ExpenseReport.simulateMonthExpense(percentToSell, inventoryAverageExpenses) + averageAnimalProfit;
						
						yearSimulation[x] = new ExpenseReport("Month " + (x+1), monthTotalProfit, monthTotalExpense);
					}catch(NumberFormatException e){
						JOptionPane.showMessageDialog(null, ERROR_NFE, TITLE, JOptionPane.ERROR_MESSAGE);
						valid = false;
					}
				}while(!valid);
			}
		}
		
		for(int x = 0; x < MONTHS_NUM; x++){
			simulation += yearSimulation[x].toString() + "\n";
		}
		
		JOptionPane.showMessageDialog(null, simulation, TITLE, JOptionPane.INFORMATION_MESSAGE);
		if(JOptionPane.showConfirmDialog(null, "Save simulation to a text file?", TITLE,
				JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
			writeSimulationToFile(yearSimulation);
		}
	}
	
	private static void animalExpense(boolean multiple, List<Inventory> items, List<Animal> animals){
		final String TITLE = "Animal Shelter Manager Menu";
		String header = "Cost of ";
		String animalType = "";
		int animalChoice = 0;
		int animalCount = 0;
		double foodCost = 0;
		double medicineCost = 0;
		double totalCost = 0;
		
		animalChoice = animalMenu();
		animalType = Animal.getAnimalTypes(animalChoice);
		
		for(Inventory i : items){
			if(i.getItemName().contains(animalType + " F")){
				foodCost = (i.getItemPurchaseCost() * ExpenseReport.getFoodMultiplier());
			}
			if(i.getItemName().contains(animalType + " M")){
				medicineCost = (i.getItemPurchaseCost() * ExpenseReport.getMedicineMultiplier());
				break;
			}
		}
		
		if(multiple){
			animalCount = animalTypeCount(animals, animalChoice);
			totalCost = ExpenseReport.calculateAnimalExpense(animalCount, foodCost, medicineCost);
			header += "the " + animalType + " population per month:\n Population: " + animalCount + "\n";
		}
		else{
			animalCount = 1;
			totalCost = ExpenseReport.calculateAnimalExpense(animalCount, foodCost, medicineCost);
			header += "a single " + animalType + " per month:\n";
		}
		
		JOptionPane.showMessageDialog(null, header + "  Food cost per month: " + String.format("$%,.2f", (foodCost * animalCount)) + "\n" +
					"  Medicine cost per month: " + String.format("$%,.2f", (medicineCost * animalCount)) + "\n" + "  Total: " + 
					String.format("$%,.2f", totalCost) + "\n", TITLE, JOptionPane.INFORMATION_MESSAGE);
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
		
		return choice - 1;
	}
	
	private static int inventoryMenu(List<Inventory> items){
		final String TITLE = "Animal Shelter Employee Menu";
		final String PROMPT = "Select an item:\n";
		final String[] PRODUCTS = {"Food", "Medicine", "Food Bowls", "Leashes", "Collars", "Toys"};
		final String[] OPTIONS = {"  1) " + PRODUCTS[0] +"\n", "  2) " + PRODUCTS[1] +"\n", "  3) " + PRODUCTS[2] +"\n",
				"  4) " + PRODUCTS[3] + "\n", "  5) " + PRODUCTS[4] + "\n", "  6) " + PRODUCTS[5] + "\n"};
		final String ERROR_NFE = "Please enter a number to select an option.";
		final String ERROR_MENU = "Please select an option from the menu.";
		int choice = 0;
		String item = "";
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
						item = Animal.getAnimalTypes(animalMenu()) + " " + PRODUCTS[choice - 1];
					}else{
						item = PRODUCTS[choice - 1];
					}
					for(Inventory i : items){
						if(i.getItemName().equals(item)){
							choice = items.indexOf(i);
							break;
						}
					}
					validChoice = true;
				}
			}catch(NumberFormatException e){
				JOptionPane.showMessageDialog(null, ERROR_NFE, TITLE, JOptionPane.ERROR_MESSAGE);
			}
			
		}while(!validChoice);
		
		return choice;
	}
	
	private static void readPersonFile(List<ShelterPerson> persons, String filePath, int type){
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
				if(type == 0){
					persons.add(new Manager());
				}
				else if(type == 1){
					persons.add(new Employee());
				}
				else if(type == 2){
					persons.add(new Customer());
				}
				lastInfoDiv = 0;
				divPosition = 0;
				//String to hold a line from the file.
				String line = reader.nextLine();
				
				//Finds the first semicolon and cuts the unnecessary parts to only have the first name.
				divPosition = line.indexOf(';') + lastInfoDiv;
				persons.get(personIndex).setFirstName(line.substring(lastInfoDiv, divPosition).trim());
				lastInfoDiv = line.indexOf(';') + 1;

				//Finds the next semicolon and cuts the unnecessary parts to only have the last name.
				divPosition = line.indexOf(';', lastInfoDiv);
				persons.get(personIndex).setLastName(line.substring(lastInfoDiv, divPosition).trim());
				lastInfoDiv = line.indexOf(';', divPosition);
				
				if(persons.get(personIndex) instanceof Manager){
					divPosition = line.indexOf(';', lastInfoDiv);
					((Manager)persons.get(personIndex)).setPassword(line.substring(divPosition + 1).trim());
				}
				else if(persons.get(personIndex)instanceof Employee){
					divPosition = line.indexOf(';', lastInfoDiv);
					((Employee)persons.get(personIndex)).setEmployeeId(line.substring(divPosition + 1).trim());
					Employee.setEmployeeCount(personIndex + 1);
				}
				else if(persons.get(personIndex) instanceof Customer){
					divPosition = line.indexOf(';', lastInfoDiv);
					lastInfoDiv = line.indexOf(';', divPosition + 1);
					((Customer)persons.get(personIndex)).setPhoneNumber(line.substring(divPosition + 1, lastInfoDiv).trim());
					
					while(line.lastIndexOf(';') != lastInfoDiv){
						divPosition = line.indexOf(';', lastInfoDiv);
						lastInfoDiv = line.indexOf(';', divPosition + 1);
						((Customer)persons.get(personIndex)).setAdoption(line.substring(divPosition + 1, lastInfoDiv).trim());
						Animal.setAnimalHistory(Animal.getAnimalHistory() + 1);
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
	
	private static void readInventoryFile(List<Inventory> items){
		//String for the title of all the messages for this method
		final String TITLE = "Animal Shelter Manager";
		//Variable to store the last position of the semicolon used for dividing attributes.
		int lastInfoDiv;
		//Variable to store the position where to start to search for the next element.
		int divPosition;
		
		//Try and catch to prevent the program from crashing if the file is missing or other I/O errors.
		try{
			//Scanner object to read the file information
			Scanner reader = new Scanner(new FileInputStream(Inventory.fileLocation()));
					
			//While loop to iterate until there are no more lines to read from the file.
			while(reader.hasNextLine()){
				lastInfoDiv = 0;
				divPosition = 0;
				Inventory temp = new Inventory();
				//String to hold a line from the file.
				String line = reader.nextLine();
						
				//Finds the first semicolon and cuts the unnecessary parts to only have the first name.
				divPosition = line.indexOf(';') + lastInfoDiv;
				temp.setItemName(line.substring(lastInfoDiv, divPosition).trim());
				lastInfoDiv = line.indexOf(';') + 1;

				//Finds the next semicolon and cuts the unnecessary parts to only have the last name.
				divPosition = line.indexOf(';', lastInfoDiv);
				temp.setItemPurchasePrice(((Double.parseDouble(line.substring(lastInfoDiv, divPosition).trim()))));
				lastInfoDiv = line.indexOf(';', divPosition) + 1;
				
				divPosition = line.indexOf(';', lastInfoDiv);
				temp.setItemSellPrice(((Double.parseDouble(line.substring(lastInfoDiv, divPosition).trim()))));
				lastInfoDiv = line.indexOf(';', divPosition);
				
				divPosition = line.indexOf(';', lastInfoDiv);
				temp.setItemCount(((Integer.parseInt(line.substring(divPosition + 1).trim()))));
				Inventory.setInventoryCount(temp.getItemCount());
				items.add(temp);
			}
			//After reading the file close the Scanner object.
			reader.close();
			//File not found exception to display an error message to the user.
			}catch(FileNotFoundException e){
				JOptionPane.showMessageDialog(null, "File was not found.", TITLE, JOptionPane.ERROR_MESSAGE);
			}
	}
	
	private static void readAnimalFile(List<Animal> animals){
		//String for the title of all the messages for this method
		final String TITLE = "Animal Shelter Manager";
		//Variable to store the last position of the semicolon used for dividing attributes.
		int lastInfoDiv;
		//Variable to store the position where to start to search for the next element.
		int divPosition;
		
		
		//Try and catch to prevent the program from crashing if the file is missing or other I/O errors.
		try{
			//Scanner object to read the file information
			Scanner reader = new Scanner(new FileInputStream(Animal.fileLocation()));
					
			//While loop to iterate until there are no more lines to read from the file.
			while(reader.hasNextLine()){
				lastInfoDiv = 0;
				divPosition = 0;
				Animal temp = new Animal();
				//String to hold a line from the file.
				String line = reader.nextLine();
				
				//Finds the first semicolon and cuts the unnecessary parts to only have the first name.
				divPosition = line.indexOf(';') + lastInfoDiv;
				temp.setAnimalID((line.substring(lastInfoDiv, divPosition).trim()));
				lastInfoDiv = line.indexOf(';') + 1;

				//Finds the next semicolon and cuts the unnecessary parts to only have the last name.
				temp.setAnimalType((line.substring(lastInfoDiv).trim()));
				animals.add(temp);
			}
			//After reading the file close the Scanner object.
			reader.close();
			
			//File not found exception to display an error message to the user.
			}catch(FileNotFoundException e){
				JOptionPane.showMessageDialog(null, "File was not found.", TITLE, JOptionPane.ERROR_MESSAGE);
			}
	}
	
	private static void writeShelterPersonToFile(List<ShelterPerson> persons){
		final String TITLE = "Animal Shelter Menu";
		PrintWriter writer = null;
		int firstIndex = 0;
		
		try{
			if(persons.get(firstIndex) instanceof Manager){
				writer = new PrintWriter(new FileOutputStream(Manager.fileLocation()));
				for(ShelterPerson m : persons){
					writer.println(((Manager)m).toFile());
				}
			}
			else if(persons.get(firstIndex) instanceof Employee){
				writer = new PrintWriter(new FileOutputStream(Employee.fileLocation()));
				
				for(ShelterPerson e : persons){
					writer.println(((Employee)e).toFile());
				}
			}
			else if(persons.get(firstIndex) instanceof Customer){
				writer = new PrintWriter(new FileOutputStream(Customer.fileLocation()));
				
				for(ShelterPerson e : persons){
					writer.println(((Customer)e).toFile());
				}
			}
			
			writer.close();
		}catch(FileNotFoundException e){
			JOptionPane.showMessageDialog(null, "File was not found.", TITLE, JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private static void writeAnimalToFile(List<Animal> animals){
		final String TITLE = "Animal Shelter Menu";
		PrintWriter writer = null;
		
		try{
			writer = new PrintWriter(new FileOutputStream(Animal.fileLocation()));
			
			for(Animal a : animals){
				writer.println(a.toFile());
			}
			
			writer.close();
		}catch(FileNotFoundException e){
			JOptionPane.showMessageDialog(null, "File was not found.", TITLE, JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private static void writeInventoryToFile(List<Inventory> items){
		final String TITLE = "Animal Shelter Menu";
		PrintWriter writer = null;
		
		try{
			writer = new PrintWriter(new FileOutputStream(Inventory.fileLocation()));
			
			for(Inventory i : items){
				writer.println(i.toFile());
			}
			
			writer.close();
		}catch(FileNotFoundException e){
			JOptionPane.showMessageDialog(null, "File was not found.", TITLE, JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private static void writeSimulationToFile(ExpenseReport[] simulation){
		final String TITLE = "Animal Shelter Menu";
		PrintWriter writer = null;
		
		try{
			writer = new PrintWriter(new FileOutputStream(ExpenseReport.fileLocation()));
			
			for(int x = 0; x < simulation.length; x++){
				writer.println(simulation[x].toString());
			}
			
			writer.close();
		}catch(FileNotFoundException e){
			JOptionPane.showMessageDialog(null, "File was not found.", TITLE, JOptionPane.ERROR_MESSAGE);
		}
	}
}