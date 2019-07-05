package org.incredible.csvProcessor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.ekstep.QRCodeGenerationModel;
import org.ekstep.util.QRCodeImageGenerator;
import org.incredible.certProcessor.CertModel;
import org.incredible.certProcessor.CertificateFactory;
import org.incredible.pojos.CertificateExtension;
import org.incredible.pojos.ob.Assertion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class Main {

    /** to get each row in csv file **/


    /**
     * List to CertModel
     **/

    private static ArrayList<CertModel> certModelsList = new ArrayList();

    private static Logger logger = LoggerFactory.getLogger(Main.class);

    static List<File> QrcodeList;

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


    private static final String DOMAIN = "http://localhost:8080/_schemas";
    private static final String CONTEXT_FILE_NAME = "context.json";
    private static final String HTML_TEMPLATE_NAME = "template.html";

    private static String context;


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
        initContext();
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
    private static void generateQRCodeForCertificate(CertificateExtension certificateExtension) {

        List<String> text = new ArrayList<>();
        List<String> data = new ArrayList<>();
        List<String> filename = new ArrayList<>();

        text.add(certificateExtension.getRecipient().getName());
        data.add(certificateExtension.getBadge().getName());
        filename.add(certificateExtension.getId());

        QRCodeGenerationModel qrCodeGenerationModel = new QRCodeGenerationModel();
        qrCodeGenerationModel.setText(text);
        qrCodeGenerationModel.setFileName(filename);
        qrCodeGenerationModel.setData(data);

        QRCodeImageGenerator qrCodeImageGenerator = new QRCodeImageGenerator();

        try {
            QrcodeList = qrCodeImageGenerator.createQRImages(qrCodeGenerationModel, "container", "path");

        } catch (IOException | WriterException | FontFormatException | NotFoundException e) {
            logger.info("Exception while generating QRcode {}", e);
        }

    }

    /**
     * generate Html Template for certificate
     **/
    private static void generateHtmlTemplateForCertificate(Assertion assertion) {
        File htmlTemplateFile = new File(getPath(HTML_TEMPLATE_NAME));
        String htmlString = null;
        File file = new File(assertion.getId() + ".png");
        String path = file.getPath();

        try {
            htmlString = FileUtils.readFileToString(htmlTemplateFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        htmlString = htmlString.replace("$title", "certificate");
        htmlString = htmlString.replace("$recipient", assertion.getRecipient().getName());
        htmlString = htmlString.replace("$img", path);
        htmlString = htmlString.replace("$course", assertion.getBadge().getName());
        File newHtmlFile = new File(assertion.getId() + ".html");
        try {
            FileUtils.writeStringToFile(newHtmlFile, htmlString);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void initContext() {
        try {
            ClassLoader classLoader = Main.class.getClassLoader();

            File file = new File(classLoader.getResource(CONTEXT_FILE_NAME).getFile());
            if (file == null) {
                throw new IOException("Context file not found");
            }
            context = DOMAIN + "/" + CONTEXT_FILE_NAME;
            logger.info("Context file Found : {} ", file.exists());
        } catch (IOException e) {

        }

    }
}