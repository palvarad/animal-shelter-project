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
	//Array to store the available types of animals at the shelter.
	private static final String[] ANIMAL_TYPES = {"Dog", "Cat","Rodent", "Bird"};
	//variables to store the initial letter associated for each type to be used for the ID
	private static Character[] ANIMAL_INITS = {'D', 'C', 'R', 'B'};
	//Variable to store the total number of animals that have been in the shelter.
	private static int animalCount;
	//Variable to store the total number of animals that have ever been in the shelter.
	private static int animalHistory;
	//Variable to store the animal type
	private String animalType;
	//Variable to store the id of the animal
	private String animalID;
	
	//Default constructor to create an Animal object with default attributes.
	public Animal(){
		this.animalType = "";
		this.animalID = "";
	}
	
	//Specific constructor to create an Animal object with user defined attributes.
	public Animal(String type){
		this();
		this.animalType = type;
		this.animalID = createAnimalID(this.animalType); 
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
	 * Accessor method to return the animal type count by using the length of the array.
	 * @return
	 */
	public static int getAnimalTypesCount(){
		return ANIMAL_TYPES.length;
	}
	
	/**
	 * Accessor method to return the animal type using from the array.
	 * @return
	 */
	public static String getAnimalTypes(int index){
		return ANIMAL_TYPES[index];
	}
	
	/**
	 * Accessor method to retrieve the value of the animal history count.
	 * @return historyCount
	 */
	public static int getAnimalHistory(){
		return animalHistory;
	}
	
	/**
	 * Mutator method to change the value of the animal history count to avoid duplicate IDs.
	 * @param historyCount
	 */
	public static void setAnimalHistory(int historyCount){
		animalHistory = historyCount;
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
			if(this.animalID.equals("")){
				this.animalID = createAnimalID(this.animalType);
			}
		}
	}
	
	/**
	 * Special purpose method to make changes to the Animal class when an animal is adopted.
	 * @param type
	 * @return String animalID
	 */
	public static String animalAdopted(Animal adopted){
		animalCount--;
		
		return adopted.getAnimalID();
	}
	
	/**
	 * Accessor method to return the number of animals currently in the shelter.
	 * @return int animalCount
	 */
	public static int getAnimalCount(){
		return animalCount;
	}
	
	public void setAnimalID(String id){
		//If the name is blank then an exception is thrown to inform the user.
		if(id.trim().equals("")){
			throw new IllegalArgumentException("The animal id cannot be blank");
		}
		//If the name is not blank then the name is stored and a true is return.
		else{
			this.animalID = id;
		}
	}
	
	/**
	 * Special purpose method to create the ID for each animal. Based on the type the first letter of the ID changes.
	 * The rest of the ID is calculated by adding INIT_ID and the animal count.
	 * @param typeToCreate
	 * @return String newID
	 */
	private static String createAnimalID(String typeToCreate){
		String newID = "";
		animalCount++;
		animalHistory++;
		
		for(int x = 0; x < ANIMAL_TYPES.length; x++ ){
			if(typeToCreate.equals(getAnimalTypes(x))){
				newID = ANIMAL_INITS[x] + Integer.toString((INIT_ID + animalHistory));
				break;
			}
		}
		
		return newID;
	}
	
	/**
	 * Mutator method to set the number of animals currently in the shelter.
	 * @param int count
	 */
	public static void setAnimalCount(int count){
		animalCount = count;
	}
	
	/**
	 * fileLocation method to return the location of the animal file to perform I/O operations.
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
		String animalInfo = this.animalID + " ; " + this.animalType;
		
		return animalInfo;
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