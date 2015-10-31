package animalShelterManager;

/**
 * This is the Customer class. This class will store all the attributes about the customer. The attributes
 * include the first name, last name, phone number, and the animals they have adopted at the animal shelter.
 * The customer will be purchasing goods and adopting animals from the shelter. 
 */
public class Customer {
	//Variable to store the location of the manager file.
	static final private String FILE_LOCATION = "./src/customerFile.txt";
	//Variable to store the max number of animals a customer can adopt.
	static final int MAX_ADOPTIONS = 10;
	//Variable to store the max length of the phone number.
	static final int MAX_PHONE_LENGTH = 12;
	//Variables to store the position of the dashes in the phone number for validation.
	static final int PHONE_LEFT_DASH = 3, PHONE_RIGHT_DASH = 7;
	//Array of the animals the customer has adopted.
	private String[] animalsAdopted;
	//Variable to store the first name of the customer.
	private String firstName;
	//Variable to store the last name of the customer.
	private String lastName;
	//Variable to store the phone number of the customer.
	private String phoneNumber;
	//Variable to keep track of the number of animals a customer has adopted.
	private int adoptionCount;
	
	//Default constructor to create a Customer object with default attributes.
	public Customer(){
		this.firstName = "";
		this.lastName = "";
		this.phoneNumber = "";
		this.adoptionCount = 0;
		this.animalsAdopted = new String[MAX_ADOPTIONS];
	}
	
	//Specific constructor to create a Customer object with user defined attributes.
	public Customer(String fName, String lName, String phone){
		this();
		this.firstName = fName;
		this.lastName = lName;
		this.phoneNumber = phone;
	}
	
	/**
	 * Accessor method to return the first name of the customer.
	 * @return String firstName
	 */
	public String getFirstName(){
		return this.firstName;
	}
	
	/**
	 * Accessor method to return the last name of the customer.
	 * @return
	 */
	public String getLastName(){
		return this.lastName;
	}
	
	/**
	 * Accessor method to return the phone number of the customer.
	 * @return
	 */
	public String getPhoneNumber(){
		return this.phoneNumber;
	}
	
	/**
	 * Accessor method to return the number of animals the customer has adopted.
	 * @return int adoptionCount
	 */
	public int getAdoptionCount(){
		return this.adoptionCount;
	}
	
	/**
	 * Special purpose method to return a list of animals the customer has adopted.
	 * The list only contains the ID of the animal that was adopted.
	 * @return String adoptionList
	 */
	public String getAnimalsAdopted(){
		String adoptionList = "";
		
		for(int i = 0; i < this.adoptionCount; i++){
			if(this.adoptionCount - 1 == i){
				adoptionList += animalsAdopted[i];
			}
			else{
				adoptionList += animalsAdopted[i] + ", ";
			}
		}
		return adoptionList;
	}
	
	/**
	 * Accessor method to return the max number of animals a customer can adopt.
	 * @return
	 */
	public static int getMaxAdoptions(){
		return MAX_ADOPTIONS;
	}
	
	/**
	 * Mutator method to set the first name of the customer. If the name is blank an exception is thrown.
	 * @param String fName
	 * @return boolean
	 */
	public boolean setFirstName(String fName){
		//If the name is blank then an exception is thrown to inform the user.
		if(fName.trim().equals("")){
			throw new IllegalArgumentException("The first name cannot be blank");
		}
		//If the name is not blank then the name is stored and a true is return.
		else{
			this.firstName = fName;
			return true;
		}
	}
	
	/**
	 * Mutator method to set the last name of the customer. If the name is blank an exception is thrown.
	 * @param String fName
	 * @return boolean
	 */
	public boolean setLastName(String lName){
		//If the name is blank then an exception is thrown to inform the user.
		if(lName.trim().equals("")){
			throw new IllegalArgumentException("The last name cannot be blank");
		}
		//If the name is not blank then the name is stored and a true is return.
		else{
			this.lastName = lName;
			return true;
		}
	}
	
	/**
	 * Mutator method to set the phone number of the customer. If the phone number is blank an exception is thrown.
	 * If the phone number is the incorrect format an exception is thrown.
	 * @param String fName
	 * @return boolean
	 */
	public void setPhoneNumber(String phone){
		//If the phone number is blank then an exception is thrown to inform the user.
		if(phone.trim().equals("")){
			throw new IllegalArgumentException("The phone number cannot be blank");
		}
		//If the phone number is not of the correct length then an exception is thrown.
		else if(phone.length() != MAX_PHONE_LENGTH){
			throw new IllegalArgumentException("The length of the phone number is incorrect.\n The length should be "
		                      + MAX_PHONE_LENGTH + " characters. (Ex.703-993-1000)");
		}
		//If the phone number does not contain the dashes in the appropriate places an exception is thrown.
		else if(phone.charAt(PHONE_LEFT_DASH) != '-' || phone.charAt(PHONE_RIGHT_DASH) != '-'){
			throw new IllegalArgumentException("The phone number must have two dashes at the 4th and 8th position. (Ex.703-993-1000)");
		}
		else{
			//For loop to check that all the characters entered except the dashes are valid numbers.
			for(int x = 0; x < MAX_PHONE_LENGTH; x++){
				//If x is equal to the position of the dashes then those positions can be skipped.
				if(x == PHONE_LEFT_DASH || x == PHONE_RIGHT_DASH){
					x++;
				}
				if(!Character.isDigit((phone.charAt(x)))){
					throw new IllegalArgumentException("One or more of the characters in the phone number entered is not a valid number.");
				}
			}
			this.phoneNumber = phone;
		}
	}
	
	/**
	 * Special purpose method to add the ID of the animal the customer wants to adopt to the list of adopted animals.
	 * @param adopt
	 */
	public void adoptAnimal(Animal adopt){
		animalsAdopted[adoptionCount] = Animal.animalAdopted(adopt);
		this.adoptionCount++;
	}
	
	/**
	 * toFile method to return the location of the customer file to perform I/O operations.
	 * @return String fileLocation
	 */
	public static String toFile(){
		return FILE_LOCATION;
	}
	
	/**
	 * toString method to return the first name, last name and id of an employee.
	 * @return String managerInfo
	 */
	public String toString(){
		String customerInfo = "First Name: " + this.firstName + "Last Name: " + this.lastName + " Phone Number: " + this.phoneNumber +
						" Animals Adopted: " + this.getAnimalsAdopted();
		
		return customerInfo;
	}
}