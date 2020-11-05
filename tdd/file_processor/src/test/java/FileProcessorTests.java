import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FileProcessorTests {

    private FileProcessor fileProcessor = new FileProcessor();
    private static final String FILE_PROCESSOR_RESOURCES = "target/test-classes/FileProcessorResources/";

    @Test
    void testEmptyFile() throws IOException {
        check(0, "file0.txt");
    }

    @Test
    void testSimpleCases() throws IOException {
        check(1, "file1.txt");
        check(7, "file2.txt");
    }

    @Test
    void testWhiteCharacters() throws IOException {
        check(7, "file3.txt");
        check(12, "file3_1.txt");
        check(20, "file4.txt");
    }

    @Test
    void testWrongPath() {
        checkThrows("wrongPathToFile.txt");
        checkThrows("file5.txt");
    }

    private void check(int characters, String pathToFile) throws IOException {
        assertEquals(characters, fileProcessor.countCharacters(FILE_PROCESSOR_RESOURCES + pathToFile));
    }

    private void checkThrows(String pathToFile) {
        assertThrows(FileNotFoundException.class, () -> fileProcessor.countCharacters(FILE_PROCESSOR_RESOURCES + pathToFile));
    }
}
