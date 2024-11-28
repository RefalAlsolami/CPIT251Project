
package Project_CPIT251_Group3_2024;

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

    // Constructor
    public Information(FileHandler fileHandler) throws IOException {
        this.fileHandler = fileHandler;
        this.lines = fileHandler.readData();
    }
    
     
    // Add new information using append
    public void addInformation() throws IOException {
        Scanner scanner = new Scanner(System.in);

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
        } else {
            System.out.println("No sections found.");
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
 //-----------------------------------------------------------
    
    
    // Search for a keyword
    public void search(String keyword) throws IOException {
        String currentSection = null;
        String currentProblem = null;
        String currentSolution = null;
        boolean found = false;

        for (String line : lines) {
            if (line.startsWith("SECTION:")) {
                currentSection = line.substring("SECTION:".length()).trim();
            } else if (line.startsWith("PROBLEM:")) {
                currentProblem = line.substring("PROBLEM:".length()).trim();
            } else if (line.startsWith("SOLUTION:")) {
                currentSolution = line.substring("SOLUTION:".length()).trim();

                // Check if the keyword exists in the solution
                if (currentSolution.contains(keyword)) {
                    System.out.println("SECTION: " + currentSection);
                    System.out.println("PROBLEM: " + currentProblem);
                    System.out.println("SOLUTION: " + currentSolution);
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

        // Update a solution

}
