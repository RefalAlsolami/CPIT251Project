
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

    // Read all data from the file method
    
    // Write all data to the file (overwrites the file) method
    
    // Append new data to the file
}
