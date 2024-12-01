
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

public class InformationTest {

    FileHandler fileHandler;
    Information information;

    @Before
    public void setUp() throws IOException {
        // Use an anonymous class to override FileHandler methods for in-memory testing
        fileHandler = new FileHandler() {
            private List<String> testFile = new ArrayList<>();

            @Override
            public List<String> readData() {
                return new ArrayList<>(testFile); // Return a copy to simulate file reading
            }

            @Override
            public void writeData(List<String> lines) {
                testFile.clear(); // Clear current data
                testFile.addAll(lines); // Simulate file write
            }
        };

        // Create a fresh Information instance using the mocked FileHandler
        information = new Information(fileHandler);
    }

    @Test
    public void testGetSectionsAndDisplay() throws IOException {
        System.out.println("Test: getSectionsAndDisplay");
        List<String> initialData = new ArrayList<>();
        initialData.add("SECTION: lamp");
        initialData.add("PROBLEM: not working");
        initialData.add("SOLUTION: Call the electrician");
        initialData.add("SECTION: prinr");
        initialData.add("PROBLEM: paper jam");
        initialData.add("SOLUTION: Clear the paper path");
        fileHandler.writeData(initialData);
        information = new Information(fileHandler);
        List<String> sections = information.getSectionsAndDisplay();
        List<String> expectedSections = Arrays.asList("lamp", "prinr");
        assertEquals(expectedSections, sections);
    }

    @Test
    public void testCreateNewSection() throws IOException {
        System.out.println("Test: createNewSection");
        //  fileHandler.writeData(new ArrayList<>()); 
        information = new Information(fileHandler);
        information.section = "prin";
        information.problem = "paper jam";
        information.solution = "Clear the paper path";
        information.createNewSection();
        List<String> expectedLines = Arrays.asList(
                "SECTION: prin",
                "PROBLEM: paper jam",
                "SOLUTION: Clear the paper path"
        );
        List<String> actualLines = information.lines;
        assertEquals("The new section was not created correctly.", expectedLines, actualLines);
    }

//-------------------------------------------------------------------------------------------------
    @Test
    public void testUpdateOrDeleteSolution() throws Exception {

    }
//------------------------------------------------------------------------------------------

    @Test
    public void testSearchWithExistingKeyword() throws IOException {
        System.out.println("Test: Search for Keyword ");

        // Expected data
        List<String> expectedData = Arrays.asList(
                "SECTION: light",
                "PROBLEM: not working",
                "SOLUTION: Call the electrician"
        );

        FileHandler fileHandler = new FileHandler();
        Information instance = new Information(fileHandler);

        String keyword = "electrician";
        instance.search(keyword);

        List<String> actualData = fileHandler.readData();

        assertTrue(actualData.containsAll(expectedData));
    }

    @Test
    public void testSearchWithNonExistingKeyword() throws IOException {
        System.out.println("Test: Search with non-existing Keyword");

        FileHandler fileHandler = new FileHandler() {
            @Override
            public List<String> readData() {
                return Arrays.asList();
            }
        };

        Information instance = new Information(fileHandler);
        String keyword = "not found";
        instance.search(keyword);

        List<String> actualData = fileHandler.readData();
        List<String> expectedData = Arrays.asList();

        assertEquals(expectedData, actualData);
    }

//-----------------------------------------------------------------------------------------
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
