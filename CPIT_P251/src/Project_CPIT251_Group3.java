
import java.io.IOException;
import java.util.Scanner;

public class Project_CPIT251_Group3 {

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        FileHandler fileHandler = new FileHandler();
        Information information = new Information(fileHandler);
        Admin admin = new Admin(); // Single admin setup

        int choice;
        do {
            // Show menu to the user
            System.out.println("\nMenu:");
            System.out.println("1. Add New Information (Admin Only)");
            System.out.println("2. Update a Solution (Admin Only)");
            System.out.println("3. Search for Information");
            System.out.println("4. Print All Information");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (choice) {
                case 1: // Add new information (Admin only)
                    if (admin.authorize(scanner)) {
                        information.addInformation();
                    } else {
                        System.out.println("Authorization failed. You are not allowed to add new information.");
                    }
                    break;

                case 2: // Update a solution (Admin only)
                    if (admin.authorize(scanner)) {
                        try {
                            information.updateOrDeleteSolution();  // Assuming this method exists in Information
                        } catch (IOException e) {
                            System.out.println("An error occurred while updating the solution: " + e.getMessage());
                        }
                    } else {
                        System.out.println("Authorization failed. You are not allowed to update solutions.");
                    }
                    break;

                case 3:
                    System.out.print("Enter a keyword to search: ");
                    String searchKeyword = scanner.nextLine();
                    information.search(searchKeyword);
                    break;

                case 4: // Print all information from the file
                    information.printAllInformation();
                    break;

                case 5: // Exit
                    System.out.println("Exiting... Goodbye!");
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 5);

        scanner.close();
    }
}
