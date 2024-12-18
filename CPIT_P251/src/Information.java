
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Information {

    private String adminID;
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
    public void addInformation(String adminID) throws IOException {
        // Display existing sections
        List<String> sections = getSectionsAndDisplay();

        System.out.print("Enter Section: ");
        section = scanner.nextLine();
        System.out.print("Enter Problem: ");
        problem = scanner.nextLine();
        System.out.print("Enter Solution: ");
        solution = scanner.nextLine();;

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
        System.out.println("Information added successfully by Admin ID: " + adminID);

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
            // Check if the current line matches the specified section
            if (content.get(i).equals("SECTION: " + section)) {
                int insertIndex = i + 1;

                // Find the correct position (before the empty line or the next section)
                while (insertIndex < content.size()
                        && !content.get(insertIndex).startsWith("SECTION:")
                        && !content.get(insertIndex).trim().isEmpty()) {
                    insertIndex++;
                }

                // Add the problem and solution before the empty line
                content.add(insertIndex, "PROBLEM: " + problem);
                content.add(insertIndex + 1, "SOLUTION: " + solution);

                break; // Exit after insertion
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
    // Update Information
    public void updateSolution(String adminID) throws IOException {
        List<String> sections = getSectionsAndDisplay();
        System.out.print("Enter Section: ");
        section = scanner.nextLine();

        if (!sections.contains(section)) {
            System.out.println("Section does not exist. Please enter a valid section.");
            return;
        }

        System.out.print("Enter Problem to modify: ");
        String problemToUpdate = scanner.nextLine();

        System.out.print("Enter new Solution: ");
        String newSolution = scanner.nextLine();

        if (updateSolutionInContent(section, problemToUpdate, newSolution)) {
            System.out.println("Solution updated successfully by Admin ID: " + adminID);
        } else {
            System.out.println("Problem not found. Update failed.");
        }
    }

    //------------------------------------------------------------------------------------
    public boolean updateSolutionInContent(String section, String problem, String newSolution) throws IOException {
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

    //------------------------------------------------------------------------------------
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

        // Return true if the similarity is above a threshold 70%
        return similarity > 0.7;
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

    //------------------------------------------------------------------------------------
    // check if the keyword is in the text 
    private boolean containsKeyword(String text, String keyword) {
        return text != null && text.toLowerCase().contains(keyword.toLowerCase());
    }

    //------------------------------------------------------------------------------------
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

            highlightedText.append(text.substring(index, keywordStart));
            // Append the highlighted keyword
            highlightedText.append(keyword.toUpperCase());
            // Move the index to after the keyword
            index = keywordStart + keyword.length();
        }

        return highlightedText.toString();
    }

    //------------------------------------------------------------------------------------
    // Display all information from the file
    public void displayAllInformation() throws IOException {
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
    // Getter and Setter
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

    public void setContent(List<String> content) {
        this.content = content;
    }

    public Scanner getScanner() {
        return scanner;
    }

    public void setScanner(Scanner scanner) {
        this.scanner = scanner;
    }

}
