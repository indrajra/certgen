package org.incredible.certificateGenerator;

import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;
import org.ekstep.QRCodeGenerationModel;
import org.ekstep.util.QRCodeImageGenerator;
import org.incredible.HTMLGenerator;
import org.incredible.HTMLTemplateProvider;
import org.incredible.certProcessor.CertModel;
import org.incredible.certProcessor.CertificateFactory;
import org.incredible.pojos.CertificateExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class CertificateGenerator {

    private CertificateFactory certificateFactory = new CertificateFactory();

    /**
     * to get all the properties from the application.properties
     */
    Properties properties = certificateFactory.readPropertiesFile();

    /**
     * context file url
     **/
    private final String context;

    private static Logger logger = LoggerFactory.getLogger(CertificateFactory.class);

    private Boolean isValid;

    private HTMLGenerator htmlGenerator = new HTMLGenerator();


    public CertificateGenerator(String context) {
        this.context = context;
    }

    public String createCertificate(CertModel certModel, HTMLTemplateProvider htmlTemplateProvider, String config) {

        CertificateExtension certificateExtension = certificateFactory.createCertificate(certModel, context);
        generateQRCodeForCertificate(certificateExtension);
        isValid = htmlTemplateProvider.checkHtmlTemplateIsValid(htmlTemplateProvider.getTemplateContent());
        if (isValid) {
            htmlGenerator.generateTemplate(certificateExtension, htmlTemplateProvider.getTemplateContent());
            return certificateExtension.getId().split("Certificate/")[1];
        } else return null;
    }


    private static void generateQRCodeForCertificate(CertificateExtension certificateExtension) {

        List<String> text = new ArrayList<>();
        List<String> data = new ArrayList<>();
        List<String> filename = new ArrayList<>();
        List<File> QrcodeList;
        text.add(certificateExtension.getRecipient().getName());
        data.add(certificateExtension.getBadge().getName());
        filename.add(certificateExtension.getId().split("Certificate/")[1]);
        QRCodeGenerationModel qrCodeGenerationModel = new QRCodeGenerationModel();
        qrCodeGenerationModel.setText(text);
        qrCodeGenerationModel.setFileName(filename);
        qrCodeGenerationModel.setData(data);
        QRCodeImageGenerator qrCodeImageGenerator = new QRCodeImageGenerator();

        try {
            QrcodeList = qrCodeImageGenerator.createQRImages(qrCodeGenerationModel, "container", "path");

        } catch (IOException | WriterException | FontFormatException | NotFoundException e) {
            logger.error("Exception while generating QRcode {}", e);
        }

    }
}
