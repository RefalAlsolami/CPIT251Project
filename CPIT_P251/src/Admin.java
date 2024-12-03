
import java.util.Scanner;

public class Admin {

    private String adminID = "22";
    private String adminPassword = "123";

    // Constructor
    public Admin() {
        this.adminID = adminID;
        this.adminPassword = adminPassword;
    }

    // Check if the user is an admin
    public boolean isAdmin(String inputID, String inputPassword) {
        return this.adminID.equals(inputID) && this.adminPassword.equals(inputPassword);
    }

    // Authorize admin
    public boolean authorize(Scanner scanner) {
        System.out.print("Enter Admin User name: ");
        String username = scanner.nextLine();
        System.out.print("Enter Admin Password: ");
        String password = scanner.nextLine();
        return isAdmin(username, password);
    }
}
