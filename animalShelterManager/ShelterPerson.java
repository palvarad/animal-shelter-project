/**
 * Group: MachineWork
 * Group Members: Larry Hong (G00714737) and Peter Alvarado Nunez (G00884723).
 * Group Leader: Larry Hong
 * Date: 12-08-2015
 * Course: IT 306 - 001
 * Project Phase VI - Final System Implementation
 */
package animalShelterManager;

/**
 * This is the ShelterPerson class. This class will store all the attributes about any person object. The attributes
 * include the first name and last name. This class will be used by any classes that represent a person in the shelter.
 */
public abstract class ShelterPerson {
	//Variable to store the first name of the manager
	private String firstName;
	//Variable to store the last name of the manager.
	private String lastName;
	
	//Default constructor for the ShelterPerson
	public ShelterPerson(){
		this.firstName = "";
		this.lastName = "";
	}
	
	/**
	 * Specific constructor for the ShelterPerson. It accepts two string variables for the first and last name.
	 * @param fName
	 * @param lName
	 */
	public ShelterPerson(String fName, String lName){
		this();
		this.firstName = fName;
		this.lastName = lName;
	}
	
	/**
	 * Accessor method to return the first name.
	 * @return String firstName
	 */
	public String getFirstName(){
		return this.firstName;
	}
	
	/**
	 * Accessor method to return the last name.
	 * @return String lastName
	 */
	public String getLastName(){
		return this.lastName;
	}
	
	/**
	 * Mutator method to set the first name. If the name is blank an exception is thrown.
	 * @param String fName
	 * @return boolean
	 */
	public void setFirstName(String fName){
		//If the user hits cancel a null is sent. Error message to let the user know they must enter a first name. 
		if(fName == null){
			throw new IllegalArgumentException("Sorry, but a first name must be entered.");
		}
		
		//If the name is blank then an exception is thrown to inform the user.
		if(fName.trim().equals("")){
			throw new IllegalArgumentException("The first name cannot be blank");
		}
		//If the name is not blank then the name is stored and a true is return.
		else{
			//Loop to check that all characters are letters and there are no digits.
			for(int x = 0; x < fName.length(); x++){
				if(Character.isDigit(fName.charAt(x))){
					throw new IllegalArgumentException("No digits are allowed in the name.");
				}
			}
			this.firstName = fName;
		}
	}
	
	/**
	 * Mutator method to set the last name. If the name is blank an exception is thrown.
	 * @param lName
	 * @return boolean
	 */
	public void setLastName(String lName){
		//If the user hits cancel a null is sent. Error message to let the user know they must enter a last name. 
		if(lName == null){
			throw new IllegalArgumentException("Sorry, but a last name must be entered.");
		}
		
		//If the name is blank then an exception is thrown to inform the user.
		if(lName.trim().equals("")){
			throw new IllegalArgumentException("The last name cannot be blank");
		}
		//If the name is not blank then the name is stored and a true is return.
		else{
			//Loop to check that all characters are letters and there are no digits.
			for(int x = 0; x < lName.length(); x++){
				if(Character.isDigit(lName.charAt(x))){
					throw new IllegalArgumentException("No digits are allowed in the name.");
				}
			}
			this.lastName = lName;
		}
	}
	
	/**
	 * Special purpose method to write to a file the contents of the object using a specific format.
	 * @return String personInfo
	 */
	public String toFile(){
		String personInfo = this.firstName + " ; " + this.lastName + " ; ";
		
		return personInfo;
	}
	
	/**
	 * toString method to return the first name and last name.
	 * @return String managerInfo
	 */
	public String toString(){
		String personInfo = "First Name: " + this.firstName + " Last Name: " + this.lastName;
		
		return personInfo;
	}
}
