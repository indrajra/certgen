
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import org.apache.log4j.PropertyConfigurator;
import org.incredible.pojos.CertificateExtension;
import org.incredible.pojos.ob.Assertion;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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

    static String fileName = "/Users/aishwarya/workspace/certgen/certgen/src/main/resources/ecredsInputCsv-Sheet1.csv";


    /**
     * csv file name
     **/

    static String csvFileName = "/Users/aishwarya/workspace/certgen/certgen/src/main/resources/InputModelMapper.json";


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


    private static final String DOMAIN = "http://localhost:8080";
    private static final String CONTEXT_FILE_NAME = "v1/context.json";

    private static String context;


    /**
     * read csv file
     **/
    private static void readMapperFile() {
        try {


            csvProperties = mapper.readValue(new File(csvFileName), HashMap.class);
        } catch (IOException io) {
            System.out.println(io);
            logger.error("Input model  mapper file does not exits {}, {}", csvFileName, io.getMessage());
        }


    }

    public static void main(String[] args) {

        PropertyConfigurator.configure("src/main/resources/log4j.properties");

        readMapperFile();
        boolean isFileExits = csvReader.isExists(fileName);
        if (isFileExits) {
            try {
                CSVParser csvParser = csvReader.readCsvFileRows(fileName);
                setCertModelsList(csvParser);

            } catch (IOException io) {
                logger.error("CSV Parsing exception {}, {}", io.getMessage(), io.getStackTrace());
            }
        } else {
            logger.error("Input CSV file not found {}", fileName);
        }


        /** iterate each inputmodel to generate certificate **/

        for (int row = 0; row < certModelsList.size(); row++) {
            // TODO - Generating certificate for <recipient> and index
            CertificateExtension certificate = certificateFactory.createCertificate(certModelsList.get(row), context);
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
