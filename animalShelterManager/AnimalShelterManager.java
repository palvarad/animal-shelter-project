package animalShelterManager;

import javax.swing.JOptionPane;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.util.*;
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
		
		List<Animal> shelterAnimals = new LinkedList<Animal>();
		
		ExpenseReport currentExpenses = new ExpenseReport("Current Report", 0, 0);
		
		//Method call that reads the file. The array and file path are passed.
		readPersonFile(shelterManager, Manager.fileLocation());
		readPersonFile(shelterEmployees, Employee.fileLocation());
		readPersonFile(shelterCustomers, Customer.fileLocation());
		readInventoryFile(shelterInventory);
		readAnimalFile(shelterAnimals);
		Animal.setAnimalHistory(Animal.getAnimalCount() + Animal.getAnimalHistory());
		
		do{
			choice = JOptionPane.showOptionDialog(null, LOGIN_PROMPT, TITLE, 0, JOptionPane.QUESTION_MESSAGE, null, LOGIN_OPTIONS, null);
			
			if(choice == 0){
				try{
					Manager currentManager = (Manager)shelterManager[0];
					currentManager.validatePassword(JOptionPane.showInputDialog(null, "Enter the manager password:", TITLE, JOptionPane.QUESTION_MESSAGE));
					terminate = managerMenu(shelterInventory, shelterAnimals, shelterEmployees, currentExpenses);
					
				}catch(IllegalArgumentException e){
					JOptionPane.showMessageDialog(null, e.getMessage(), TITLE, JOptionPane.ERROR_MESSAGE);
				}
			}
			else if(choice == 1){
				employeeMenu(shelterCustomers, shelterInventory, shelterAnimals, currentExpenses);
			}
			else if(choice == -1){
				JOptionPane.showMessageDialog(null, "Only the manager can terminate the system", TITLE, JOptionPane.ERROR_MESSAGE);
			}
			writeInventoryToFile(shelterInventory);
			writeAnimalToFile(shelterAnimals);
			
		}while(terminate != -1);
	}
	
	private static int managerMenu(Inventory[] items, List<Animal> animals, ShelterPerson[] employees, ExpenseReport current){
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
	
	private static void employeeMenu(ShelterPerson[] customers, Inventory[] items, List<Animal> animals, ExpenseReport current){
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
	
	private static void animalAdoption(ShelterPerson[] customers, List<Animal> animals, Inventory[] items, ExpenseReport current){
		final String TITLE = "Animal Shelter Adooption Menu";
		int foundIndex = 0;
		String phoneNum = "";
		boolean customerFound = false;
		Customer tempCustomer = null;
		boolean valid = true;
		
		do{
			try{
				valid = true;	
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
					adoptAnimal((Customer)customers[foundIndex], animals, items, current);
				}
				else if(!customerFound){
					JOptionPane.showMessageDialog(null, "Customer information not found.\n A new account must be created.");
					createNewCustomer((Customer)customers[Customer.getCustomerCount()], phoneNum);
					adoptAnimal((Customer)customers[Customer.getCustomerCount() - 1], animals, items, current);
				}
			
				writeShelterPersonToFile(customers);
				}catch(IllegalArgumentException e){
					JOptionPane.showMessageDialog(null, e.getMessage(), TITLE, JOptionPane.ERROR_MESSAGE);
					valid = false;
				}
		}while(!valid);
	}
	
	private static void createNewCustomer(Customer newCustomer, String phone){
		final String TITLE = "Animal Shelter Adooption Menu", FNAME_PROMPT = "Enter the customer's first name:",
						LNAME_PROMPT = "Enter the customer's last name:";
		final int INCREASE_COUNT = 1;
		boolean valid = true;
		int promptLevel = 0;
		
		do{
			valid = true;
			try{
				newCustomer.setPhoneNumber(phone);
				if(promptLevel == 0){
					newCustomer.setFirstName(JOptionPane.showInputDialog(null, FNAME_PROMPT, TITLE, JOptionPane.QUESTION_MESSAGE));
					promptLevel = 1;
				}
				if(promptLevel == 1){
					newCustomer.setLastName(JOptionPane.showInputDialog(null, LNAME_PROMPT, TITLE, JOptionPane.QUESTION_MESSAGE));
					Customer.setCustomerCount(INCREASE_COUNT);
				}
				
			}catch(IllegalArgumentException e){
				JOptionPane.showMessageDialog(null, e.getMessage(), TITLE, JOptionPane.ERROR_MESSAGE);
				valid = false;
			}
		}while(!valid);
	}
	
	private static void adoptAnimal(Customer oldCustomer, List<Animal> animals, Inventory[] items, ExpenseReport current){
		final String TITLE = "Animal Shelter Adooption Menu";
		final int PURCHASE_REQUIREMENT = 2;
		int choice = animalMenu();
		double foodCost = 0;
		double medicineCost = 0;
		double total = 0;
		String animalType = Animal.getAnimalTypes(choice), adoptionMessage = "The following ", adoptionMessageCont = "";
		
		switch(choice){
			case 1:
				foodCost = ExpenseReport.calculateMonthlyExpense(items[0].getItemSellCost(), PURCHASE_REQUIREMENT);
				medicineCost = ExpenseReport.calculateMonthlyExpense(items[1].getItemSellCost(), PURCHASE_REQUIREMENT);
				break;
			case 2:
				foodCost = ExpenseReport.calculateMonthlyExpense(items[2].getItemSellCost(), PURCHASE_REQUIREMENT);
				medicineCost = ExpenseReport.calculateMonthlyExpense(items[3].getItemSellCost(), PURCHASE_REQUIREMENT);
				break;
			case 3:
				foodCost = ExpenseReport.calculateMonthlyExpense(items[4].getItemSellCost(), PURCHASE_REQUIREMENT);
				medicineCost = ExpenseReport.calculateMonthlyExpense(items[5].getItemSellCost(), PURCHASE_REQUIREMENT);
				break;
			case 4:
				foodCost = ExpenseReport.calculateMonthlyExpense(items[6].getItemSellCost(), PURCHASE_REQUIREMENT);
				medicineCost = ExpenseReport.calculateMonthlyExpense(items[7].getItemSellCost(), PURCHASE_REQUIREMENT);
				break;
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
	
	private static void purchaseItem(Inventory[] items, ExpenseReport current){
		final String TITLE = "Animal Shelter Purchase Menu";
		int purchaseCount = 0, buyMore = 0, item = 0;
		String receipt = "", quantityPrompt = "", morePurchasesPrompt = "Would you like to make another purchase?", header = "";
		double total = 0;
		String[] itemNames = new String[Inventory.getMaxItemsCount()];
		int[] itemQuan = new int[Inventory.getMaxItemsCount()];
		double[] itemPrices = new double[Inventory.getMaxItemsCount()];
		
		do{
			item = inventoryMenu(items);
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
	
	private static void animalPopulation(List<Animal> animals){
		final String TITLE = "Animal Shelter Population Menu";
		String header = "Animal Population:\n";
		String population = "";
		
		for(int x = 0; x < Animal.getAnimalTypesCount(); x++){
			population += "  " + Animal.getAnimalTypes(x) + " Population: " + animalTypeCount(animals, x) + "\n"; 
		}
		
		String currentPop = "  Current Population: " + Animal.getAnimalCount() + "\n";
		String maxPop = "  Max Population: " + Animal.getMaxAnimals();
		
		JOptionPane.showMessageDialog(null, header + population + currentPop + maxPop, TITLE, JOptionPane.INFORMATION_MESSAGE);
	}
	
	private static void checkInventory(Inventory[] items){
		final String TITLE = "Animal Shelter Inventory Menu";
		String header = "Inventory Screen 1/2:\n";
		String inventoryPrint = "";
		for(int x = 0; x < Inventory.getMaxItemsCount(); x++){
			inventoryPrint += "  " + items[x].getItemName() + ": " + items[x].getItemCount() + "\n";
			if(x == 7){
				JOptionPane.showMessageDialog(null, header + inventoryPrint, TITLE, JOptionPane.INFORMATION_MESSAGE);
				inventoryPrint = "";
			}
		}
		
		header = "Inventory Screen 2/2:\n";
		String total = "Total: " + Inventory.getInventoryCount() + "\n";
		String max = "Max: " + Inventory.getMaxInventory() + "\n";
		JOptionPane.showMessageDialog(null, header + inventoryPrint + total + max, TITLE, JOptionPane.INFORMATION_MESSAGE);
	}
	
	private static void orderSupplies(Inventory[] items, ExpenseReport current){
		final String TITLE = "Animal Shelter Purchase Menu";
		int purchaseCount = 0, buyMore = 0, item = 0;
		String receipt = "", quantityPrompt = "", morePurchasesPrompt = "Would you like to make another purchase?", header = "";
		double total = 0;
		String[] itemNames = new String[Inventory.getMaxItemsCount()];
		int[] itemQuan = new int[Inventory.getMaxItemsCount()];
		double[] itemPrices = new double[Inventory.getMaxItemsCount()];
		
		do{
			item = inventoryMenu(items);
			quantityPrompt = "Enter the number of " + items[item].getItemName() + " to purchase:";
			itemNames[purchaseCount] = items[item].getItemName();
		
			try{
				itemQuan[purchaseCount] = Integer.parseInt(JOptionPane.showInputDialog(null, quantityPrompt, TITLE, JOptionPane.QUESTION_MESSAGE));
				itemPrices[purchaseCount] = items[item].inventoryPurchase((itemQuan[purchaseCount]));
				
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
	
	private static void employeeManagement(ShelterPerson[] employees){
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
							addEmployee((Employee)employees[Employee.getEmployeeCount()]);
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
	
	private static void displayEmployees(ShelterPerson[] employees){
		final String TITLE = "Animal Shelter Manager Menu";
		String employeeList = "List of Employees:\n";
		
		if(Employee.getEmployeeCount() == 0){
			employeeList = "There are no employees.";
		}
		else{
			for(int i = 0; i < Employee.getEmployeeCount(); i++){
				if(employees[i] instanceof Employee){
					Employee tempEmployee = (Employee)employees[i];
					employeeList += "  " + "- " + tempEmployee.toString() + "\n"; 
				}
			}
		}
		
		JOptionPane.showMessageDialog(null, employeeList, TITLE, JOptionPane.INFORMATION_MESSAGE);
	}
	
	private static void addEmployee(Employee newEmployee){
		final String TITLE = "Animal Shelter Manager Menu";
		final String FNAME_PROMPT = "Enter the new employee's first name:";
		final String LNAME_PROMPT = "Enter the new employee's last name:";
		final String ID_PROMPT = "Enter the ID of the new employee:";
		final int INCREASE_COUNT = 1;
		boolean valid = true;
		int promptLevel = 0;
		
		do{
			valid = true;
			try{
				if(promptLevel == 0){
					newEmployee.setFirstName(JOptionPane.showInputDialog(null, FNAME_PROMPT, TITLE, JOptionPane.QUESTION_MESSAGE));
					promptLevel = 1;
				}
				if(promptLevel == 1){
					newEmployee.setLastName(JOptionPane.showInputDialog(null, LNAME_PROMPT, TITLE, JOptionPane.QUESTION_MESSAGE));
					promptLevel = 2;
				}
				if(promptLevel == 2){
					newEmployee.setEmployeeId(JOptionPane.showInputDialog(null, ID_PROMPT, TITLE, JOptionPane.QUESTION_MESSAGE));
					Employee.setEmployeeCount(Employee.getEmployeeCount() + INCREASE_COUNT);
				}
			}catch(IllegalArgumentException e){
				JOptionPane.showMessageDialog(null, e.getMessage(), TITLE, JOptionPane.ERROR_MESSAGE);
				valid = false;
			}
		}while(!valid);
	}
	
	private static void removeEmployee(ShelterPerson[] employees){
		final String TITLE = "Animal Shelter Manager Menu";
		final String ERROR_NFE = "Please enter a number to select an option.";
		final String ERROR_MENU = "Please select an option from the menu.";
		String employeeList = "Select an employee to remove:\n";
		int removeThis = 0;
		
		if(Employee.getEmployeeCount() == 0){
			employeeList = "There are no employees.";
		}
		else{
			for(int i = 0; i < Employee.getEmployeeCount(); i++){
				if(employees[i] instanceof Employee){
					Employee tempEmployee = (Employee)employees[i];
					employeeList += "  " + (i+1) + ") " + tempEmployee.getFirstName() + " " + tempEmployee.getLastName() +"\n"; 
				}
			}
			employeeList += "  " + (Employee.getEmployeeCount() + 1) + ") Exit";
			
			try{
				removeThis = Integer.parseInt(JOptionPane.showInputDialog(null, employeeList, TITLE, JOptionPane.INFORMATION_MESSAGE));
				
				if(removeThis < 0 || removeThis > Employee.getEmployeeCount()){
					JOptionPane.showMessageDialog(null, ERROR_MENU, TITLE, JOptionPane.ERROR_MESSAGE);
				}
				else{
					employees[removeThis-1] = new Employee();
					Employee.setEmployeeCount(Employee.getEmployeeCount() - 1);
					
					for(int x = removeThis - 1; x < Employee.getEmployeeCount(); x++){
						employees[x] = employees[x+1];
					}
				}
				
				JOptionPane.showMessageDialog(null, "Employee List Updated!", TITLE, JOptionPane.INFORMATION_MESSAGE);
			}catch(NumberFormatException e){
				JOptionPane.showMessageDialog(null, ERROR_NFE, TITLE, JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	private static void expenseManagement(Inventory[] items, List<Animal> animals, ShelterPerson[] employees, ExpenseReport current){
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
	
	private static void yearSimulation(ExpenseReport current, Inventory[] items){
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
				averageAnimalExpense += items[i].getItemPurchaseCost();
				averageAnimalProfit += items[i].getItemSellCost();
			}
			inventoryAverageExpenses += items[i].getItemPurchaseCost();
			inventoryAverageProfit += items[i].getItemSellCost();
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
	
	private static void animalExpense(boolean multiple, Inventory[] items, List<Animal> animals){
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
		
		switch(animalChoice){
			case 0:
				foodCost = ExpenseReport.calculateMonthlyExpense(items[0].getItemPurchaseCost(), ExpenseReport.getFoodMultiplier());
				medicineCost =  ExpenseReport.calculateMonthlyExpense(items[1].getItemPurchaseCost(), ExpenseReport.getMedicineMultiplier());
				animalCount = animalTypeCount(animals, animalChoice);
				break;
			case 1:
				foodCost = ExpenseReport.calculateMonthlyExpense(items[2].getItemPurchaseCost(), ExpenseReport.getFoodMultiplier());
				medicineCost =  ExpenseReport.calculateMonthlyExpense(items[3].getItemPurchaseCost(), ExpenseReport.getMedicineMultiplier());
				animalCount = animalTypeCount(animals, animalChoice);
				break;
			case 2:
				foodCost = ExpenseReport.calculateMonthlyExpense(items[4].getItemPurchaseCost(), ExpenseReport.getFoodMultiplier());
				medicineCost =  ExpenseReport.calculateMonthlyExpense(items[5].getItemPurchaseCost(), ExpenseReport.getMedicineMultiplier());
				animalCount = animalTypeCount(animals, animalChoice);
				break;
			case 3:
				foodCost = ExpenseReport.calculateMonthlyExpense(items[6].getItemPurchaseCost(), ExpenseReport.getFoodMultiplier());
				medicineCost =  ExpenseReport.calculateMonthlyExpense(items[7].getItemPurchaseCost(), ExpenseReport.getMedicineMultiplier());
				animalCount = animalTypeCount(animals, animalChoice);
				break;
		}
		
		if(multiple){
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
	
	private static int inventoryMenu(Inventory[] items){
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
					
					for(int x = 0; x < Inventory.getMaxItemsCount(); x++){
						if(items[x].getItemName().equals(item)){
							choice = x;
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
	
	private static void readAnimalFile(List<Animal> animals){
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
				writer.println(persons);
				
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