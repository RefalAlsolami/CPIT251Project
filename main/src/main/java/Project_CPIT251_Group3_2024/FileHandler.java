
package Project_CPIT251_Group3_2024;

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

     // Read data from the file
    public List<String> readData() throws IOException {
        List<String> lines = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line;
        while ((line = reader.readLine()) != null) {
            lines.add(line);
        }
        reader.close();
        return lines;
    }

    // Write data to the file

}
