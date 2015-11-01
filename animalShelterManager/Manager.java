package animalShelterManager;

/**
 * This is the ShelterManager class. This class will store all the attributes about the manager. The attributes
 * include the first name and last name of the manager as well as the password to access the manager menu
 * The manager will have administrative access to everything within the software and the ability to purchase
 * inventory supplies, see employee information, as well as generate expense reports.
 */
public class Manager extends ShelterPerson{
	//Variable to store the location of the manager file.
	static final private String FILE_LOCATION = "./src/managerFile.txt";
	static final private int MAX_MANAGERS = 1;
	//Variable to store the password of the manager.
	private String password;
	
	//Default constructor to create a ShelterManager object with default attributes.
	public Manager(){
		this("", "", "");
	}
	
	//Specific constructor to create a ShelterManager object with user defined attributes.
	public Manager(String fName, String lName, String pass){
		super(fName, lName);
		this.password = pass;
	}
	
	public static int getMaxManagers(){
		return MAX_MANAGERS;
	}
	
	/**
	 * Special purpose method to validate that the password entered by the user is the correct manager password.
	 * If the password entered is blank or incorrect then an exception is thrown to inform the user.
	 * @param String pass
	 * @return void
	 */
	public void validatePassword(String pass){
		//If the password is blank an exception is thrown to inform the user.
		if(pass.trim().equals("")){
			throw new IllegalArgumentException("The password cannot be blank");
		}
		//If the password is incorrect an exception is thrown to inform the user.
		else if(!pass.equals(this.password)){
			throw new IllegalArgumentException("The password entered is incorrect");
		}
	}
	
	
	/**
	 * Mutator method to set the password of the manager. If the password is blank an exception is thrown.
	 * @param pass
	 * @return boolean
	 */
	public boolean setPassword(String pass){
		//If the password is blank then an exception is thrown to inform the user.
		if(pass.trim().equals("")){
			throw new IllegalArgumentException("The last name cannot be blank");
		}
		//If the password is not blank then is set an a true is return. 
		else{
			this.password = pass;
			return true;
		}
	}
	
	/**
	 * toFile method to return the location of the manager file to perform I/O operations.
	 * @return String fileLocation
	 */
	public static String toFile(){
		return FILE_LOCATION;
	}
	
	/**
	 * toString method to return the first name and last name of the manager.
	 * @return String managerInfo
	 */
	public String toString(){
		String managerInfo = "Manager: \n" + super.toString();
		
		return managerInfo;
	}
}
