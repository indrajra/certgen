import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CSVReader {

    public CSVParser readCsvFileRows(String filename) throws IOException {
        System.out.println(filename);
            Reader reader = Files.newBufferedReader(Paths.get(filename));
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withIgnoreHeaderCase()
                    .withTrim().withNullString(""));

          return csvParser;
    }

    public boolean isExists(String filename) {
        File file = new File(filename);
        boolean exists = file.exists();
        return exists;
    }
}

