package animalShelterManager;

import javax.swing.JOptionPane;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.PrintWriter;

public class AnimalShelterManager {

	public static void main(String[] args) {
		final String TITLE = "Animal Shelter Manager";
		final String[] LOGIN_OPTIONS = {"Manager", "Employee"};
		final String LOGIN_PROMPT = "Select User Type:";
		
		ShelterPerson shelterManager[] = new Manager[Manager.getMaxManagers()];
		for(int x = 0; x < Manager.getMaxManagers(); x++){
			shelterManager[x] = new Manager();
		}
		
		ShelterPerson shelterEmployees[] = new Employee[Employee.getMaxEmployees()];
		for(int x = 0; x < Employee.getMaxEmployees(); x++){
			shelterEmployees[x] = new Employee();
		}
		
		readPersonFile(shelterManager, Manager.toFile());
		readPersonFile(shelterEmployees, Employee.toFile());
		
		for(int x = 0; x < Manager.getMaxManagers(); x++){
			System.out.println(shelterManager[x].toString() + "\n");
		}
		
		for(int x = 0; x < Employee.getEmployeeCount(); x++){
			System.out.println(shelterEmployees[x].toString() + "\n");
		}
		
		int choice = JOptionPane.showOptionDialog(null, LOGIN_PROMPT, TITLE, 0, JOptionPane.QUESTION_MESSAGE, null, LOGIN_OPTIONS, null);
		if(choice == 0){
			//Example how to use the exceptions being thrown by the DDCs.
			try{
				Manager currentManager = (Manager)shelterManager[0];
				currentManager.validatePassword(JOptionPane.showInputDialog(null, "Enter Password", TITLE, JOptionPane.QUESTION_MESSAGE));
			}catch(IllegalArgumentException e){
				JOptionPane.showMessageDialog(null, e.getMessage(), TITLE, JOptionPane.ERROR_MESSAGE);
			}
		}
		else{
			JOptionPane.showMessageDialog(null, "Add Employee Menu Here");
		}

	}
	
	private static void readPersonFile(ShelterPerson[] persons, String filePath){
		//String for the title of all the messages for this method
		final String TITLE = "Animal Shelter Manager";
		int personCount = 0;
		//Variable to store the last position of the semicolon used for dividing attributes.
		int lastInfoDiv;
		//Variable to store the position where to start to search for the next element.
		int divPosition;
		//Scanner object to read the file set to null.
		Scanner reader = null;
		
		//Try and catch to prevent the program from crashing if the file is missing or other I/O errors.
		try{
			//Scanner object to read the file information
			reader = new Scanner(new FileInputStream(filePath));
			
			//While loop to iterate until there are no more lines to read from the file.
			while(reader.hasNextLine()){
				lastInfoDiv = 0;
				divPosition = 0;
				//String to hold a line from the file.
				String line = reader.nextLine();
				
				//Finds the first semicolon and cuts the unnecessary parts to only have the first name.
				divPosition = line.indexOf(';') + lastInfoDiv;
				persons[personCount].setFirstName(line.substring(lastInfoDiv, divPosition));
				lastInfoDiv = line.indexOf(';') + 1;

				//Finds the next semicolon and cuts the unnecessary parts to only have the last name.
				divPosition = line.indexOf(';', lastInfoDiv);
				persons[personCount].setLastName(line.substring(lastInfoDiv, divPosition));
				lastInfoDiv = line.indexOf(';', divPosition);
				
				if(persons[personCount] instanceof Manager){
					Manager currentManager = (Manager)persons[personCount];
					//Anything after the last semicolon is the password. The white space is trim.
					divPosition = line.indexOf(';', lastInfoDiv);
					currentManager.setPassword(line.substring(divPosition + 1).trim());
				}
				else if(persons[personCount] instanceof Employee){
					Employee currentEmployee = (Employee)persons[personCount];
					divPosition = line.indexOf(';', lastInfoDiv);
					currentEmployee.setEmployeeId(line.substring(divPosition + 1).trim());
					Employee.setEmployeeCount(personCount + 1);
				}
				
				personCount++;
			}
			//After reading the file close the Scanner object.
			reader.close();
		//File not found exception to display an error message to the user.
		}catch(FileNotFoundException e){
			JOptionPane.showMessageDialog(null, "File was not found.", TITLE, JOptionPane.ERROR_MESSAGE);
		}
	}
}
