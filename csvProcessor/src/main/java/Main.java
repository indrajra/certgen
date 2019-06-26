import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.incredible.pojos.CertificateExtension;
import org.incredible.pojos.ob.Assertion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;


public class Main {

    /** to get each row in csv file **/


    /**
     * List to CertModel
     **/

    private static ArrayList<CertModel> certModelsList = new ArrayList();

    private static Logger logger = LoggerFactory.getLogger(Main.class);

    /**
     * to get the file name
     **/

    static String csvFileName = "input.csv";


    /**
     * csv file name
     **/

    static String modelFileName = "CertModelMapper.json";


    static CertificateFactory certificateFactory = new CertificateFactory();

    static CSVReader csvReader = new CSVReader();


    /**
     * mapper to read cert json mapper file
     **/

    private static ObjectMapper mapper = new ObjectMapper();

    /**
     * to store catv column name
     **/

    private static HashMap<String, String> csvProperties;


    private static String readFromFile(String file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        StringBuilder sb = new StringBuilder();
        try {
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            return "";
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        return sb.toString();
    }

    private static String getPath(String file) {
        String result = null;
        try {
            result = Main.class.getClassLoader().getResource(file).getFile();
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            return result;
        }
    }
    /**
     * read csv file
     **/
    private static void readFile(String fileName) {
        try {
            String csvContent = new String(Files.readAllBytes(new File(getPath(fileName)).toPath()));
            csvProperties = mapper.readValue(csvContent, HashMap.class);
        } catch (IOException io) {
            logger.error("Input model  mapper file does not exits {}, {}", modelFileName, io.getMessage());
        }
    }

    private static void readCSV(String filename) {
        boolean isFileExits = csvReader.isExists(filename);
        if (isFileExits) {
            try {
                CSVParser csvParser = csvReader.readCsvFileRows(filename);
                setCertModelsList(csvParser);

            } catch (IOException io) {
                logger.error("CSV Parsing exception {}, {}", io.getMessage(), io.getStackTrace());
            }
        } else {
            logger.error("Input CSV file not found {}", csvFileName);
        }

        logger.info("Finished reading the csv file");
    }

    public static void main(String[] args) {
        readFile(modelFileName);
        readCSV(getPath(csvFileName));

        /** iterate each inputmodel to generate certificate **/

        for (int row = 0; row < certModelsList.size(); row++) {
            // TODO - Generating certificate for <recipient> and index
            CertificateExtension certificate = certificateFactory.createCertificate(certModelsList.get(row));
            generateQRCodeForCertificate(certificate);
            generateHtmlTemplateForCertificate(certificate);
        }

    }

    /**
     * set each field to inputmodel object
     **/

    public static CertModel getInputModel(CSVRecord csvRecord) {
        CertModelFactory certModelFactory = new CertModelFactory(csvProperties);
        CertModel model = certModelFactory.create(csvRecord);
        logger.info("csv row => {}", csvRecord);
        logger.info("Model created is => {}", model.toString());
        return model;
    }


    private static void setCertModelsList(CSVParser csvParser) {
        for (CSVRecord csvRecord : csvParser) {
            CertModel model = getInputModel(csvRecord);
            if (null == model) {
                logger.error("Cannot generate certificate for row {}. Invalid input", csvRecord.getRecordNumber());
            } else {
                certModelsList.add(model);
            }
        }
    }


    /**
     * to generateQRCode for certificate
     **/
    private static void generateQRCodeForCertificate(Assertion assertion) {

    }

    /**
     * generate Html Template for certificate
     **/
    private static void generateHtmlTemplateForCertificate(Assertion assertion) {

    }


}
