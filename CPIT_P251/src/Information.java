
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Information {

    private FileHandler fileHandler;
    private String section;
    private String problem;
    private String solution;
    private List<String> content;
    Scanner scanner;

    // Constructorlines
    public Information(FileHandler fileHandler) throws IOException {
        this.fileHandler = fileHandler;
        this.content = fileHandler.readData();
        scanner = new Scanner(System.in);
    }
    //------------------------------------------------------------------------------------
    //Add Information

    public void addInformation() throws IOException {
        // Display existing sections
        List<String> sections = getSectionsAndDisplay();

        // Collect user input 
        collectUserInput();

        // Check for similar section
        String matchedSection = findMatchingSection(sections, section);

        // Use the matched section if found, otherwise create a new section
        if (matchedSection != null) {
            System.out.println("Section found: " + matchedSection);
            section = matchedSection; // Use the matched section name
            appendToSection();
        } else {
            System.out.println("No similar section found. Creating a new section.");
            createNewSection();
        }

        // Write updated content back to the file
        fileHandler.writeData(content);
        System.out.println("Information added successfully!");
    }

    //---------------------------------------------------------------
    public void collectUserInput() {
        System.out.print("Enter Section: ");
        section = scanner.nextLine();
        System.out.print("Enter Problem: ");
        problem = scanner.nextLine();
        System.out.print("Enter Solution: ");
        solution = scanner.nextLine();
    }
    //---------------------------------------------------------------
    //Get and display sections

    public List<String> getSectionsAndDisplay() {
        List<String> sections = new ArrayList<>();
        for (int i = 0; i < content.size(); i++) {
            String line = content.get(i);
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

    //------------------------------------------------------------  
    public String findMatchingSection(List<String> sections, String inputSection) {
        for (int i = 0; i < sections.size(); i++) {
            String existingSection = sections.get(i);
            if (isSimilar(existingSection, inputSection)) {
                return existingSection; // Return the matching section
            }
        }
        return null; // No match found
    }
    //-----------------------------------------------------------------------
    // Append new information to an existing section

    public void appendToSection() {
        for (int i = 0; i < content.size(); i++) {
            if (content.get(i).equals("SECTION: " + section)) {
                int insertIndex = i + 1;
                while (insertIndex < content.size() && !content.get(insertIndex).startsWith("SECTION:")) {
                    insertIndex++;
                }
                content.add(insertIndex, "PROBLEM: " + problem);
                content.add(insertIndex + 1, "SOLUTION: " + solution);
                break;
            }
        }
    }
    //----------------------------------------------------------------------
    // Create a new section

    public void createNewSection() {
        System.out.println("Section does not exist. Creating a new section.");
        if (!content.isEmpty()) {
            content.add(""); // Add spacing between sections
        }
        content.add("SECTION: " + section);
        content.add("PROBLEM: " + problem);
        content.add("SOLUTION: " + solution);
    }
    //------------------------------------------------------------------------
    // Update and Delete Information

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
                if (deleteProblem(section, problem)) {
                    System.out.println("Problem and solution deleted successfully!");
                } else {
                    System.out.println("Problem not found. Deletion failed.");
                }
                break;
            case "update":
                System.out.print("Enter new Solution: ");
                String newSolution = scanner.nextLine();
                if (updateSolution(section, problem, newSolution)) {
                    System.out.println("Solution updated successfully!");
                } else {
                    System.out.println("Problem not found. Update failed.");
                }
                break;
            default:
                System.out.println("Invalid action. Please enter 'update' or 'delete'.");
                break;
        }
    }

    public boolean deleteProblem(String section, String problem) throws IOException {
        boolean isChanged = false;
        for (int i = 0; i < content.size(); i++) {
            if (isSimilar(content.get(i), "SECTION: " + section)) {
                for (int j = i + 1; j < content.size() && !content.get(j).startsWith("SECTION:"); j++) {
                    if (isSimilar(content.get(j), "PROBLEM: " + problem)) {
                        content.remove(j); // Remove problem
                        content.remove(j); // Remove solution
                        isChanged = true;
                        break;
                    }
                }
                break;
            }
        }
        if (isChanged) {
            fileHandler.writeData(content);
        }
        return isChanged;
    }

    public boolean updateSolution(String section, String problem, String newSolution) throws IOException {
        boolean isChanged = false;
        for (int i = 0; i < content.size(); i++) {
            if (isSimilar(content.get(i), "SECTION: " + section)) {
                for (int j = i + 1; j < content.size() && !content.get(j).startsWith("SECTION:"); j++) {
                    if (isSimilar(content.get(j), "PROBLEM: " + problem)) {
                        content.set(j + 1, "SOLUTION: " + newSolution);
                        isChanged = true;
                        break;
                    }
                }
                break;
            }
        }
        if (isChanged) {
            fileHandler.writeData(content);
        }
        return isChanged;
    }

    public boolean isSimilar(String text, String userInput) {
        text = text.toLowerCase().trim().replaceAll("\\s+", "");
        userInput = userInput.toLowerCase().trim().replaceAll("\\s+", "");

        // Calculate the minimum length of both strings
        int minLength = Math.min(text.length(), userInput.length());
        int maxLength = Math.max(text.length(), userInput.length());
        int matchCount = 0;

        // Compare each character up to the length of the shorter string
        for (int i = 0; i < minLength; i++) {
            if (text.charAt(i) == userInput.charAt(i)) {
                matchCount++;
            }
        }

        // Calculate similarity based on the number of matching characters and the maximum length
        double similarity = (double) matchCount / maxLength;

        // Return true if the similarity is above a threshold, say 60%
        return similarity > 0.5;
    }

    //------------------------------------------------------------------------------------
    // Search for information
    public void search(String keyword) throws IOException {
        String currentSection = null;
        String currentProblem = null;
        String currentSolution = null;
        boolean found = false;

        for (int i = 0; i < content.size(); i++) {
            String line = content.get(i);

            if (line.startsWith("SECTION:")) {
                currentSection = line.substring("SECTION:".length()).trim();
            } else if (line.startsWith("PROBLEM:")) {
                currentProblem = line.substring("PROBLEM:".length()).trim();
            } else if (line.startsWith("SOLUTION:")) {
                currentSolution = line.substring("SOLUTION:".length()).trim();

                if (containsKeyword(currentSection, keyword)
                        || containsKeyword(currentProblem, keyword)
                        || containsKeyword(currentSolution, keyword)) {
                    System.out.println("SECTION: " + highlightKeyword(currentSection, keyword));
                    System.out.println("PROBLEM: " + highlightKeyword(currentProblem, keyword));
                    System.out.println("SOLUTION: " + highlightKeyword(currentSolution, keyword));
                    System.out.println("-----------------------------------");
                    found = true;
                }
            }
        }

        if (!found) {
            System.out.println("No matching keyword found in the file.");
        }
    }

    // check if the keyword is in the text (case-insensitive)
    private boolean containsKeyword(String text, String keyword) {
        return text != null && text.toLowerCase().contains(keyword.toLowerCase());
    }

    //highlight the keyword in the text
    private String highlightKeyword(String text, String keyword) {
        if (text == null) {
            return "";
        }
        String lowerText = text.toLowerCase();
        String lowerKeyword = keyword.toLowerCase();

        StringBuilder highlightedText = new StringBuilder();
        int index = 0;

        while (index < text.length()) {
            int keywordStart = lowerText.indexOf(lowerKeyword, index);
            if (keywordStart == -1) {
                // Append the remaining part of the text
                highlightedText.append(text.substring(index));
                break;
            }

            // Append text before the keyword
            highlightedText.append(text.substring(index, keywordStart));
            // Append the highlighted keyword
            highlightedText.append(keyword.toUpperCase());
            // Move the index to after the keyword
            index = keywordStart + keyword.length();
        }

        return highlightedText.toString();
    }

    //------------------------------------------------------------------------------------
    // Print all information from the file
    public void printAllInformation() throws IOException {
        // Check if the file is empty
        if (content.isEmpty()) {
            System.out.println("The file is empty. No information available to display.");
        } else {
            // Print all lines if the file is not empty
            for (String line : content) {
                System.out.println(line);
            }
        }
    }
    //------------------------------------------------------------------------------------
    //Getter-Setter Methods

    public FileHandler getFileHandler() {
        return fileHandler;
    }

    public void setFileHandler(FileHandler fileHandler) {
        this.fileHandler = fileHandler;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public List<String> getContent() {
        return content;
    }

    public void setContent(List<String> lines) {
        this.content = lines;
    }

    public Scanner getScanner() {
        return scanner;
    }

    public void setScanner(Scanner scanner) {
        this.scanner = scanner;
    }
}
