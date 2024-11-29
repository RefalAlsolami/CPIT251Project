


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Information {

    private FileHandler fileHandler;
    String section;
    String problem;
    String solution;
    List<String> lines;
    Scanner scanner;
    // Constructorlines
    public Information(FileHandler fileHandler) throws IOException {
        this.fileHandler = fileHandler;
        this.lines = fileHandler.readData(); 
         scanner = new Scanner(System.in);
    }
    
    // Add new information using append
     public void addInformation() throws IOException {
   

        // Display existing sections
        List<String> sections = getSectionsAndDisplay();

        // Get admin input
        System.out.print("Enter Section: ");
        section = scanner.nextLine();
        System.out.print("Enter Problem: ");
        problem = scanner.nextLine();
        System.out.print("Enter Solution: ");
        solution = scanner.nextLine();

        // Check if section exists and delegate accordingly
        if (sections.contains(section)) {
            appendToSection();
        } else {
            createNewSection();
        }

        // Write updated content back to the file
        fileHandler.writeData(lines);
        System.out.println("Information added successfully!");
    }

    // Helper method: Get and display sections
    private List<String> getSectionsAndDisplay() {
        List<String> sections = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.startsWith("SECTION:")) {
                sections.add(line.substring("SECTION:".length()).trim());
            }
        }

        // Display sections
        if (!sections.isEmpty()) {
            System.out.println("Existing sections:");
            for (String section : sections) {
                System.out.println("- " + section);
            }
        } 

        return sections;
    }

    // Append new information to an existing section
    private void appendToSection() {
        System.out.println("Section exists. Appending problem and solution to it.");
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).equals("SECTION: " + section)) {
                int insertIndex = i + 1;
                while (insertIndex < lines.size() && !lines.get(insertIndex).startsWith("SECTION:")) {
                    insertIndex++;
                }
                lines.add(insertIndex, "PROBLEM: " + problem);
                lines.add(insertIndex + 1, "SOLUTION: " + solution);
                break;
            }
        }
    }

    // Create a new section
    private void createNewSection() {
        System.out.println("Section does not exist. Creating a new section.");
        if (!lines.isEmpty()) {
            lines.add(""); // Add spacing between sections
        }
        lines.add("SECTION: " + section);
        lines.add("PROBLEM: " + problem);
        lines.add("SOLUTION: " + solution);
    }
    // Update a solution
    
    public void updateOrDeleteSolution() throws IOException {
      

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
    public void search(String keyword) throws IOException {
    String currentSection = null;
    String currentProblem = null;
    String currentSolution = null;
    boolean found = false;

    for (int i = 0; i < lines.size(); i++) {
        String line = lines.get(i);

        if (line.startsWith("SECTION:")) {
            currentSection = line.substring("SECTION:".length()).trim();
        } else if (line.startsWith("PROBLEM:")) {
            currentProblem = line.substring("PROBLEM:".length()).trim();
        } else if (line.startsWith("SOLUTION:")) {
            currentSolution = line.substring("SOLUTION:".length()).trim();

            if (currentSolution.contains(keyword)) {
                System.out.println("SECTION: " + currentSection);
                System.out.println("PROBLEM: " + currentProblem);
                String highlightedSolution = currentSolution.replace(keyword, keyword.toUpperCase());
                System.out.println("SOLUTION: " + highlightedSolution);
                System.out.println("-----------------------------------");
                found = true;
            }
        }
    }

    if (!found) {
        System.out.println("No matching keyword found in the file.");
    }
}

    // Print all information
    public void printAllInformation() throws IOException {
        List<String> lines = fileHandler.readData();
        for (String line : lines) {
            System.out.println(line);
        }
    }
    
}
