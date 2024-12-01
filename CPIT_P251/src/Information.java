
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
    public List<String> getSectionsAndDisplay() {
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
    void appendToSection() {
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
    public void createNewSection() {
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
        List<String> sections = getSectionsAndDisplay();
        System.out.print("Enter Section: ");
        section = scanner.nextLine();

        if (!sections.contains(section)) {
            System.out.println("Section does not exist. Please enter a valid section.");
            return;
        }

        System.out.print("Enter Problem to modify: ");
        String problem = scanner.nextLine();

        System.out.println("Do you want to update or delete the problem? (Enter 'update' or 'delete')");
        String action = scanner.nextLine().toLowerCase();

        switch (action) {
            case "delete":
                deleteProblem(section, problem);
                break;
            case "update":
                System.out.print("Enter new Solution: ");
                String newSolution = scanner.nextLine();
                updateSolution(section, problem, newSolution);
                break;
            default:
                System.out.println("Invalid action. Please enter 'update' or 'delete'.");
                break;
        }
    }

    private void deleteProblem(String section, String problem) throws IOException {
        List<String> lines = fileHandler.readData();
        int startIndex = lines.indexOf("SECTION: " + section) + 1;
        boolean isChanged = false;

        for (int i = startIndex; i < lines.size() && !lines.get(i).startsWith("SECTION:"); i++) {
            String currentProblem = lines.get(i).substring("PROBLEM: ".length());
            if (isSimilarSimple(currentProblem, problem)) {
                lines.remove(i);
                lines.remove(i);
                isChanged = true;
                System.out.println("Problem and solution deleted successfully!");
                break;
            }
        }

        if (isChanged) {
            fileHandler.writeData(lines);
        } else {
            System.out.println("Problem not found.");
        }
    }

    private void updateSolution(String section, String problem, String newSolution) throws IOException {
        List<String> lines = fileHandler.readData();
        int startIndex = lines.indexOf("SECTION: " + section) + 1;
        boolean isChanged = false;

        for (int i = startIndex; i < lines.size() && !lines.get(i).startsWith("SECTION:"); i++) {
            String currentProblem = lines.get(i).substring("PROBLEM: ".length());
            if (isSimilarSimple(currentProblem, problem)) {
                lines.set(i + 1, "SOLUTION: " + newSolution);
                isChanged = true;
                System.out.println("Solution updated successfully!");
                break;
            }
        }

        if (isChanged) {
            fileHandler.writeData(lines);
        } else {
            System.out.println("Problem not found.");
        }
    }

    private boolean isSimilarSimple(String text1, String text2) {
        text1 = text1.toLowerCase().trim().replaceAll("\\s+", "");
        text2 = text2.toLowerCase().trim().replaceAll("\\s+", "");

        // Calculate the minimum length of both strings
        int minLength = Math.min(text1.length(), text2.length());
        int maxLength = Math.max(text1.length(), text2.length());
        int matchCount = 0;

        // Compare each character up to the length of the shorter string
        for (int i = 0; i < minLength; i++) {
            if (text1.charAt(i) == text2.charAt(i)) {
                matchCount++;
            }
        }

        // Calculate similarity based on the number of matching characters and the maximum length
        double similarity = (double) matchCount / maxLength;

        // Return true if the similarity is above a threshold, say 60%
        return similarity > 0.6;
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

                if (currentSection.toLowerCase().contains(keyword.toLowerCase())
                        || currentProblem.toLowerCase().contains(keyword.toLowerCase())
                        || currentSolution.toLowerCase().contains(keyword.toLowerCase())) {

                    String highlightedSection = currentSection;
                    if (currentSection.toLowerCase().contains(keyword.toLowerCase())) {
                        highlightedSection = currentSection.replace(keyword, keyword.toUpperCase());
                    }

                    String highlightedProblem = currentProblem;
                    if (currentProblem.toLowerCase().contains(keyword.toLowerCase())) {
                        highlightedProblem = currentProblem.replace(keyword, keyword.toUpperCase());
                    }

                    String highlightedSolution = currentSolution;
                    if (currentSolution.toLowerCase().contains(keyword.toLowerCase())) {
                        highlightedSolution = currentSolution.replace(keyword, keyword.toUpperCase());
                    }

                    System.out.println("SECTION: " + highlightedSection);
                    System.out.println("PROBLEM: " + highlightedProblem);
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

    // Print all information from the file
    public void printAllInformation() throws IOException {
        List<String> lines = fileHandler.readData();
        for (String line : lines) {
            System.out.println(line);
        }
    }

}
