import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.mockito.Mockito.*;

class DirProcessorTests {
	
	private DirProcessor dirProcessor = new DirProcessor();
    private FileProcessor mockedFileProcessor = mock(FileProcessor.class);
    private static final String DIR_PROCESSOR_RESOURCES = "target\\test-classes\\DirProcessorResources\\";

    @Test
    void testEmptyDirectory() throws IOException {
        check(0, "fileDir1");
        check(0, "fileDirWithSubDir\\subDir");
        
        verify(mockedFileProcessor, never()).countCharacters(anyString());
    }
    
    @Test
    void testDirectoryWithOneFile() throws IOException {
        prepareMock(3, "fileDir2\\file1.txt");
        
    	check(3, "fileDir2");
    	
    	verify(mockedFileProcessor, times(1)).countCharacters(anyString());
    }
    
    @Test
    void testDirectoryWithMultipleFiles() throws IOException {
        prepareMock(6, "fileDir3\\file2.txt");
        prepareMock(4, "fileDir3\\file3.txt");
        
    	check(10, "fileDir3");
    	
    	verify(mockedFileProcessor, times(2)).countCharacters(anyString());
    }
    

    @Test
    void testSubDirectory() throws IOException {
        prepareMock(5, "fileDirWithSubDir\\file1.txt");
        
        check(5, "fileDirWithSubDir");

        verify(mockedFileProcessor, times(1)).countCharacters(anyString());
    }

    @Test
    void testDifferentFileExtensions() throws IOException {
        prepareMock(3, "fileDir5\\file1.txt");
        
        check(0, "fileDir4");
        check(3, "fileDir5");
        
        verify(mockedFileProcessor, never()).countCharacters("htmlFile.html");
        verify(mockedFileProcessor, never()).countCharacters("sheet.css");
    }

    private void check(int expected, String pathToDirectory) throws IOException {
        dirProcessor.setFileProcessor(mockedFileProcessor);
        assertEquals(expected, dirProcessor.countCharacters(DIR_PROCESSOR_RESOURCES + pathToDirectory));
    }
    
    private void prepareMock(int expected, String pathToDirectory) throws IOException {
    	when(mockedFileProcessor.countCharacters(DIR_PROCESSOR_RESOURCES + pathToDirectory)).thenReturn(expected);
    }
}
