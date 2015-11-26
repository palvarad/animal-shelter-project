/**
 * Group: MachineWork
 * Group Members: Larry Hong (G00714737) and Peter Alvarado Nunez (G00884723).
 * Group Leader: Larry Hong
 * Date: 11-11-2015
 * Course: IT 306 - 001
 * Project Phase V - Preliminary System Implementation
 */
package animalShelterManager;

/**
* This is the Inventory class. This class will store all the attributes about the items for sale at the animal shelter.
* The attributes include the name, purchase and sell prices of the items, and the total number of items for each type, 
* The items are the only source of revenue for the animal shelter. The class will also handle the details of purchase
* and sell of inventory.
*/
public class Inventory {
	//Variable to store the location of the manager file.
	static final private String FILE_LOCATION = "./src/inventoryFile.txt";
	//Variable to store the max number of items allowed at the shelter.
	private static final int MAX_INVENTORY = 300;
	//Variable to store the max number of items the shelter sells.
	private static final int MAX_ITEMS = 12;
	//Variable to store the count that it considered too low. 
	private static final int LOW_ALERT = 10;
	//Variable to store the max number of each type of item.
	private static final int MAX_ITEM_TYPES = 25;
	//Variable to keep track of the number of items available in the animal shelter.
	private static int inventoryCount = 0;
	//Variable to store the name of the item
	private String inventoryName;
	//Variable to store the purchase price of the item
	private double inventoryPurchase;
	//Variable to store the sell price of the item
	private double inventorySell;
	//Variable to store the amount of items available for a specific type of item
	private int itemCount;
	
	//Default constructor to create an Inventory object with default attributes.
	public Inventory(){
		this.inventoryName = "";
		this.inventoryPurchase = 0;
		this.inventorySell = 0;
		this.itemCount = 0;
	}
	
	//Specific constructor to create an Animal object with user defined attributes.
	public Inventory(String name, double purchase, double sell, int count){
		this();
		this.inventoryName = name;
		this.inventoryPurchase = purchase;
		this.inventorySell = sell;
		this.itemCount = count;
	}
	
	/**
	 * Accessor method to return the name of the item.
	 * @return
	 */
	public String getItemName(){
		return this.inventoryName;
	}
	
	/**
	 * Accessor method to return the purchase cost of the item.
	 * @return
	 */
	public double getItemPurchaseCost(){
		return this.inventoryPurchase;
	}
	
	/**
	 * Accessor method to return the sell cost of the item.
	 * @return
	 */
	public double getItemSellCost(){
		return this.inventorySell;
	}
	
	/**
	 * Accessor method to return the sell cost of the item.
	 * @return
	 */
	public int getItemCount(){
		return this.itemCount;
	}
	
	/**
	 * Accessor method to return the max number of items the animal shelter can hold.
	 * @return int MAX_INVENTORY
	 */
	public static int getMaxInventory(){
		return MAX_INVENTORY;
	}
	
	/**
	 * Accessor method to return the max number of items allowed for each type.
	 * @return int MAX_ITEM_TYPES
	 */
	public static int getMaxItemTypesCount(){
		return MAX_ITEM_TYPES;
	}
	
	/**
	 * Accessor method to return the max number of items that are sold in the store.
	 * @return int MAX_ITEMS
	 */
	public static int getMaxItemsCount(){
		return MAX_ITEMS;
	}
	
	/**
	 * Accessor method to return the number of items at the animal shelter.
	 * @return int inventoryCount
	 */
	public static int getInventoryCount(){
		return inventoryCount;
	}
	
	/**
	 * Accessor method to return the trigger for low count.
	 * @return
	 */
	public static int getLowAlert(){
		return LOW_ALERT;
	}
	
	/**
	 * Mutator method to set the name of the item. If the name is blank an exception is thrown.
	 * @param String iName
	 * @return boolean
	 */
	public void setItemName(String iName){
		//If the name is blank then an exception is thrown to inform the user.
		if(iName.trim().equals("")){
			throw new IllegalArgumentException("The item name cannot be blank");
		}
		//If the name is not blank then the name is stored and a true is return.
		else{
			this.inventoryName = iName;
		}
	}
	
	/**
	 * Mutator method to set the purchase cost of the item. If the cost is 0 or less an exception is thrown.
	 * @param double pCost
	 * @return void
	 */
	public void setItemPurchasePrice(double pCost){
		//If the cost is 0 or less then an exception is thrown to inform the user.
		if(pCost <= 0){
			throw new NumberFormatException("The cost of the item must be positive.");
		}
		//If the cost is positive then the cost is set.
		else{
			this.inventoryPurchase = pCost;
		}
	}
	
	/**
	 * Mutator method to set the sell cost of the item. If the cost is 0 or less an exception is thrown.
	 * @param double pCost
	 * @return void
	 */
	public void setItemSellPrice(double sCost){
		//If the cost is 0 or less then an exception is thrown to inform the user.
		if(sCost <= 0){
			throw new NumberFormatException("The sell price of the item must be positive.");
		}
		//If the cost is positive then the cost is set.
		else{
			this.inventorySell = sCost;
		}
	}
	
	/**
	 * Mutator method to set the count of the item. If the item is less than 0 an exception is thrown.
	 * @param int count
	 * @return void
	 */
	public void setItemCount(int count){
		//If the count is less than 0 then an exception is thrown to inform the user.
		if(count < 0){
			throw new NumberFormatException("The number of items available for each type must be positive.");
		}
		//If the count is 0 or positive then the count is set.
		else{
			this.itemCount = count;
		}
	}
	
	/**
	 * Mutator method to add the inventory count of a new item to the overall inventory count.
	 * @param invCount
	 */
	public static void setInventoryCount(int invCount){
		//If the count is less than 0 then an exception is thrown to inform the user.
		if(invCount < 0){
			throw new NumberFormatException("The number of items available must be positive or 0.");
		}
		//If the count is 0 or positive then the count is set.
		else{
			inventoryCount += invCount;
		}
	}
	
	/**
	 * Special purpose method to calculate the cost of buying an item and to update the count for that item.
	 * @param amountToPurchase
	 * @return
	 */
	public double inventoryPurchase(int amountToPurchase){
		double purchaseTotal = 0;
		
		if((Inventory.getInventoryCount() + amountToPurchase) > Inventory.getMaxInventory()){
			throw new IllegalArgumentException("Invalid number of items. Buying that amount will exceed the inventory limit.\n"
					+ "Inventory Count:" + Inventory.getInventoryCount() + " Max Inventory: " + Inventory.getMaxInventory());
		}
		else if((this.itemCount + amountToPurchase) > Inventory.getMaxItemTypesCount()){
			throw new IllegalArgumentException("Invalid number of items. Buying that amount will exceed the item limit.\n"
					+ "Item Count: " + this.itemCount + " Max Inventory: " + Inventory.getMaxItemTypesCount());
		}
		else{
			purchaseTotal = this.inventoryPurchase * amountToPurchase;
			this.setItemCount(this.itemCount + amountToPurchase);
			inventoryCount += amountToPurchase;
			return purchaseTotal;
		}
	}
	
	/**
	 * Special purpose method to calculate the cost of selling an item and to update the count for that item.
	 * @param amountToSell
	 * @return
	 */
	public double inventorySell(int amountToSell){
		double sellTotal = 0;
		
		if(amountToSell <= 0){
			throw new IllegalArgumentException("Invalid number of items. The number of items to purchase must be positive.");
		}
		else if(amountToSell > 10){
			throw new IllegalArgumentException("Invalid number of items. Only 10 of each item can be purchased.");
		}
		else if((this.itemCount - amountToSell) < 0){
			throw new IllegalArgumentException("Invalid number of items. There are not sufficient items of that item in inventory to complete the sell.\n"
					+ "Item Count: " + this.itemCount);
		}
		else if((inventoryCount - amountToSell) < 0){
			throw new IllegalArgumentException("Invalid number of items. There are not sufficient items in inventory to complete the sell.\n"
					+ "Inventory Count: " + inventoryCount);
		}
		else{
			sellTotal = this.inventorySell * amountToSell;
			this.setItemCount(this.itemCount - amountToSell);
			inventoryCount -= amountToSell;
			return sellTotal;
		}
	}
	
	/**
	 * fileLocation method to return the location of the inventory file to perform I/O operations.
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
		String inventoryInfo = this.inventoryName + " ; " + this.inventoryPurchase + " ; " + this.inventorySell
						+ " ; " + this.itemCount;
		
		return inventoryInfo;
	}
	
	/**
	 * toString method to return the name, purchase and sell price, and the item count.
	 * @return String inventoryInfo
	 */
	public String toString(){
		String inventoryInfo = "Item Name:" + this.inventoryName + " Purchase Price: " + this.inventoryPurchase + " Sell Price: "
					+ this.inventorySell + " Items Available: " + this.itemCount;
		
		return inventoryInfo;
	}
}