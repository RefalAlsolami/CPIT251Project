
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {

    private String fileName = "data.txt";

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
        List<String> content = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            content.add(line);
        }
        reader.close();
        return content;
    }

    // Write all data to the file
    public void writeData(List<String> content) {
        try {
            FileWriter fileWriter = new FileWriter(fileName, false); // false to overwrite
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            for (String line : content) {
                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }

            bufferedWriter.close();
            fileWriter.close();
        } catch (IOException e) {
            System.err.println("Error writing to the file: " + e.getMessage());
        }
    }

}
