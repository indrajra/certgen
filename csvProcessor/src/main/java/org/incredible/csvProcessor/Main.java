package org.incredible.csvProcessor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.ekstep.QRCodeGenerationModel;
import org.ekstep.util.QRCodeImageGenerator;

import org.incredible.HTMLGenerator;
import org.incredible.HTMLTemplateFile;
import org.incredible.certProcessor.CertModel;
import org.incredible.certProcessor.CertificateFactory;
import org.incredible.pojos.CertificateExtension;
import org.incredible.pojos.ob.Assertion;

import org.incredible.pojos.ob.exeptions.InvalidDateFormatException;
import org.incredible.utils.KeyGenerator;
import org.incredible.utils.StorageParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.List;


public class Main {

    /** to get each row in csv file **/


    /**
     * List to CertModel
     **/

    private static ArrayList<CertModel> certModelsList = new ArrayList();

    private static Logger logger = LoggerFactory.getLogger(Main.class);

    private static List<File> QrcodeList;

    /**
     * to get the file name
     **/

    private static String csvFileName = "input.csv";


    /**
     * csv file name
     **/

    private static String modelFileName = "CertModelMapper.json";

    private static final String templateName = "template.html";


    private static CertificateFactory certificateFactory = new CertificateFactory();

    private static CSVReader csvReader = new CSVReader();

    private static Properties properties = certificateFactory.readPropertiesFile();


    /**
     * mapper to read cert json mapper file
     **/

    private static ObjectMapper mapper = new ObjectMapper();

    /**
     * to store catv column name
     **/

    private static HashMap<String, String> csvProperties;


    private static final String domain = properties.getProperty("CONTEXT");
    private static final String contextFileName = "context.json";

    private static String context;

    private static final String WORK_DIR = "./";

    /**
     * The algorithm to use
     */
    private static final String RSA_ALGO = "RSA";

    /**
     * The public key file name
     */
    private static final String PUBLIC_KEY_FILENAME = WORK_DIR + "public.pub";

    /**
     * The private key file name
     */
    private static final String PRIVATE_KEY_FILENAME = WORK_DIR + "private.key";

    /**
     * The public key pair
     */
    private static KeyPair keyPair;


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
        initializeKeys();
        readCSV(getPath(csvFileName));
        initContext();
        /** iterate each inputmodel to generate certificate **/

        for (int row = 0; row < certModelsList.size(); row++) {
            try {
                CertificateExtension certificate = certificateFactory.createCertificate(certModelsList.get(row), context);
//                File file = new File(certificate.getId().split("Certificate/")[1] + ".json");
//                mapper.writeValue(file, certificate);
//                String url = uploadFileToCloud(file);
                generateQRCodeForCertificate(certificate, certificate.getId() + ".json");
                generateHtmlTemplateForCertificate(certificate);

            } catch (InvalidDateFormatException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
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
    private static void generateQRCodeForCertificate(CertificateExtension certificateExtension, String url) {

        List<String> text = new ArrayList<>();
        List<String> data = new ArrayList<>();
        List<String> filename = new ArrayList<>();
        text.add("123456");
        data.add(url);
        filename.add(certificateExtension.getId().split("Certificate/")[1]);
        QRCodeGenerationModel qrCodeGenerationModel = new QRCodeGenerationModel();
        qrCodeGenerationModel.setText(text);
        qrCodeGenerationModel.setFileName(filename);
        qrCodeGenerationModel.setData(data);
        QRCodeImageGenerator qrCodeImageGenerator = new QRCodeImageGenerator();

        try {
            QrcodeList = qrCodeImageGenerator.createQRImages(qrCodeGenerationModel, "container", "path");

        } catch (IOException | WriterException | FontFormatException | NotFoundException e) {
            logger.info("Exception while generating QRcode {}", e.getMessage());
        }

    }

    /**
     * generate Html Template for certificate
     **/
    private static void generateHtmlTemplateForCertificate(Assertion assertion) throws Exception {
        HTMLGenerator htmlTemplateGenerator = new HTMLGenerator();
        HTMLTemplateFile htmlTemplateFile = new HTMLTemplateFile(templateName);
        Boolean valid = htmlTemplateFile.checkHtmlTemplateIsValid(htmlTemplateFile.getTemplateContent());
        if (valid) {
            htmlTemplateGenerator.generateHTML(assertion, htmlTemplateFile.getTemplateContent());
//            File file = new File(assertion.getId().split("Certificate/")[1] + ".html");
//            uploadFileToCloud(file);
        } else throw new Exception("HTML template is not valid");

    }

    private static void initContext() {
        try {
            ClassLoader classLoader = Main.class.getClassLoader();

            File file = new File(classLoader.getResource(contextFileName).getFile());
            if (file == null) {
                throw new IOException("Context file not found ");
            }
            context = domain + "/" + contextFileName;
            logger.info("Context file Found : {} ", file.exists());
        } catch (IOException e) {

        }

    }


    /**
     * to upload file to cloud
     *
     * @param file file to be uploaded
     * @return url
     */

    private static String uploadFileToCloud(File file) {
        StorageParams storageParams = new StorageParams();
        String url = storageParams.upload(properties.getProperty("CONTAINER_NAME"), "", file, false);
        return url;

    }


    private static KeyPair generateKeys() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = new KeyGenerator(RSA_ALGO, 2048);
        keyPair = keyGenerator.get();
        System.out.println(keyPair.getPublic().toString());
        return keyPair;
    }

    private String getPublicKey() throws IOException {
        return new KeyWriter().getPublicKey(keyPair.getPublic());
    }

    private static void initializeKeys() {

        try {
            keyPair = KeyLoader.load(RSA_ALGO, PUBLIC_KEY_FILENAME, PRIVATE_KEY_FILENAME);
            if (keyPair.getPrivate() == null ||
                    keyPair.getPublic() == null) {
                // Generate new key pairs
                keyPair = generateKeys();
                // Write it in current directory for next time
                new KeyWriter().write(keyPair, WORK_DIR);
            }
        } catch (NoSuchAlgorithmException algoException) {
            logger.info("No such algorithm. Refer https://docs.oracle.com/javase/8/docs/technotes/guides/security/StandardNames.html, {}", algoException.getMessage());
        } catch (IOException ioException) {
            logger.info(ioException.getCause() + "message : " + ioException.getMessage());
        }
    }

}