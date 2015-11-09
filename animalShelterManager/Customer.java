package animalShelterManager;

/**
 * This is the Customer class. This class will store all the attributes about the customer. The attributes
 * include the first and last name (inherit from ShleterPerson), phone number, and the animals they have adopted.
 * The customer will be purchasing goods and adopting animals from the shelter. 
 */
public class Customer extends ShelterPerson{
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
	//Variable to store the phone number of the customer.
	private String phoneNumber;
	//Variable to keep track of the number of animals a customer has adopted.
	private int adoptionCount;
	
	//Default constructor to create a Customer object with default attributes.
	public Customer(){
		this("","","");
		
	}
	
	//Specific constructor to create a Customer object with user defined attributes.
	public Customer(String fName, String lName, String phone){
		super(fName, lName);
		this.phoneNumber = phone;
		this.adoptionCount = 0;
		this.animalsAdopted = new String[MAX_ADOPTIONS];
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
			adoptionList += animalsAdopted[i] + " ; ";
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
	 * Mutator method to change the number of animals the customer has adopted.
	 * @param count
	 */
	public void setAdoption(String adoptedAnimal){
		this.animalsAdopted[this.getAdoptionCount()] = adoptedAnimal;
		this.adoptionCount++;
	}
	/**
	 * Mutator mehtod to set the customer's phone number.
	 * @param phone
	 */
	public void setPhoneNumber(String phone){
		//If all the checks passed then the phone number is set.
		this.phoneNumber = phone;
	}
	
	/**
	 * Mutator method to set the phone number of the customer. If the phone number is blank an exception is thrown.
	 * If the phone number is the incorrect format(Ex.703-993-1000) an exception is thrown.
	 * @param String fName
	 * @return boolean
	 */
	public static void validatePhoneNumber(String phone){
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
				//If any of the positions where a number should be is not a number then this exception is thrown.
				if(!Character.isDigit((phone.charAt(x)))){
					throw new IllegalArgumentException("One or more of the characters in the phone number entered is not a valid number.");
				}
			}
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
	 * fileLocation method to return the location of the customer file to perform I/O operations.
	 * @return String fileLocation
	 */
	public static String fileLocation(){
		return FILE_LOCATION;
	}
	
	/**
	 * Special purpose method to write to a file the contents of the object using a specific format.
	 * @return String managerInfo
	 */
	public String toFile(){
		String customerInfo = super.toFile() + this.phoneNumber + " ; ";
		
		if(this.getAdoptionCount() > 0){
			customerInfo += this.getAnimalsAdopted();
		}
		
		return customerInfo;
	}
	
	/**
	 * toString method to return the first name, last name and id of an employee.
	 * @return String managerInfo
	 */
	public String toString(){
		String customerInfo = super.toString() + " Phone Number: " + this.phoneNumber +
						" Animals Adopted: " + this.getAnimalsAdopted();
		
		return customerInfo;
	}
}