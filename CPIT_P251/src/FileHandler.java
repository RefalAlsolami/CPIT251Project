
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {

    private String fileName = "data.txt";

    // Constructor
    public FileHandler() {
        try {
            File file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            System.err.println("Error initializing the file: " + e.getMessage());
        }
    }

    // Read all data from the file method
    public List<String> readData() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        List<String> lines = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            lines.add(line);
        }
        reader.close();
        return lines;
    }

    // Write all data to the file (overwrites the file) method
    public void writeData(List<String> lines) {
        try {
            FileWriter fileWriter = new FileWriter(fileName, false); // false to overwrite
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            for (String line : lines) {
                bufferedWriter.write(line);
                bufferedWriter.newLine(); // New line for each entry
            }

            bufferedWriter.close();
            fileWriter.close();
        } catch (IOException e) {
            System.err.println("Error writing to the file: " + e.getMessage());
        }
    }

    // Append new data to the file
}
