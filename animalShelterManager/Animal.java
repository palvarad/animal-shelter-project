package animalShelterManager;

/**
* This is the Animal class. This class will store all the attributes about the animals. The attributes
* include the animal ID, the animal type, the number of animals of that type at the animal shelter.
* The animals will be adopted by the customers that go to the shelter. 
*/
public class Animal {
	//Variable to store the location of the manager file.
	static final private String FILE_LOCATION = "./src/animalFile.txt";
	//Variable to store the value to be used to create an ID for an animal
	private static final int INIT_ID = 1000;
	//Variable to store the max number of animals that can be at the shelter.
	private static final int MAX_ANIMALS = 100;
	//variables to store the initial letter associated for each type to be used for the ID
	private static Character dogInit = 'D', catInit = 'C', rodentInit = 'R', birdInit = 'B';
	//Variables to store the total number of animals of each type at the shelter
	private static int dogCount = 0, catCount = 0, rodentCount = 0, birdCount = 0;
	//Variable to store the total number of animals at the shelter.
	private static int animalCount;
	//Variable to store the animal type
	private String animalType;
	//Variable to store the id of the animal
	private String animalID;
	
	//Default constructor to create an Animal object with default attributes.
	public Animal(){
		this.animalType = "";
		animalCount++;
	}
	
	//Specific constructor to create an Animal object with user defined attributes.
	public Animal(String type){
		this();
		this.animalType = type;
		this.animalID = createID(this.animalType); 
	}

	/**
	 * Accessor method to return the ID of the animal.
	 * @return String animalID
	 */
	public String getAnimalID(){
		return this.animalID;
	}
	
	/**
	 * Accessor method to return the type of the animal.
	 * @return String animalID
	 */
	public String getAnimalType(){
		return this.animalType;
	}
	
	/**
	 * Accessor method to return the max number of animals the shelter can accommodate.
	 * @return
	 */
	public static int getMaxAnimals(){
		return MAX_ANIMALS;
	}
	
	/**
	 * Mutator method to set the animal type. If the type is empty then an exception is thrown.
	 * @param type
	 * @return void
	 */
	public void setAnimalType(String type){
		//If the name is blank then an exception is thrown to inform the user.
		if(type.trim().equals("")){
			throw new IllegalArgumentException("The animal type cannot be blank");
		}
		//If the name is not blank then the name is stored and a true is return.
		else{
			this.animalType = type;
		}
	}
	
	/**
	 * Special purpose method to make changes to the Animal class when an animal is adopted.
	 * @param type
	 * @return String animalID
	 */
	public static String animalAdopted(Animal adopted){
		animalCount--;
		
		switch(adopted.getAnimalType()){
			case "Dog":
				dogCount--;
				break;
			case "Cat":
				catCount--;
				break;
			case "Rodent":
				rodentCount--;
				break;
			case "Bird":
				birdCount--;
				break;
		}
		
		return adopted.getAnimalID();
	}
	
	/**
	 * Accessor method to return the number of animals currently in the shelter.
	 * @return int animalCount
	 */
	public static int getAnimalCount(){
		return animalCount;
	}
	
	/**
	 * Special purpose method to return the number of animals of a specific type.
	 * @param type
	 * @return
	 */
	public static int getTypeCount(String type){
		int typeCount = 0;
		
		switch(type){
			case "Dog":
				typeCount = dogCount;
				break;
			case "Cat":
				typeCount = catCount;
				break;
			case "Rodent":
				typeCount = rodentCount;
				break;
			case "Bird":
				typeCount = birdCount;
				break;
		}
		return typeCount;
	}
	
	/**
	 * Special purpose method to create the ID for each animal. Based on the type the first letter of the ID changes.
	 * The rest of the ID is calculated by adding INIT_ID and the animal count.
	 * @param typeToCreate
	 * @return String newID
	 */
	private static String createID(String typeToCreate){
		String newID = "";
		
		switch(typeToCreate){
			case "Dog":
				newID = dogInit + Integer.toString((INIT_ID + animalCount));
				dogCount++;
				break;
			case "Cat":
				newID = catInit + Integer.toString((INIT_ID + animalCount));
				catCount++;
				break;
			case "Rodent":
				newID = rodentInit + Integer.toString((INIT_ID + animalCount));
				rodentCount++;
				break;
			case "Bird":
				newID = birdInit + Integer.toString((INIT_ID + animalCount));
				birdCount++;
				break;
		}
		return newID;
	}
	
	/**
	 * toFile method to return the location of the animal file to perform I/O operations.
	 * @return String fileLocation
	 */
	public static String toFile(){
		return FILE_LOCATION;
	}
	
	/**
	 * toString method to return the id and type of the animal.
	 * @return String managerInfo
	 */
	public String toString(){
		String animalInfo = "ID: " + this.animalID + "Type: " + this.animalType;
		
		return animalInfo;
	}
}