package animalShelterManager;

/**
 * This is the Employee class. This class will store all the attributes about the employee. The attributes
 * include the first and last name (inherit from ShelterPerosn), and employee ID. The employee will be 
 * assisting customer that want to adopt an animal and purchase from the shelter. 
 */
public class Employee extends ShelterPerson{
	//Variable to store the location of the manager file.
	static final private String FILE_LOCATION = "./src/employeeFile.txt";
	//Variable to store the max number of employees that can work at the animal shelter.
	static final private int MAX_EMPLOYEES = 5;
	//Variable to store the dollars per hour the employees are paid.
	static final private double EMPLOYEE_PAY = 10.00;
	//Variable to store the number max number of hours an employee can work per week.
	static final private int MAX_HOURS = 40;
	//Variable to store the number of employees working at the animal shelter.
	static private int employeeCount;
	//Variable to store the ID of the employee.
	private String employeeID;
	
	//Default constructor to create an Employee object with default attributes.
	public Employee(){
		this("", "", "");
	}
	
	//Specific constructor to create an Employee object with user defined attributes.
	public Employee(String id, String fName, String lName){
		super(fName, lName);
		this.employeeID = id;
	}
	
	/**
	 * Accessor method to return the id of the employee.
	 * @return
	 */
	public String getEmployeeID(){
		return this.employeeID;
	}
	
	/**
	 * Accessor method to return the number of employees at the animal shelter.
	 * @return employeeCount
	 */
	public static int getEmployeeCount(){
		return employeeCount;
	}
	
	/**
	 * Accessor method to return the max number of employees that can work at the animal shelter.
	 * @return int MAX_EMPLOYEES
	 */
	public static int getMaxEmployees(){
		return MAX_EMPLOYEES;
	}
	
	/**
	 * Accessor method to return the amount to be paid to an employee.
	 * All the employees are paid the same and the amount of hours is also the same.
	 * @return double payToEmployee
	 */
	public static double getEmployeePay(){
		double payToEmployee = EMPLOYEE_PAY * MAX_HOURS;
		
		return payToEmployee;
	}
	
	/**
	 * Mutator method to set the number of employees that are currently working at the shelter. The manager is excluded.
	 * @param count
	 */
	public static void setEmployeeCount(int count){
		employeeCount = count;
	}
	
	/**
	 * Mutator method to set the ID of the employee. If the id is blank an exception is thrown.
	 * @param String id
	 * @return void
	 */
	public void setEmployeeId(String id){
		//If the name is blank then an exception is thrown to inform the user.
		if(id.trim().equals("")){
			throw new IllegalArgumentException("The last name cannot be blank");
		}
		//If the name is not blank then the name is stored and a true is return.
		else{
			this.employeeID = id;
		}
	}
	
	/**
	 * Special purpose method to remove an employee from the file. The employee is attributes are 
	 * set to blank and the employee count is decreased by one.
	 */
	public static void removeEmployee(Employee fired){
		fired.employeeID = "";
		fired.setFirstName("");
		fired.setLastName("");
		employeeCount--;
	}
	
	/**
	 * fileLocation method to return the location of the employee file to perform I/O operations.
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
		String employeeInfo = super.toFile() + this.employeeID;
		
		return employeeInfo;
	}
	
	/**
	 * toString method to return the first name, last name and id of an employee.
	 * @return String managerInfo
	 */
	public String toString(){
		String employeeInfo = "ID: " + this.employeeID + super.toString();
		
		return employeeInfo;
	}
}