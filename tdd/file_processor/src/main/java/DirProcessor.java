import com.google.common.io.Files;

import java.io.*;

class DirProcessor {
    private FileProcessor fileProcessor;
    private static final String TEXT_EXTENSION = "txt";
    
    void setFileProcessor(FileProcessor fileProcessor) {
    	this.fileProcessor = fileProcessor;
    }

    int countCharacters(String pathToDir) throws IOException {
        File folder = new File(pathToDir);
        File[] files = folder.listFiles();

        int count=0;
        
        if(files == null) {
            return count;
        }

        for(File file : files) {
        	if(!Files.getFileExtension(file.getName()).equals(TEXT_EXTENSION)) {
        		continue;
        	}
            count += fileProcessor.countCharacters(file.getPath());
        }
        return count;
    }
}
