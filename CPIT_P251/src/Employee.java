import java.io.IOException;
import java.util.Scanner;

public class Employee {

    protected Information information;
    // Constructor to initialize the Employee with Information object
    public Employee(Information information) {
        this.information = information;
    }
    // Method to search for information based on a keyword
    public void searchInformation(String keyword) throws IOException {
        System.out.println("Searching for keyword: " + keyword);
        information.search(keyword);
    }
    // Method to display all available information
    public void displayAllInformation() throws IOException {
        System.out.println("Displaying all information:");
        information.displayAllInformation();
    }
}
