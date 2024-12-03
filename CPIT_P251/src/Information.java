
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
//--------------------------------------------------------------------
    public void collectUserInput() {
    System.out.print("Enter Section: ");
    section = scanner.nextLine();
    System.out.print("Enter Problem: ");
    problem = scanner.nextLine();
    System.out.print("Enter Solution: ");
    solution = scanner.nextLine();
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
            if (isSimilar(currentProblem, problem)) {
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
            if (isSimilar(currentProblem, problem)) {
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

    private boolean isSimilar(String text1, String text2) {
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

        for (int i = 0; i < content.size(); i++) {
            String line = content.get(i);

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
