


import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Information {

    private FileHandler fileHandler;

    // Constructor
    public Information(FileHandler fileHandler) {
        this.fileHandler = fileHandler;
    }
    
    // Add new information using append
    
    // Update a solution
    
    public void updateOrDeleteSolution() throws IOException {
        Scanner scanner = new Scanner(System.in);

        // Display existing sections and return list of sections
        List<String> sections = getSectionsAndDisplay();

        // Get admin input for section
        System.out.print("Enter Section: ");
        String section = scanner.nextLine();

        // Validate section existence
        if (!sections.contains(section)) {
            System.out.println("Section does not exist. Please enter a valid section.");
            return;
        }

        // Get admin input for problem
        System.out.print("Enter Problem to modify: ");
        String problemToModify = scanner.nextLine();

        // Ask if user wants to update or delete the problem
        System.out.println("Do you want to update or delete the problem? (Enter 'update' or 'delete')");
        String action = scanner.nextLine().toLowerCase();

        // Process based on user decision
        boolean isChanged = false;
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).equals("SECTION: " + section)) {
                i++;
                while (i < lines.size() && !lines.get(i).startsWith("SECTION:")) {
                    if (lines.get(i).equals("PROBLEM: " + problemToModify)) {
                        if (action.equals("delete")) {
                            // Remove problem and solution
                            lines.remove(i); // remove problem
                            lines.remove(i); // remove solution after problem is removed
                            isChanged = true;
                            System.out.println("Problem and solution deleted successfully!");
                        } else if (action.equals("update")) {
                            // Get new solution and update
                            System.out.print("Enter new Solution: ");
                            String newSolution = scanner.nextLine();
                            lines.set(i + 1, "SOLUTION: " + newSolution);
                            isChanged = true;
                            System.out.println("Solution updated successfully!");
                        }
                        break;
                    }
                    i += 2; // Skip to the next problem within the section
                }
            }
            if (isChanged) {
                fileHandler.writeData(lines);
                break;
            }
        }

        if (!isChanged) {
            System.out.println("Problem not found. No updates made.");
        }
    }

    
    // Search for information

    // Print all information
    
}
