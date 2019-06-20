import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.log4j.BasicConfigurator;
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

    static String fileName = "/Users/aishwarya/workspace/certgen/certgen/src/main/resources/ecreds input csv - Sheet1.csv";


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

    /**
     * read csv file
     **/
    private static void readMapperFile() {

        try {
            csvProperties = mapper.readValue(new File(csvFileName), HashMap.class);
        } catch (IOException io) {
            System.out.println(io);
        }


    }

    public static void main(String[] args) {
        BasicConfigurator.configure();
        readMapperFile();
        boolean isFileExits = csvReader.isExists(fileName);
        if (isFileExits) {
            try {
                CSVParser csvParser = csvReader.readCsvFileRows(fileName);
                setCertModelsList(csvParser);

            } catch (IOException io) {
                logger.error("File Not found" + io);
                // TODO - verify your file name and try again
            }
        } else {
            logger.error("File not found with this filename");
        }


        /** iterate each inputmodel to generate certificate **/

        for (int row = 0; row < certModelsList.size(); row++) {
            // TODO - Generating certificate for <recipient> and index
            CertificateExtension certificate = certificateFactory.createCertificate(certModelsList.get(row));
            generateQRCodeForCertificate(certificate);
            generateHtmlTemplateForCertificate(certificate);
            System.out.println(certModelsList.get(row));
        }

    }

    /**
     * set each field to inputmodel object
     **/

    public static CertModel getInputModel(CSVRecord csvRecord) {
        CertModelFactory certModelFactory = new CertModelFactory(csvProperties);
        CertModel model = certModelFactory.create(csvRecord);
        logger.info("csv row" + csvRecord + "Certificate model of each row" + model.toString());
        // TODO: print csv (input) and print inputModel (derivation from the input)
        return model;
    }


    private static void setCertModelsList(CSVParser csvParser) {
        for (CSVRecord csvRecord : csvParser) {
            CertModel model = getInputModel(csvRecord);
            if (null == model) {
                logger.error("Cannot generate certificate for csvRecord please check give proper input");
                // TODO: Can't generate certificate for csvRecord
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
