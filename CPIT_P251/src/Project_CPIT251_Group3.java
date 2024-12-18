
import java.io.IOException;
import java.util.Scanner;

public class Project_CPIT251_Group3 {

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        FileHandler fileHandler = new FileHandler();
        Information information = new Information(fileHandler);
        Admin admin = new Admin(information); // Admin object that inherits from Employee

        int choice = -1; // Initialize choice
        do {
            // Show menu to the user
            System.out.println();
            System.out.println("|-------------------------------------------------------------|");
            System.out.println("|------- Welcome in Technical User Guide System (Menu) -------|");
            System.out.println("|-------------------------------------------------------------|");
            System.out.println("|   1. Add New Information (Admin Only)");
            System.out.println("|   2. Update a Solution (Admin Only)");
            System.out.println("|   3. Search for Information");
            System.out.println("|   4. Display All Information");
            System.out.println("|   5. Exit");
            System.out.println("|-------------------------------------------------------------|");
            System.out.print("Enter your choice: ");

            try {
                choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1: // Add new information (Admin only)
                        if (admin.authorize(scanner)) {
                            admin.addInformation();
                        } else {
                            System.out.println("Authorization failed.");
                        }
                        break;

                    case 2: // Update solution (Admin only)
                        if (admin.authorize(scanner)) {
                            admin.updateSolution();
                        } else {
                            System.out.println("Authorization failed.");
                        }
                        break;

                    case 3: // Search for information (Employee functionality)
                        System.out.print("Enter a keyword to search: ");
                        String keyword = scanner.nextLine();
                        admin.searchInformation(keyword); // من Employee
                        break;

                    case 4: // Display all information (Employee functionality)
                        admin.displayAllInformation(); // من Employee
                        break;

                    case 5: // Exit
                        System.out.println("Exiting... Goodbye!");
                        break;

                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (java.util.InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
                scanner.nextLine();
            }
        } while (choice != 5);

        scanner.close();
    }
}
