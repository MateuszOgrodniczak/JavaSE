import java.io.*;

class FileProcessor {
    private static final int STREAM_END_SYMBOL = -1;

    int countCharacters(String path) throws IOException {
        int count = 0;

        InputStream is = new FileInputStream(path);
        Reader reader = new InputStreamReader(is);
        int input;

        while((input = reader.read()) != STREAM_END_SYMBOL) {
            if(Character.isWhitespace((char) input)) {
                continue;
            }
            count++;
        }

        reader.close();
        return count;
    }
}
