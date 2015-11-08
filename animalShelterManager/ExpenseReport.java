package animalShelterManager;

/**
 * This is the ExpenseReport class. This class will store all the attributes about the expense reports. The attributes
 * include the name of the report and the number of times to multiply each cost. Also, a 12 month simulation of
 * expenses and profit. The ExpenseReport will help the manager keep track of expenses and reports. 
 */
public class ExpenseReport {
	//Variable to store the location of the manager file.
	static final private String FILE_LOCATION = "./src/expenseReportFile.txt";
	//Variables to store the number of times an item should be multiply to calculate expense.
	static final private int FOOD_MULTIPLY = 4, MEDICINE_MULTIPLY = 2;
	//Variable to store the profit of the current month
	private double monthProfit;
	//Variable to store the expense of the current month
	private double monthExpense;
	//Variable to store the name of the report.
	private String reportName;
	
	//Default constructor to create an ExpenseReport object with default attributes.
	public ExpenseReport(){
		this.reportName = "";
		this.monthProfit = 0;
		this.monthExpense = 0;
	}
	
	//Specific constructor to create an ExpenseReport object with user defined attributes.
	public ExpenseReport(String name, double profit, double expense){
		this();
		this.reportName = name;
		this.monthProfit = profit;
		this.monthExpense = expense;
	}
	
	/**
	 * Accessor method to return the report name.
	 * @return
	 */
	public String getReportName(){
		return this.reportName;
	}
	
	/**
	 * Accessor method to return the total expenses for the current month.
	 * @return
	 */
	public double getMonthExpense(){
		return this.monthExpense;
	}
	
	/**
	 * Accessor method to return the total profit for the current month.
	 * @return
	 */
	public double getMonthProfit(){
		return this.monthProfit;
	}
	
	public static int getFoodMultiplier(){
		return FOOD_MULTIPLY;
	}
	
	public static int getMedicineMultiplier(){
		return  MEDICINE_MULTIPLY;
	}
	
	/**
	 * Mutator method to set the name of the report. If the name is blank an exception is thrown.
	 * @param String rName
	 * @return boolean
	 */
	public void setReportName(String rName){
		//If the name is blank then an exception is thrown to inform the user.
		if(rName.trim().equals("")){
			throw new IllegalArgumentException("The report name cannot be blank");
		}
		//If the name is not blank then the name is stored.
		else{
			this.reportName = rName;
		}
	}
	
	/**
	 * Mutator method add an action that earned profit for the current month. No checks need to be performed, because
	 * the value from the inventory class should be passed to this method. The inventory class already handles all checks.
	 * @param double earned
	 * @return void
	 */
	public void setMonthProfit(double earned){
		this.monthProfit += earned;
	}
	
	/**
	 * Mutator method subtract an action that is an expense for the current month. No checks need to be performed, because
	 * the value from the inventory class should be passed to this method. The inventory class already handles all checks.
	 * @param double expense
	 * @return void
	 */
	public void setMonthExpense(double expense){
		this.monthExpense += expense;
	}
	
	public static double calculateMonthlyExpense(double expense, int multiplier){
		double monthlyExpense = 0;
		
		monthlyExpense = expense * multiplier;
		
		return monthlyExpense;
	}
	
	/**
	 * Special purpose method to calculate the cost of an animal or a group of animals
	 * @param numberOf (This variable represents the number of animals to multiply the expense by) 
	 * @param foodExpense (The cost of the food expense for the animal type)
	 * @param medicineExpense (The cost of the medicine expense for the animal type)
	 * @return
	 */
	public static double calculateAnimalExpense(int numberOf, double foodExpense, double medicineExpense){
		double total = 0;
		total = foodExpense + medicineExpense;
		total = total * numberOf;
		
		return total;
	}
	
	/**
	 * Special purpose to calculate the cost of an employee or group of employees
	 * @param employeeCount (The number of employees to calculate for)
	 * @param employeeCost (The cost of the employee)
	 * @return
	 */
	public static double calculateEmployeeExpense(int employeeCount, double employeeCost){
		double total = 0;
		
		total = employeeCost * employeeCount;
		
		return total;
	}
	
	/**
	 * Special purpose method to calculate the profit of a simulated month.
	 * @param percentToSell (The percentage, in integer, the manager expects to sell)
	 * @param averageOfInventory (The average of the inventory using the sell price)
	 * @return
	 */
	public static double simulateMonthProft(double percentToSell, double averageOfInventory){
		double simulatedProfit = 0;
		int itemsSold = 0;
		
		itemsSold = (int)(Inventory.getMaxInventory() * (percentToSell/100));
		simulatedProfit = itemsSold * averageOfInventory;
		
		return simulatedProfit;
	}
	
	/**
	 * Special purpose method to calculate the expense of a simulated month.
	 * @param percentToSell (The percentage, in integer, the manager expects to sell)
	 * @param averageOfInventory (The average of the inventory using the purchase price)
	 * @return
	 */
	public static double simulateMonthExpense(double percentToSell, double averageOfInventory){
		double restockCost = 0;
		int itemsSold = 0;
		int itemsToPurchase = 0;
		
		itemsSold = (int)(Inventory.getMaxInventory() * (percentToSell/100));
		itemsToPurchase = Inventory.getMaxInventory() - itemsSold;
		restockCost = itemsToPurchase * averageOfInventory;
		
		return restockCost;
	}
	
	/**
	 * fileLocation method to return the location of the ExpenseReport file to perform I/O operations.
	 * @return String fileLocation
	 */
	public static String fileLocation(){
		return FILE_LOCATION;
	}
	
	/**
	 * toString method to return the name, profit, and expense.
	 * @return String expenseReportInfo
	 */
	public String toString(){
		String expenseReportInfo = "Name: " + this.reportName + " Profit: " + 
					String.format("$%,.2f", this.monthProfit) + " Expense: " + String.format("$%,.2f" ,this.monthExpense);
		
		return expenseReportInfo;
	}
}