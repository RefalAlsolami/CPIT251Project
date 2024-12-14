
import java.util.Scanner;

public class Admin {

    private String adminID = "AA";
    private String adminPassword = "123";

    public Admin() {
        this.adminID = adminID;
        this.adminPassword = adminPassword;
    }

    public boolean isAdmin(String inputID, String inputPassword) {
        return this.adminID.equals(inputID) && this.adminPassword.equals(inputPassword);
    }

    // Authorize admin
    public boolean authorize(Scanner scanner) {
        System.out.print("Enter Admin UserName: ");
        String username = scanner.nextLine();
        System.out.print("Enter Admin Password: ");
        String password = scanner.nextLine();
        return isAdmin(username, password);
    }
}
