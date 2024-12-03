
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

class TestFileHandler extends FileHandler {

    List<String> testFile = new ArrayList<>(); // Simulate the file content

    @Override
    public List<String> readData() {
        return new ArrayList<>(testFile); // Return a copy to simulate reading from a file
    }

    @Override
    public void writeData(List<String> lines) {
        testFile.clear(); // Clear the simulated file
        testFile.addAll(lines); // Add new lines to the simulated file
    }
}
//------------------------------------------------------------------------------------
public class InformationTest {

    FileHandler fileHandler;
    Information information;

    List<String> initialData = Arrays.asList(
            "SECTION: lamp",
            "PROBLEM: not working",
            "SOLUTION: Call the electrician",
            "SECTION: printer",
            "PROBLEM: paper jam",
            "SOLUTION: Clear the paper path"
    );

    @Before
    public void setUp() throws IOException {
        fileHandler = new TestFileHandler(); // Use the mock file handler
        information = new Information(fileHandler); // Pass it to the Information class
    }
//-------------------------------------------------------------
    @Test
    public void testGetSectionsAndDisplay() throws IOException {
        System.out.println("Test: getSectionsAndDisplay");

        fileHandler.writeData(initialData);

        // Reload lines in the existing Information instance
        information.setContent(fileHandler.readData());

        // Test the method
        List<String> sections = information.getSectionsAndDisplay();
        List<String> expectedSections = Arrays.asList("lamp", "printer");
        assertEquals(expectedSections, sections);
    }
//--------------------------------------------------------------
    @Test
    public void testAppendToSection() throws IOException {
        System.out.println("Test: appendToSection");
        fileHandler.writeData(initialData);

        information.setContent(fileHandler.readData());
        information.setSection("lamp"); // Existing section
        information.setProblem("flickering");
        information.setSolution("Check and tighten the bulb");

        // Call appendToSection
        information.appendToSection();

        // Expected data after appending
        List<String> expectedData = Arrays.asList(
                "SECTION: lamp",
                "PROBLEM: not working",
                "SOLUTION: Call the electrician",
                "PROBLEM: flickering",
                "SOLUTION: Check and tighten the bulb",
                "SECTION: printer",
                "PROBLEM: paper jam",
                "SOLUTION: Clear the paper path"
        );

        // Verify that the lines now match the expected result
        List<String> actualData = information.getContent(); // Use the `lines` field directly
        assertEquals(expectedData, actualData);
    }

//-------------------------------------------------------------------------------
    @Test
    public void testCreateNewSection() throws IOException {
        System.out.println("Test: createNewSection");

        information.setContent(fileHandler.readData());
        information.setSection("prin");
        information.setProblem("paper jam");
        information.setSolution("Clear the paper path");
        information.createNewSection();
        List<String> expectedLines = Arrays.asList(
                "SECTION: prin",
                "PROBLEM: paper jam",
                "SOLUTION: Clear the paper path"
        );
        List<String> actualLines = information.getContent();
        assertEquals(expectedLines, actualLines);
    }

    //--------------------------------------------------------------
    @Test
    public void testFindMatchingSectionNoMatch() {
        List<String> sections = Arrays.asList("lamp", "printer", "router");
        String inputSection = "monitor";

        String matchedSection = information.findMatchingSection(sections, inputSection);
        assertNull(matchedSection); // Ensure no match is found
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
//------------------------------------------------------------------------------------
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
    @Test
    public void testPrintAllInformation() throws IOException {
        System.out.println("Test: printAllInformation");

        // Step 1: Write data to the mock file handler
        List<String> fileData = Arrays.asList(
                "SECTION: PC",
                "PROBLEM: not working",
                "SOLUTION: Restart the pc"
        );
        fileHandler.writeData(fileData);

        // Step 2: Call the method to test
        information.printAllInformation();

        // Step 3: Verify that the data in the file matches the expected data
        assertEquals("printAllInformation executed successfully and the file content"
                + " matches the expected data.", fileData, fileHandler.readData());
    }
//------------------------------------------------------------------------------------
    @Test
    public void testPrintAllInformationEmptyFile() throws IOException {
        System.out.println("Test: print All Information From Empty File");

        // Step 1: Write an empty list to the mock file handler to simulate an empty file
        fileHandler.writeData(new ArrayList<>());

        // Step 2: Call the method to test
        information.printAllInformation();

        // Step 3: Verify that the file is indeed empty
        List<String> actualData = fileHandler.readData();
        assertTrue("File should be empty", actualData.isEmpty());
    }
//-----------------------------------------------------------------------------------------
    @Test
    public void testPrintAllInformationLargeFile() throws IOException {
        System.out.println("Test: print All Information with Large Data");

        // Generate a large dataset
        List<String> largeFileData = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            largeFileData.add("SECTION: Section " + i);
            largeFileData.add("PROBLEM: Issue " + i);
            largeFileData.add("SOLUTION: Solution " + i);
        }
        fileHandler.writeData(largeFileData);

        // Call the method to test
        information.printAllInformation();

        // Verify that the data in the file matches the large dataset
        assertEquals("The file content should match the large dataset.", largeFileData, fileHandler.readData());
    }

}
