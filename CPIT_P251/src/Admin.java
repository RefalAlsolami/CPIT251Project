
import java.io.IOException;
import java.util.Scanner;

public class Admin extends Employee {

    private String adminID = "AA"; 
    private String adminPassword = "123";

    public Admin(Information information) {
        super(information);
    }

    public boolean isAdmin(String inputID, String inputPassword) {
        return this.adminID.equals(inputID) && this.adminPassword.equals(inputPassword);
    }

    public boolean authorize(Scanner scanner) {
        System.out.print("Enter Admin UserName: ");
        String username = scanner.nextLine();
        System.out.print("Enter Admin Password: ");
        String password = scanner.nextLine();
        return isAdmin(username, password);
    }

    public void addInformation() throws IOException {
        information.addInformation(getAdminID()); 
    }

    public void updateSolution() throws IOException {
        information.updateSolution(getAdminID()); 
    }

    public String getAdminID() {
        return adminID;
    }
}
