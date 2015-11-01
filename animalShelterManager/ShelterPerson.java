package animalShelterManager;

public abstract class ShelterPerson {
	//Variable to store the first name of the manager
	private String firstName;
	//Variable to store the last name of the manager.
	private String lastName;
	
	public ShelterPerson(){
		this.firstName = "";
		this.lastName = "";
	}
	
	public ShelterPerson(String fName, String lName){
		this();
		this.firstName = fName;
		this.lastName = lName;
	}
	
	/**
	 * Accessor method to return the first name of the manager.
	 * @return String firstName
	 */
	public String getFirstName(){
		return this.firstName;
	}
	
	/**
	 * Accessor method to return the last name of the manager.
	 * @return
	 */
	public String getLastName(){
		return this.lastName;
	}
	
	/**
	 * Mutator method to set the first name of the manager. If the name is blank an exception is thrown.
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
	 * Mutator method to set the last name of the manager. If the name is blank an exception is thrown.
	 * @param lName
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
	 * toString method to return the first name and last name of the manager.
	 * @return String managerInfo
	 */
	public String toString(){
		String personInfo = "First Name: " + this.firstName + " Last Name: " + this.lastName;
		
		return personInfo;
	}
}
