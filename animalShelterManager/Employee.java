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
 * This is the Employee class. This class will store all the attributes about the employee. The attributes
 * include the first and last name (inherit from ShelterPerosn), and employee ID. The employee will be 
 * assisting customer that want to adopt an animal and purchase from the shelter. 
 */
public class Employee extends ShelterPerson implements Comparable<Object>{
	//Variable to store the location of the manager file.
	static final private String FILE_LOCATION = "./src/employeeFile.txt";
	//Variable to store the max number of employees that can work at the animal shelter.
	static final private int MAX_EMPLOYEES = 5, EMPLOYEE_ID_LENGTH  = 3;
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
			throw new IllegalArgumentException("The employee id cannot be blank");
		}
		else if(id.trim().length() != EMPLOYEE_ID_LENGTH){
			throw new IllegalArgumentException("The employee id must be " + EMPLOYEE_ID_LENGTH + " characters in lenght.");
		}
		//If the name is not blank then the name is stored and a true is return.
		else{
			//For loop to check that all the characters entered except the dashes are valid numbers.
			for(int x = 0; x < EMPLOYEE_ID_LENGTH; x++){
				//If any of the positions where a number should be is not a number then this exception is thrown.
				if(!Character.isDigit((id.charAt(x)))){
					throw new IllegalArgumentException("All characters in the employee id must be numeric values.");
				}
			}
			this.employeeID = id;
		}
	}
	
	/**
	 * Special purpose method to compare the id of a newly created employee with current employees
	 * to avoid two employees with the same id.
	 * @return -2: invalid. 0: equal. -1: not equal.
	 */
	public int compareTo(Object o){
		if(!(o instanceof Employee)){
			return -2;
		}
		Employee emp = (Employee)o;
		if(emp.getEmployeeID().equals(this.getEmployeeID())){
			return 0;
		}
		else{
			return -1;
		}
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
		String employeeInfo = "ID: " + this.employeeID + " " + super.toString();
		
		return employeeInfo;
	}
}