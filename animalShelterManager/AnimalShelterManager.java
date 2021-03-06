/**
 * Group: MachineWork
 * Group Members: Larry Hong (G00714737) and Peter Alvarado Nunez (G00884723).
 * Group Leader: Larry Hong
 * Date: 12-08-2015
 * Course: IT 306 - 001
 * Project Phase VI - Final System Implementation
 */
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
		List<ShelterPerson> shelterManager = new LinkedList<ShelterPerson>();
		List<ShelterPerson> shelterEmployees = new LinkedList<ShelterPerson>();
		List<ShelterPerson> shelterCustomers = new LinkedList<ShelterPerson>();
		List<Animal> shelterAnimals = new LinkedList<Animal>();
		List<Inventory> shelterInventory = new LinkedList<Inventory>();
		
		//ExpenseReport object to store all the transactions done in the current session.
		ExpenseReport currentExpenses = new ExpenseReport("Current Report", 0, 0);
		
		//Method call that reads the files. The List, file path, and an integer to represent type are passed.
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
			//After the manager or employee return to the user selection screen the information is saved.
			//Also, when the program is terminated.
			writeInventoryToFile(shelterInventory);
			writeAnimalToFile(shelterAnimals);
			writeShelterPersonToFile(shelterCustomers);
			writeShelterPersonToFile(shelterEmployees);
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
		
		//Loop to prompt the user until they select the option to exit or terminate the system.
		do{
			try{
				managerChoice = Integer.parseInt(JOptionPane.showInputDialog(null, PROMPT + OPTIONS[0] + OPTIONS[1] + OPTIONS[2]
						+ OPTIONS[3] + OPTIONS[4] + OPTIONS[5] , TITLE, JOptionPane.QUESTION_MESSAGE));
				
				//Based on the user's input one of the following will occur.
				switch(managerChoice){
					case 1:
						checkInventory(items);
						break;
					case 2:
						makeOrder(items, current, true);
						break;
					case 3:
						employeeManagement(employees);
						break;
					case 4:
						expenseManagement(items, animals, employees, current);
						break;
					case 5:
						//Message to let the user know they are returning to the login screen.
						JOptionPane.showMessageDialog(null, "Returning to Login Screen", TITLE, JOptionPane.INFORMATION_MESSAGE);
						break;
					case 6:
						//This option represents the user wanting to shutdown the system. The variables that control the loop
						//are changed to a value to terminate all loops.
						terminationChoice = -1;
						managerChoice = 5;
						break;
					//Error if the users an option that is not part of the menu.
					default:
						JOptionPane.showMessageDialog(null, ERROR_MENU, TITLE, JOptionPane.ERROR_MESSAGE);
				}
			}catch(NumberFormatException e){
				//Error if the user enters a non-numeric value in the prompt.
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
		
		//Foe loop to add the name and count of all the items in the shelter to a string to be display.
		for(Inventory i : items){
			inventoryPrint += "  " + i.getItemName() + ": " + i.getItemCount() + "\n";
		}
		
		//Variables to store the number of items in the shelter and the max number of items in the shelter.
		String total = "Total: " + Inventory.getInventoryCount() + "\n";
		String max = "Max: " + Inventory.getMaxInventory() + "\n";
		//All the information is displayed to the user.
		JOptionPane.showMessageDialog(null, header + inventoryPrint + total + max, TITLE, JOptionPane.INFORMATION_MESSAGE);
	}
	
	/**
	 * Method to allow the employees to buy or sell inventory. If the call comes from the manager menu
	 * the prices are lower and is considered an expense. If the call comes from the employee menu
	 * the prices are higher and is considered profit.
	 * @param items
	 * @param current
	 * @param manager
	 */
	private static void makeOrder(List<Inventory> items, ExpenseReport current, boolean manager){
		//Variable to store the title that will be displayed at the top of the prompt.
		final String TITLE = "Animal Shelter Purchase Menu";
		//Variables to keep track of the number of purchases, if the user wants to buy more, and the item to buy.
		int purchaseCount = 0, buyMore = 0, item = 0;
		//Variables to display the messages to the user.
		String receipt = "", quantityPrompt = "", morePurchasesPrompt = "Would you like to make another purchase?", header = "";
		//Variable to store the total the user will pay at the end.
		double total = 0;
		//Variable to check if the user entered the exit option in the menu.
		boolean countPrompt = false;
		//Parallel arrays to store the name, quantity, and prices of the items the user wants to buy.
		String[] itemNames = new String[Inventory.getMaxItemsCount()];
		int[] itemQuan = new int[Inventory.getMaxItemsCount()];
		double[] itemPrices = new double[Inventory.getMaxItemsCount()];
		
		//Loop to prompt keep prompting the user to pick an item and the quantity.
		do{
			//Calls the inventoryMenu method to show all the options to the user.
			item = inventoryMenu(items);
			if(item == -100){
				break;
			}
			quantityPrompt = "Enter the number of " + items.get(item).getItemName() + " to purchase:";
			itemNames[purchaseCount] = items.get(item).getItemName();
			
			//Error to be displayed if no more of the item need to be purchased
			if(items.get(item).getItemCount() == Inventory.getMaxItemTypesCount() && manager){
				JOptionPane.showMessageDialog(null, "The stock of " + items.get(item).getItemName() +" is full.",
							TITLE, JOptionPane.INFORMATION_MESSAGE);
			}
			//Error to be displayed if there no more of that particular item available for purchase.
			else if(items.get(item).getItemCount() == 0 && !manager){
				JOptionPane.showMessageDialog(null, "The stock of " + items.get(item).getItemName() +" is empty.",
						TITLE, JOptionPane.INFORMATION_MESSAGE);
			}
			else{
				do{
					//Try and catch to prevent the user from entering non-numeric values and to catch other exceptions.
					try{
						itemQuan[purchaseCount] = Integer.parseInt(JOptionPane.showInputDialog(null, quantityPrompt, TITLE, JOptionPane.QUESTION_MESSAGE));
						if(manager){
							itemPrices[purchaseCount] = items.get(item).inventoryPurchase((itemQuan[purchaseCount]));
							countPrompt = true;
						}
						else if(!manager){
							itemPrices[purchaseCount] = items.get(item).inventorySell(itemQuan[purchaseCount]);
							countPrompt = true;
						}
					}catch(NumberFormatException e){
						JOptionPane.showMessageDialog(null, "The quantity must a whole number.", TITLE, JOptionPane.ERROR_MESSAGE);
						countPrompt = false;	
					}catch(IllegalArgumentException e){
						JOptionPane.showMessageDialog(null, e.getMessage(), TITLE, JOptionPane.ERROR_MESSAGE);
						countPrompt = false;
					}
				}while(!countPrompt);
					
				//Purchase count is incremented in case the user wants to purchase one more item. 
				purchaseCount++;
				//Message to be displayed if the inventory is below 10.
				if(items.get(item).getItemCount() <= Inventory.getLowAlert()){
					JOptionPane.showMessageDialog(null, "The number of " + items.get(item).getItemName() + " is less than "
								+ Inventory.getLowAlert() + "\n In Stock: " + items.get(item).getItemCount()
								, TITLE, JOptionPane.INFORMATION_MESSAGE);
				}
			}
			header = "Receipt Preview:\n";
			
			//Loop to create a receipt for the user by putting all the relevant information inside a string.
			for(int x = 0; x < purchaseCount; x++){
				receipt += "  Item " + (x + 1) + ": " + itemNames[x] + " || Qty: " + itemQuan[x] 
						+ " || Cost: " + String.format("$%,.2f", itemPrices[x]) + "\n";
				total += itemPrices[x];
			}
			//Message to display the receipt along the total.
			JOptionPane.showMessageDialog(null, header + receipt + "  Total: " + String.format("$%,.2f", total));
			//Prompt to know if the user would like to purchase more items.
			buyMore = JOptionPane.showConfirmDialog(null, morePurchasesPrompt, TITLE, JOptionPane.YES_NO_OPTION);
			//If the user wants to purchase more items the receipt and total are reset and will be calculated
			//again during the execution of the for loop.
			if(buyMore == JOptionPane.YES_OPTION){
				receipt = "";
				total = 0;
			}
		}while(buyMore != JOptionPane.NO_OPTION);
		
		//When the user selects to not purchase anymore then the final receipt is displayed.
		header = "Final Receipt:\n";
		JOptionPane.showMessageDialog(null, header + receipt + "  Total: " + String.format("$%,.2f", total));
		//Based on the menu that called this method the transaction is either an expense or a profit.
		if(manager){
			current.setMonthExpense(total);
		}
		else if(!manager){
			current.setMonthProfit(total);
		}
	}
	
	/**
	 * Method to display a menu with the options available to the manager about employees.
	 * @param employees
	 */
	private static void employeeManagement(List<ShelterPerson> employees){
		//Variables that hold the messages that are displayed to the manager.
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
						//If the max number of employees has been reached then this message is displayed to the user.
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
						//This option return the user to the previous menu. Prevents the default from triggering.
						break;
					default:
						JOptionPane.showMessageDialog(null, ERROR_MENU, TITLE, JOptionPane.ERROR_MESSAGE);
				}
			}catch(NumberFormatException e){
				JOptionPane.showMessageDialog(null, ERROR_NFE, TITLE, JOptionPane.ERROR_MESSAGE);
			}
		}while(choice != 4);
	}
	
	/**
	 * Method to display a menu with the options available to the manager about expenses. 
	 * @param items
	 * @param animals
	 * @param employees
	 * @param current
	 */
	private static void expenseManagement(List<Inventory> items, List<Animal> animals, List<ShelterPerson> employees, ExpenseReport current){
		//Variables that hold the messages that are displayed to the manager.
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
						//This option return the user to the previous menu. Prevents the default from triggering.
						break;
					default:
						JOptionPane.showMessageDialog(null, ERROR_MENU, TITLE, JOptionPane.ERROR_MESSAGE);
				}
			}catch(NumberFormatException e){
				JOptionPane.showMessageDialog(null, ERROR_NFE, TITLE, JOptionPane.ERROR_MESSAGE);
			}
		}while(choice != 5);
	}
	
	/**
	 * Method to display all the options available to the employee. Also, used to control when and how other methods
	 * are called.
	 * @param customers
	 * @param items
	 * @param animals
	 * @param current
	 */
	private static void employeeMenu(List<ShelterPerson> customers, List<Inventory> items, List<Animal> animals, ExpenseReport current){
		//Variables that hold the messages that are displayed to the manager.
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
						//If to check that the animal shelter is not empty before trying to adopt an animal.
						if(!animals.isEmpty()){
							animalAdoption(customers, animals, items, current);
						}
						//If the there are no animals then this error is displayed.
						else{
							JOptionPane.showMessageDialog(null, "No animals available for adoption.", TITLE, JOptionPane.ERROR_MESSAGE);
						}
						break;
					case 2:
						//If to check that the animal shelter is not full.
						if(animals.size() != Animal.getMaxAnimals()){
							addAnimal(animals);
						}
						//If the shelter is full then this error is displayed.
						else{
							JOptionPane.showMessageDialog(null, "Shelter is full. No animals can be added.", TITLE, JOptionPane.ERROR_MESSAGE);
						}
						
						break;
					case 3:
						makeOrder(items, current, false);
						break;
					case 4:
						animalPopulation(animals);
						break;
					case 5:
						//This option return the user to the login menu. Prevents the default from triggering.
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
	
	/**
	 * Method to handle the adoption of animals. First the user is prompted for their phone number and based on that
	 * a new customer is created or an existing customer is used.
	 * @param customers
	 * @param animals
	 * @param items
	 * @param current
	 */
	private static void animalAdoption(List<ShelterPerson> customers, List<Animal> animals,  List<Inventory> items, ExpenseReport current){
		final String TITLE = "Animal Shelter Adooption Menu";
		int compareResult = 0;
		boolean valid = true;
		//Creates a new customer object and adds it to the list. This allows the set and comparison of the phone number.
		customers.add(new Customer());
		
		do{
			try{
				valid = true;	
				((Customer)customers.get(customers.size()-1)).setPhoneNumber(JOptionPane.showInputDialog(null,
									"Enter the customer's phone number:", TITLE, JOptionPane.QUESTION_MESSAGE));
				
				//For loop to go through all the customers except the newly created one to compare phone numbers.
				for(int x = 0; x < customers.size() - 1; x++){
					if(customers.get(x) instanceof Customer){
						compareResult = ((Customer)customers.get(x)).compareTo((Customer)customers.get(customers.size()-1));
						//If the value of compareResult is 0 then a match was found.
						if(compareResult == 0){
							//First remove the newly created one since this customer already exists.
							customers.remove(customers.get(customers.size()-1));
							//If the customer has reached the limit of adoptions then this error is displayed.
							if(((Customer)customers.get(x)).getAdoptionCount() == Customer.getMaxAdoptions()){
								JOptionPane.showMessageDialog(null, "Sorry " + customers.get(x).getFirstName() + "!\n" + " You have reached"
										+ " the max number of adoptions allowed.");
							}
							else{
								//If the customer has not reached the limit then they are allow to adopt an animal.
								//The Customer information is displayed along side a welcome message.
								JOptionPane.showMessageDialog(null, "Welcome Back " + customers.get(x).getFirstName() 
											+ " " + customers.get(x).getLastName() + "!\n" + " Adoption History: "
											+ ((Customer)customers.get(x)).getAnimalsAdopted(), TITLE, JOptionPane.INFORMATION_MESSAGE);
								adoptAnimal((Customer)customers.get(x), animals, items, current);
								break;
							}
						}
					}
				}
				
				//If no match is found the user is informed that a new customer must be created.
				if(compareResult == -1){
					JOptionPane.showMessageDialog(null, "Customer information not found.\n A new account must be created.");
					createNewCustomer(customers);
					adoptAnimal((Customer)customers.get(customers.size()-1), animals, items, current);
				}
				
				}catch(IllegalArgumentException e){
					JOptionPane.showMessageDialog(null, e.getMessage(), TITLE, JOptionPane.ERROR_MESSAGE);
					valid = false;
				}
		}while(!valid);
	}
	
	/**
	 * Method to allow the user to add an animal to the shelter.
	 * @param animals
	 */
	private static void addAnimal(List<Animal> animals){
		final String TITLE = "Animal Shelter Employee Menu";
		String message = ""; 
		int menuResponse = 0;
		
		menuResponse = animalMenu();
		
		if(menuResponse != 4){
			Animal newAnimal = new Animal();
			//The animal history is updated to create a new unique ID.
			Animal.setAnimalHistory(Animal.getAnimalHistory() + 1);
			//The type of the animal is set based on the user choice in the animal menu method.
			newAnimal.setAnimalType(Animal.getAnimalTypes(menuResponse));
			//The id is created for the new animal.
			newAnimal.setAnimalID(Animal.createAnimalID(newAnimal.getAnimalType()));
			
			//A confirmation message is displayed to let the user know a new animal was added. 
			message = " A new " + newAnimal.getAnimalType() + " has been added!\n ID: " + newAnimal.getAnimalID();
			animals.add(newAnimal);
			JOptionPane.showMessageDialog(null, message, TITLE, JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	/**
	 * Method to allow the user to see all the animal types and the population of each in the shelter.
	 * @param animals
	 */
	private static void animalPopulation(List<Animal> animals){
		final String TITLE = "Animal Shelter Population Menu";
		String header = "Animal Population:\n";
		String population = "";
		
		//Loop to go through all the animals in the shelter and count how many of each type there are.
		for(int x = 0; x < Animal.getAnimalTypesCount(); x++){
			population += "  " + Animal.getAnimalTypes(x) + " Population: " + animalTypeCount(animals, x) + "\n"; 
		}
		
		String currentPop = "  Current Population: " + animals.size() + "\n";
		String maxPop = "  Max Population: " + Animal.getMaxAnimals();
		
		JOptionPane.showMessageDialog(null, header + population + currentPop + maxPop, TITLE, JOptionPane.INFORMATION_MESSAGE);
	}
	
	/**
	 * Method to create a new customer by prompting them for their first name and last name.
	 * @param customers
	 */
	private static void createNewCustomer(List<ShelterPerson> customers){
		final String TITLE = "Animal Shelter Adooption Menu", FNAME_PROMPT = "Enter the customer's first name:",
						LNAME_PROMPT = "Enter the customer's last name:";
		boolean valid = true;
		//Variable to control the prompt that needs to be displayed to the user. To avoid repeating the same prompts.
		int promptLevel = 0;
		
		do{
			valid = true;
			try{
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
	
	/**
	 * Method to handle the adoption of animals. It creates a receipt and display the ID of an animal available based on
	 * the type chosen. 
	 * @param customer
	 * @param animals
	 * @param items
	 * @param current
	 */
	private static void adoptAnimal(Customer customer, List<Animal> animals,  List<Inventory> items, ExpenseReport current){
		final String TITLE = "Animal Shelter Adooption Menu";
		final int PURCHASE_REQUIREMENT = 2;
		int choice = animalMenu();
		double foodCost = 0;
		double medicineCost = 0;
		double total = 0;
		if(choice != 4){
			String animalType = Animal.getAnimalTypes(choice), adoptionMessage = "The following ", adoptionMessageCont = "";
			
			//Loop to get the food price and medicine price based on the animal type. The user is required to purchase a certain
			//amount of each during the adoption.
			for(Inventory i : items){
				if(i.getItemName().contains(animalType + " F")){
					foodCost = (i.getItemSellCost() * PURCHASE_REQUIREMENT);
				}
				if(i.getItemName().contains(animalType + " M")){
					medicineCost = (i.getItemSellCost() * PURCHASE_REQUIREMENT);
					break;
				}
			}
			
			//The total cost of adopting is calculated and it will be displayed later.
			total = foodCost + medicineCost;
			adoptionMessage += animalType + " is available for adoption.\n ID: ";
			adoptionMessageCont = "\n Food Cost: " + String.format("$%,.2f", foodCost) + "\n"
					+ " Medicine Cost: " + String.format("$%,.2f", medicineCost) + "\n Total: " + String.format("$%,.2f", total);
			
			//For loop to go through all the animals until a match for the same animal type is found.
			for(Animal a : animals){
				if(a.getAnimalType().equals(animalType)){
					customer.adoptAnimal(a);
					//When the match is found the message is displayed.
					JOptionPane.showMessageDialog(null, adoptionMessage + a.getAnimalID() + adoptionMessageCont,
							TITLE, JOptionPane.INFORMATION_MESSAGE);
					//The animal with the matching type gets remove from the list.
					animals.remove(a);
					break;
				}
			}
			//Adopting an animal is considered profit and is added to the current calculations.
			current.setMonthProfit(total);
		}
	}
	
	/**
	 * Method to count the different types of animals in the shelter and return the count for that
	 * animal type.
	 * @param animals
	 * @param index
	 * @return
	 */
	private static int animalTypeCount(List<Animal> animals, int index){
		//Array to hold the count for all the types.
		int[] counts = {0, 0, 0, 0};
		
		//For loop to go through all the animals in the shelter to count their type.
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
		//Returns the count for the specified animal index.
		return counts[index];
	}
	
	/**
	 * Method to display all the employees in the shelter. The id, first name,
	 * and last name of the employee is displayed.
	 * @param employees
	 */
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
		//Message that is displayed to the user with the list of all the employees.
		JOptionPane.showMessageDialog(null, employeeList, TITLE, JOptionPane.INFORMATION_MESSAGE);
	}
	
	/**
	 * Method to allow the manager to add a new employee to the animal shelter.
	 * @param employees
	 */
	private static void addEmployee(List<ShelterPerson> employees){
		//Variables to display the various messages to the user.
		final String TITLE = "Animal Shelter Manager Menu";
		final String FNAME_PROMPT = "Enter the new employee's first name:";
		final String LNAME_PROMPT = "Enter the new employee's last name:";
		final String ID_PROMPT = "Enter the ID of the new employee:";
		PrintWriter writer = null;
		//Variables to store the current employee count and the number of employees to increment by.
		final int INCREASE_COUNT = 1, EMPLOYEE_COUNT = Employee.getEmployeeCount();
		//Variable to control the loop that prompts the manager for the employee information.
		boolean valid = true;
		//Variable to control the prompt level, so the user is not asked the same question twice.
		int promptLevel = 0;
		//Variable to store the comparison of the ID entered by the manager and the existing IDs.
		int compareResult = 0;
		//Adds a new employee to the list.
		employees.add(new Employee());
		
		//Do while loop to prompt the user for the employee information.
		do{
			valid = true;
			//Try and catch to avoid exceptions crashing the program.
			try{
				//First prompt to ask the manager for the first name of the new employee.
				if(promptLevel == 0){
					employees.get(EMPLOYEE_COUNT).setFirstName(JOptionPane.showInputDialog(null, FNAME_PROMPT, TITLE, JOptionPane.QUESTION_MESSAGE));
					promptLevel = 1;
				}
				//First prompt to ask the manager for the last name of the new employee.
				if(promptLevel == 1){
					employees.get(EMPLOYEE_COUNT).setLastName(JOptionPane.showInputDialog(null, LNAME_PROMPT, TITLE, JOptionPane.QUESTION_MESSAGE));
					promptLevel = 2;
				}
				//First prompt to ask the manager for the id of the new employee.
				if(promptLevel == 2){
					((Employee)employees.get(EMPLOYEE_COUNT)).setEmployeeId(JOptionPane.showInputDialog(null, ID_PROMPT, TITLE, JOptionPane.QUESTION_MESSAGE));
					//Loop to go through all the employees and compare their IDs with the ID entered by the manager to avoid duplicates.
					for(int x = 0; x < employees.size() - 1; x++){
						if(employees.get(x) instanceof Employee){
							compareResult = ((Employee)employees.get(x)).compareTo((Employee)employees.get(employees.size()-1));
							//If the ID is already in use then this error is displayed.
							if(compareResult == 0){
								JOptionPane.showMessageDialog(null, "An employee has already been assigned that ID.\n Please enter a new ID.",
										TITLE, JOptionPane.ERROR_MESSAGE);
								valid = false;
								break;
							}
						}
					}
					//If the ID is unique then the new employee can be added to the list.
					if(compareResult == -1){
						Employee.setEmployeeCount(EMPLOYEE_COUNT + INCREASE_COUNT);
					}
				}
			}catch(IllegalArgumentException e){
				JOptionPane.showMessageDialog(null, e.getMessage(), TITLE, JOptionPane.ERROR_MESSAGE);
				valid = false;
			}
		}while(!valid);
		//After the customer information is validated and added it is appended to the file.
		try{
			//Writer to write to the file.
			writer = new PrintWriter(new FileOutputStream(Employee.fileLocation(), true));
			//Writes only the last entry, because it is the only one that needs to be changed.
			writer.println(employees.get(employees.size()-1).toFile());
			//Closes writer.
			writer.close();
		}
		//Catch if the file was not found.
		catch(FileNotFoundException e){
			JOptionPane.showMessageDialog(null, "The file was not found.", TITLE, JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/**
	 * Method to remove an employee from the list.
	 * @param employees
	 */
	private static void removeEmployee(List<ShelterPerson> employees){
		//Variables to display the various messages to the user.
		final String TITLE = "Animal Shelter Manager Menu";
		final String ERROR_NFE = "Please enter a number to select an option.";
		final String ERROR_MENU = "Please select an option from the menu.";
		String employeeList = "Select an employee to remove:\n";
		//Variables to store the choice by the user and the counter to display the options.
		int removeThis = 0, counter = 0;
		//Variable to control the loop that prompts the manager for the employee information.
		boolean valid = true;
		
		//If the employee count is 0 then there are no employees and the messages is shown. 
		if(Employee.getEmployeeCount() == 0){
			employeeList = "There are no employees.";
		}
		else{
			//For loop to display all the employees that are currently working at the shelter.
			for(ShelterPerson e : employees){
				if(e instanceof Employee){
					employeeList += "  " + (counter+1) + ") " + e.getFirstName() + " " + e.getLastName() +"\n";
				}
				counter++;
			}
			//Counter is increased one more time to be used as the option to exit the menu.
			counter++;
			employeeList += "  " + (counter) + ") Exit";
			//Loop to display the employees in the list and allow the manager to remove one of the employees.
			do{
				try{
					removeThis = Integer.parseInt(JOptionPane.showInputDialog(null, employeeList, TITLE, JOptionPane.QUESTION_MESSAGE));
					//If the user enters an option from the menu the employee at that number gets removed.
					if(removeThis > 0 && removeThis <= Employee.getEmployeeCount()){
						employees.remove(removeThis - 1);
						JOptionPane.showMessageDialog(null, "Employee List Updated!", TITLE, JOptionPane.INFORMATION_MESSAGE);
						Employee.setEmployeeCount(Employee.getEmployeeCount() - 1);
						//After the employee is removed the loop can terminate.
						valid = true;
					}
					else if(removeThis == counter){
						//If the user decides to return to the previous menu the loop can terminate.
						valid = true;
					}
					else{
						//If the user enters an option outside the available options this error is display.
						JOptionPane.showMessageDialog(null, ERROR_MENU, TITLE, JOptionPane.ERROR_MESSAGE);
						valid = false;
					}
				}catch(NumberFormatException e){
					//If the user entered a non-numeric option then this error is displayed.
					JOptionPane.showMessageDialog(null, ERROR_NFE, TITLE, JOptionPane.ERROR_MESSAGE);
				}
			}while(!valid);
		}
	}
	
	/**
	 * Method for the user to create a 12 month simulation of profits and expenses based on user input.
	 * The input includes the number of animals expected at the shelter and the number animals that is expected
	 * to be adopted.
	 * @param current
	 * @param items
	 */
	private static void yearSimulation(ExpenseReport current, List<Inventory> items){
		//Variables to display the various messages to the user.
		final String TITLE = "Animal Shelter 12-Month Simulation";
		final String ITEM_PROMPT = "Enter the expected percentage of merchandise to be sold in Month ";
		final String ANIMAL_PROMPT = "Enter the expected number of animals in the shelter for the year:";
		final String ANIMAL_PROMPT2 = "Enter the expected number of animals to be adopted for the year:";
		final String ERROR_NFE = "Please enter a positive number as the percentage.";
		//Variables to store the max number of animals that can be at the shelter in the year.
		//Based on the max number of animals (100) * the number of prompts (11).
		final int MAX_YEARLY_ANIMALS = 1100;
		//Variable to store the number of months for the simulation.
		final int MONTHS_NUM = 12;
		
		String simulation = "12 Month Simulation:\n";
		//Variables that will store values that will be used during the calculations.
		double inventoryAverageExpenses = 0;
		double inventoryAverageProfit = 0;
		double employeeExpense = 0;
		double percentToSell = 0;
		double monthTotalExpense = 0;
		double monthTotalProfit = 0;
		double averageAnimalExpense = 0;
		double averageAnimalProfit = 0;
		int expectedAnimals = 0, expectedAdoptions = 0, promptLevel = 0;
		//Variable to control the loop that prompts the user.
		boolean valid = true;
		//Array to store all the simulated months as an ExpenseReport.
		ExpenseReport[] yearSimulation = new ExpenseReport[MONTHS_NUM];
		
		//Loop to calculate the average cost of all the items based on their cost and the number of items.
		for(int i = 0; i < Inventory.getMaxItemsCount(); i++){
			//The first 7 items are related to animal expense, so they are calculated separately.
			if(i >= 0 && i <= 7){
				averageAnimalExpense += items.get(i).getItemPurchaseCost();
				averageAnimalProfit += items.get(i).getItemSellCost();
			}
			//Variables to store the average all of the items in the inventory.
			inventoryAverageExpenses += items.get(i).getItemPurchaseCost();
			inventoryAverageProfit += items.get(i).getItemSellCost();
		}
		
		//Variables to store the rest of the expenses.
		inventoryAverageExpenses = (inventoryAverageExpenses / Inventory.getMaxItemsCount());
		inventoryAverageProfit = (inventoryAverageProfit / Inventory.getMaxItemsCount());
		//The employee pay is biweekly, so to calculate a monthly pay it has to be multiply by two.
		employeeExpense = ExpenseReport.calculateEmployeeExpense(Employee.getEmployeeCount(), Employee.getEmployeePay()) * 2;
		averageAnimalExpense = (averageAnimalExpense / 8); 
		averageAnimalProfit = (averageAnimalProfit / 8); 
		
		//Loop to prompt the user for the information needed to calculate the 12 month simulation.
		for(int x = 0; x < MONTHS_NUM; x++){
			monthTotalExpense = 0;
			monthTotalProfit = 0;
			//The first month is assigned the current expenses and profits as the program is running.
			if(x == 0){
				yearSimulation[x] = new ExpenseReport("Month " + (x+1), current.getMonthProfit(), current.getMonthExpense());
			}
			else{
				do{
					valid = true;
					try{
						//First prompt to ask for the expected number of animals at the shelter.
						if(promptLevel == 0){
							expectedAnimals = Integer.parseInt(JOptionPane.showInputDialog(null, ANIMAL_PROMPT, TITLE, JOptionPane.QUESTION_MESSAGE));
							if(expectedAnimals > 0 && expectedAnimals <= MAX_YEARLY_ANIMALS){
								averageAnimalExpense = (averageAnimalExpense * expectedAnimals);
								promptLevel = 1;
							//Error if the number of animals is greater than the max allowed.
							}else{
								JOptionPane.showMessageDialog(null, "The number of expected animals must be positive and less than " +  MAX_YEARLY_ANIMALS,
											TITLE, JOptionPane.ERROR_MESSAGE);
								valid = false;
							}
						}
						//Prompt the user for the expected number of animals to be adopted during the year.
						if(promptLevel == 1){
							expectedAdoptions = Integer.parseInt(JOptionPane.showInputDialog(null, ANIMAL_PROMPT2, TITLE, JOptionPane.QUESTION_MESSAGE));
							if(expectedAdoptions > 0 && expectedAdoptions <= expectedAnimals){
								averageAnimalProfit = (averageAnimalProfit * expectedAdoptions);
								promptLevel = 2;
							}else{
								JOptionPane.showMessageDialog(null, "The number of expected adoptions must be positive and less than " +  expectedAnimals,
											TITLE, JOptionPane.ERROR_MESSAGE);
								valid = false;
							}
						}
						
						//Prompt the user for the expected percentage of inventory to be sold during that simulated month.
						if(promptLevel == 2){
							percentToSell = Double.parseDouble(JOptionPane.showInputDialog(null, ITEM_PROMPT + (x+1) + ":"
									, TITLE, JOptionPane.QUESTION_MESSAGE));
						}
						
						//Stores the profit calculations based on the the values calculated and the values entered by the user.
						monthTotalProfit =  (ExpenseReport.simulateMonthProft(percentToSell, inventoryAverageProfit) + (employeeExpense + averageAnimalExpense));
						//Stores the profit calculations based on the the values calculated and the values entered by the user.
						monthTotalExpense = ExpenseReport.simulateMonthExpense(percentToSell, inventoryAverageExpenses) + averageAnimalProfit;
						//Stores the information in the next position in the array as a new object.
						yearSimulation[x] = new ExpenseReport("Month " + (x+1), monthTotalProfit, monthTotalExpense);
					}catch(NumberFormatException e){
						//Error to display if the user enters a non-numeric value this error is displayed.
						JOptionPane.showMessageDialog(null, ERROR_NFE, TITLE, JOptionPane.ERROR_MESSAGE);
						valid = false;
					}
					catch(IllegalArgumentException e){
						JOptionPane.showMessageDialog(null, e.getMessage(), TITLE, JOptionPane.ERROR_MESSAGE);
						valid = false;
					}
				}while(!valid);
			}
		}
		
		//Loop to print all the simulated months information.
		for(int x = 0; x < MONTHS_NUM; x++){
			simulation += yearSimulation[x].toString() + "\n";
		}
		
		JOptionPane.showMessageDialog(null, simulation, TITLE, JOptionPane.INFORMATION_MESSAGE);
		//Prompt to ask the user if they wished to store the simulation.
		if(JOptionPane.showConfirmDialog(null, "Save simulation to a text file?", TITLE,
				JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
			writeSimulationToFile(yearSimulation);
		}
	}
	
	/**
	 * Method to calculate the animal expenses based on the type and the number of animals.
	 * @param multiple
	 * @param items
	 * @param animals
	 */
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
		if(animalChoice != 4){
			animalType = Animal.getAnimalTypes(animalChoice);
			//Loops to find the associated expense with the animal type.
			for(Inventory i : items){
				//First finds the cost of food by using the animal type and the letter F.
				if(i.getItemName().contains(animalType + " F")){
					foodCost = (i.getItemPurchaseCost() * ExpenseReport.getFoodMultiplier());
				}
				//Second finds the cost of medicine by using the animal type and the letter M.
				if(i.getItemName().contains(animalType + " M")){
					medicineCost = (i.getItemPurchaseCost() * ExpenseReport.getMedicineMultiplier());
					break;
				}
			}
			
			//If to calculate the cost of all the animals of that type.
			if(multiple){
				animalCount = animalTypeCount(animals, animalChoice);
				totalCost = ExpenseReport.calculateAnimalExpense(animalCount, foodCost, medicineCost);
				header += "the " + animalType + " population per month:\n Population: " + animalCount + "\n";
			}
			//Else the cost of a single animal is calculated.
			else{
				animalCount = 1;
				totalCost = ExpenseReport.calculateAnimalExpense(animalCount, foodCost, medicineCost);
				header += "a single " + animalType + " per month:\n";
			}
			
			//Message to display all the costs.
			JOptionPane.showMessageDialog(null, header + "  Food cost per month: " + String.format("$%,.2f", (foodCost * animalCount)) + "\n" +
						"  Medicine cost per month: " + String.format("$%,.2f", (medicineCost * animalCount)) + "\n" + "  Total: " + 
						String.format("$%,.2f", totalCost) + "\n", TITLE, JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	/**
	 * Method to display the available types of animals in the shelter. This method is used anywhere the user
	 * has to pick from one of the type of animals.
	 * @return
	 */
	private static int animalMenu(){
		//Variables to display the various messages to the user.
		final String TITLE = "Animal Shelter Employee Menu";
		final String PROMPT = "Select the type of animal:\n";
		final String[] OPTIONS = {"  1) Dog\n", "  2) Cat\n", "  3) Small Rodent\n", "  4) Bird\n", "  5) Exit"};
		final String ERROR_NFE = "Please enter a number to select an option.";
		final String ERROR_MENU = "Please select an option from the menu.";
		//Variable to store the user choice.
		int choice = 0;
		//Variable to control the loop.
		boolean validChoice = false;
		
		do{
			//Try and catch to prevent exceptions from crashing the program.
			try{
				//Animal menu is displayed.
				choice = Integer.parseInt(JOptionPane.showInputDialog(null, PROMPT + OPTIONS[0] + OPTIONS[1] + OPTIONS[2]
						+ OPTIONS[3] + OPTIONS[4], TITLE, JOptionPane.QUESTION_MESSAGE));
				if(choice <= 0 || choice > 5){
					//If the user enters an option outside the menu options this error is displayed.
					JOptionPane.showMessageDialog(null, ERROR_MENU, TITLE, JOptionPane.ERROR_MESSAGE);
					validChoice = false;
				}else{
					//If the user enters a valid choice the loop can terminate.
					validChoice = true;
				}
			}catch(NumberFormatException e){
				//If the user enters a non-numeric value then this error is displayed.
				JOptionPane.showMessageDialog(null, ERROR_NFE, TITLE, JOptionPane.ERROR_MESSAGE);
			}
			
		}while(!validChoice);
		
		//The location of the animal type in the an array in the DDC is returned.
		return choice - 1;
	}
	
	/**
	 * Method to display the available items in the shelter. This method is used anywhere the user has to
	 * pick from one of the type of animals.
	 * @param items
	 * @return
	 */
	private static int inventoryMenu(List<Inventory> items){
		//Variables to display the various messages to the user.
		final String TITLE = "Animal Shelter Employee Menu";
		final String PROMPT = "Select an item:\n";
		final String[] PRODUCTS = {"Food", "Medicine", "Food Bowls", "Leashes", "Collars", "Toys"};
		final String[] OPTIONS = {"  1) " + PRODUCTS[0] +"\n", "  2) " + PRODUCTS[1] +"\n", "  3) " + PRODUCTS[2] +"\n",
				"  4) " + PRODUCTS[3] + "\n", "  5) " + PRODUCTS[4] + "\n", "  6) " + PRODUCTS[5] + "\n"
				+ "  7) Exit"};
		final String ERROR_NFE = "Please enter a number to select an option.";
		final String ERROR_MENU = "Please select an option from the menu.";
		//Variable to store the user choice.
		int choice = 0;
		String item = "";
		//Variable to control the loop.
		boolean validChoice = false;
		
		do{
			//Try and catch to prevent exceptions from crashing the program.
			try{
				//Inventory menu is displayed.
				choice = Integer.parseInt(JOptionPane.showInputDialog(null, PROMPT + OPTIONS[0] + OPTIONS[1] + OPTIONS[2]
						+ OPTIONS[3] + OPTIONS[4] + OPTIONS[5], TITLE, JOptionPane.QUESTION_MESSAGE));
				if(choice <= 0 || choice > 7){
					//If the user enters an option outside the menu options this error is displayed.
					JOptionPane.showMessageDialog(null, ERROR_MENU, TITLE, JOptionPane.ERROR_MESSAGE);
					validChoice = false;
				}else{
					//If the user selects the Exit option a sentinel value is set and we break from the loop.
					if(choice == 7){
						choice = -100;
						validChoice = true;
						break;
					}
					//If the user enters 1 or 2 then the animal menu has to be called.
					if(choice == 1 || choice == 2){
						int animalChoice = animalMenu();
						if(animalChoice != 4){
							item = Animal.getAnimalTypes(animalChoice) + " " + PRODUCTS[choice - 1];
						}
						else{
							choice = -100;
							break;
						}
					}else{
						//Any other option is does not need to call other methods, so the name is stored.
						item = PRODUCTS[choice - 1];
					}
					//Loop to find the item the user picked in the menu and the position in the list.
					for(Inventory i : items){
						if(i.getItemName().equals(item)){
							choice = items.indexOf(i);
							break;
						}
					}
					//If everything was done correctly the loop can terminate.
					validChoice = true;
				}
			}catch(NumberFormatException e){
				//If the user enters a non-numeric value then this error is displayed.
				JOptionPane.showMessageDialog(null, ERROR_NFE, TITLE, JOptionPane.ERROR_MESSAGE);
			}
			
		}while(!validChoice);
		//Returns the position in the items list so the information for that item can be used.
		return choice;
	}
	
	/**
	 * Method to read information from a file for any ShleterPerson type objects.
	 * @param persons
	 * @param filePath
	 * @param type
	 */
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
				
				//If the object is of manager type the password has to be read.
				if(persons.get(personIndex) instanceof Manager){
					divPosition = line.indexOf(';', lastInfoDiv);
					try{
						((Manager)persons.get(personIndex)).setPassword(line.substring(divPosition + 1).trim());
					}
					catch(IllegalArgumentException e){
						JOptionPane.showMessageDialog(null, "No manager password set.\nPlease edit the managerFile and add a password.",
								TITLE, JOptionPane.ERROR_MESSAGE);
					}
					
				}
				//If the object is of employee type the id has to be read.
				else if(persons.get(personIndex)instanceof Employee){
					divPosition = line.indexOf(';', lastInfoDiv);
					((Employee)persons.get(personIndex)).setEmployeeId(line.substring(divPosition + 1).trim());
					Employee.setEmployeeCount(personIndex + 1);
				}
				//If the object is of customer type then the phone number and adoption history has to be read. 
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
			//The manager file must be present before running the program. This prevents the program from running
			//if that file is missing.
			if(filePath.equals(Manager.fileLocation())){
				JOptionPane.showMessageDialog(null, "The manager file (" + filePath + ") must be present.",
						TITLE, JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			}
		}
	}
	
	/**
	 * Method to read the inventory file that stores the information of all items sold in the shelter.
	 * @param items
	 */
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
	
	/**
	 * Method to read the animal file that stores the information of all animals in the shelter.
	 * @param animals
	 */
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
				if(divPosition != -1){
					temp.setAnimalID((line.substring(lastInfoDiv, divPosition).trim()));
					lastInfoDiv = line.indexOf(';') + 1;

					//Finds the next semicolon and cuts the unnecessary parts to only have the last name.
					temp.setAnimalType((line.substring(lastInfoDiv).trim()));
					animals.add(temp);
				}
			}
			//After reading the file close the Scanner object.
			reader.close();
			
			//File not found exception to display an error message to the user.
			}catch(FileNotFoundException e){
				JOptionPane.showMessageDialog(null, "File was not found.", TITLE, JOptionPane.ERROR_MESSAGE);
			}
	}
	
	/**
	 * Method to write to any ShelterPerson object to a file.
	 * @param persons
	 */
	private static void writeShelterPersonToFile(List<ShelterPerson> persons){
		final String TITLE = "Animal Shelter Menu";
		PrintWriter writer = null;
		int firstIndex = 0;
		
		try{
			//If the object is of manager type then the location and information used is from the manager class.
			if(persons.get(firstIndex) instanceof Manager){
				writer = new PrintWriter(new FileOutputStream(Manager.fileLocation()));
				//Loop to go through all the objects and write their information to the file.
				for(ShelterPerson m : persons){
					writer.println(((Manager)m).toFile());
				}
			}
			//If the object is of employee type then the location and information used is from the employee class.
			else if(persons.get(firstIndex) instanceof Employee){
				writer = new PrintWriter(new FileOutputStream(Employee.fileLocation()));
				//Loop to go through all the objects and write their information to the file.
				for(ShelterPerson e : persons){
					writer.println(((Employee)e).toFile());
				}
			}
			//If the object is of customer type then the location and information used is from the customer class.
			else if(persons.get(firstIndex) instanceof Customer){
				writer = new PrintWriter(new FileOutputStream(Customer.fileLocation()));
				//Loop to go through all the objects and write their information to the file.
				for(ShelterPerson e : persons){
					writer.println(((Customer)e).toFile());
				}
			}
			//Closes the PrintWriter object.
			writer.close();
		}catch(FileNotFoundException e){
			JOptionPane.showMessageDialog(null, "File was not found.", TITLE, JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/**
	 * Method to write the information of all animals in the shelter to a file.
	 * @param animals
	 */
	private static void writeAnimalToFile(List<Animal> animals){
		final String TITLE = "Animal Shelter Menu";
		PrintWriter writer = null;
		
		try{
			writer = new PrintWriter(new FileOutputStream(Animal.fileLocation()));
			//Loop to go through all the objects and write their information to the file.
			for(Animal a : animals){
				writer.println(a.toFile());
			}
			//Closes the PrintWriter object.
			writer.close();
		}catch(FileNotFoundException e){
			JOptionPane.showMessageDialog(null, "File was not found.", TITLE, JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/**
	 * Method to write all the inventory information to a file.
	 * @param items
	 */
	private static void writeInventoryToFile(List<Inventory> items){
		final String TITLE = "Animal Shelter Menu";
		PrintWriter writer = null;
		
		try{
			writer = new PrintWriter(new FileOutputStream(Inventory.fileLocation()));
			//Loop to go through all the objects and write their information to the file.
			for(Inventory i : items){
				writer.println(i.toFile());
			}
			//Closes the PrintWriter object.
			writer.close();
		}catch(FileNotFoundException e){
			JOptionPane.showMessageDialog(null, "File was not found.", TITLE, JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/**
	 * Method to write the 12 month simulation to a file.
	 * @param simulation
	 */
	private static void writeSimulationToFile(ExpenseReport[] simulation){
		final String TITLE = "Animal Shelter Menu";
		PrintWriter writer = null;
		
		try{
			writer = new PrintWriter(new FileOutputStream(ExpenseReport.fileLocation()));
			//Loop to go through all the objects and write their information to the file.
			for(int x = 0; x < simulation.length; x++){
				writer.println(simulation[x].toString());
			}
			//Closes the PrintWriter object.
			writer.close();
		}catch(FileNotFoundException e){
			JOptionPane.showMessageDialog(null, "File was not found.", TITLE, JOptionPane.ERROR_MESSAGE);
		}
	}
}