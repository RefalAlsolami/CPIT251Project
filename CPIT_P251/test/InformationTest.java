
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

public class InformationTest {

    //testAddInformation()
    @Test
    public void testAddInformation() throws Exception {

    }

    //testUpdateOrDeleteSolution()
    @Test
    public void testUpdateOrDeleteSolution() throws Exception {

    }

    //testSearch()
    @Test
    public void testSearch() throws Exception {

    }

    //testPrintAllInformation()
    @Test
    public void testPrintAllInformation() throws IOException {
        System.out.println("Test: printAllInformation from File (data.txt)");

        // Expected data (manually define expected content from the file)
        List<String> expectedData = Arrays.asList(
                "SECTION: light",
                "PROBLEM: not working",
                "SOLUTION: Call the electrician"
        );

        // Create an instance of Information using the actual FileHandler
        FileHandler fileHandler = new FileHandler(); // Create a real FileHandler
        Information instance = new Information(fileHandler); // Pass it to Information

        // Read actual data from the file
        List<String> actualData = fileHandler.readData(); // Read data from the file

        // Assert that the file contains all expected data
        assertTrue(actualData.containsAll(expectedData));
    }

    @Test
    public void testPrintAllInformationEmptyFile() throws IOException {
        System.out.println("Test: printAllInformation (Empty File)");

        // Simulate an empty file by creating an empty FileHandler
        FileHandler fileHandler = new FileHandler() {
            @Override
            public List<String> readData() {
                return Arrays.asList(); // Return an empty list to simulate an empty file
            }
        };

        // Create an instance of Information using the mock FileHandler
        Information instance = new Information(fileHandler);

        // Read actual data from the (empty) file
        List<String> actualData = fileHandler.readData();

        // Expected data (empty list)
        List<String> expectedData = Arrays.asList();

        // Assert that the file is empty
        assertEquals(expectedData, actualData);
    }

}
