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
import org.incredible.pojos.ob.Assertion;
import org.incredible.pojos.ob.exeptions.InvalidDateFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CertificateGenerator {


    /**
     * context file url
     **/
    private final String context;

    private static Logger logger = LoggerFactory.getLogger(CertificateFactory.class);

    private Boolean isValid;

    private HashMap<String, String> properties;

    public CertificateGenerator(String context, HashMap<String, String> properties) {
        this.context = context;
        this.properties = properties;
    }

    private CertificateFactory certificateFactory = new CertificateFactory();


    public String createCertificate(CertModel certModel, HTMLTemplateProvider htmlTemplateProvider, String config, String signatureConfig) throws InvalidDateFormatException {

        CertificateExtension certificateExtension = certificateFactory.createCertificate(certModel, context, properties);
        generateQRCodeForCertificate(certificateExtension);
        isValid = htmlTemplateProvider.checkHtmlTemplateIsValid(htmlTemplateProvider.getTemplateContent());
        if (isValid) {
            HTMLGenerator htmlGenerator = new HTMLGenerator(htmlTemplateProvider.getTemplateContent());
            htmlGenerator.generateHTMLForSingleCertificate(certificateExtension);
            return certificateExtension.getId().split("Certificate/")[1];
        } else return null;
    }

    private void generateQRCodeForCertificate(Assertion assertion) {

        List<String> text = new ArrayList<>();
        List<String> data = new ArrayList<>();
        List<String> filename = new ArrayList<>();
        List<File> QrcodeList;
        //todo generate n digit code
        text.add("123456");
        data.add(assertion.getBadge().getName());
        filename.add(assertion.getId().split("Certificate/")[1]);
        QRCodeGenerationModel qrCodeGenerationModel = new QRCodeGenerationModel();
        qrCodeGenerationModel.setText(text);
        qrCodeGenerationModel.setFileName(filename);
        qrCodeGenerationModel.setData(data);
        try {
            QrcodeList = QRCodeImageGenerator.createQRImages(qrCodeGenerationModel, "container", "path");

        } catch (IOException | WriterException | FontFormatException | NotFoundException e) {
            logger.error("Exception while generating QRcode {}", e.getMessage());
        }

    }
}
