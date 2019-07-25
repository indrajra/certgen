package org.incredible.certProcessor;

import org.incredible.builders.*;
import org.incredible.pojos.CertificateExtension;
import org.incredible.pojos.RankAssessment;
import org.incredible.pojos.ob.Criteria;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.Properties;
import java.util.UUID;

import org.incredible.pojos.ob.VerificationObject;
import org.incredible.pojos.ob.exeptions.InvalidDateFormatException;
import org.incredible.utils.SignatureHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CertificateFactory {

    private static String uuid;

    private static Logger logger = LoggerFactory.getLogger(CertificateFactory.class);

    final String resourceName = "application.properties";

    private static SignatureHelper signatureHelper;

    public CertificateExtension createCertificate(CertModel certModel, String context, HashMap<String, String> properties) throws InvalidDateFormatException {

        uuid = properties.get("DOMAIN") + UUID.randomUUID().toString();

        CertificateExtensionBuilder certificateExtensionBuilder = new CertificateExtensionBuilder(context);
        CompositeIdentityObjectBuilder compositeIdentityObjectBuilder = new CompositeIdentityObjectBuilder(context);
        BadgeClassBuilder badgeClassBuilder = new BadgeClassBuilder(context);
        AssessedEvidenceBuilder assessedEvidenceBuilder = new AssessedEvidenceBuilder(properties.get("AssessedDomain"));
        IssuerBuilder issuerBuilder = new IssuerBuilder(context);
        SignatureBuilder signatureBuilder = new SignatureBuilder();


        Criteria criteria = new Criteria();
        criteria.setNarrative("For exhibiting outstanding performance");
        criteria.setId(uuid);

        RankAssessment rankAssessment = new RankAssessment();
        rankAssessment.setValue(8);
        rankAssessment.setMaxValue(1);


        //todo decide hosted or signed badge based on config

        String[] type = new String[]{properties.get("typeOfVerification")};
        VerificationObject verificationObject = new VerificationObject();
        verificationObject.setType(type);

        /**
         *  recipent object
         *  **/
        compositeIdentityObjectBuilder.setName(certModel.getRecipientName()).setId(certModel.getRecipientPhone())
                .setHashed(false).
                setType(new String[]{"phone"});


        issuerBuilder.setId(properties.get("IssuerUrl")).setName(certModel.getIssuer());
        /**
         * badge class object
         * **/

        badgeClassBuilder.setName(certModel.getCourseName()).setDescription(certModel.getCertificateDescription())
                .setId(properties.get("BadgeUrl")).setCriteria(criteria)
                .setImage(certModel.getCertificateLogo()).
                setIssuer(issuerBuilder.build());


        /**
         *  assessed evidence object
         **/
        AssessmentBuilder assessmentBuilder = new AssessmentBuilder(context);
        assessmentBuilder.setValue(21);

        assessedEvidenceBuilder.setAssessedBy("https://dgt.example.gov.in/iti-assessor.json").setId(uuid)
                .setAssessedOn(certModel.getAssessedOn()).setAssessment(assessmentBuilder.build());

        /**
         *
         * Certificate extension object
         */
        certificateExtensionBuilder.setId(uuid).setRecipient(compositeIdentityObjectBuilder.build())
                .setBadge(badgeClassBuilder.build()).setEvidence(assessedEvidenceBuilder.build())
                .setIssuedOn(certModel.getIssuedDate()).setExpires(certModel.getExpiry())
                .setValidFrom(certModel.getValidFrom()).setVerification(verificationObject);


//        /**
//         * to assign signature value
//         */
//        initSignatureHelper(certModel.getSignatoryList());
//        /** certificate before signature value **/
//        String toSignCertificate = certificateExtensionBuilder.build().toString();
//
//        String signatureValue = getSignatureValue(toSignCertificate);
//
//        signatureBuilder.setCreated(Instant.now().toString()).setCreator("https://dgt.example.gov.in/keys/awarding_body.json")
//                .setSignatureValue(signatureValue);
//        certificateExtensionBuilder.setSignature(signatureBuilder.build());
//
//        logger.info("signed certificate is valid {}", verifySignature(toSignCertificate, signatureValue));
        logger.info("certificate extension => {}", certificateExtensionBuilder.build());
        return certificateExtensionBuilder.build();
    }


    public Properties readPropertiesFile() {
        ClassLoader loader = CertificateFactory.class.getClassLoader();
        Properties properties = new Properties();
        try (InputStream resourceStream = loader.getResourceAsStream(resourceName)) {
            properties.load(resourceStream);
        } catch (IOException e) {
            e.printStackTrace();
            logger.info("Exception while reading application.properties {}", e.getMessage());
        }
        return properties;
    }

    public static boolean verifySignature(String certificate, String signatureValue) {
        boolean isValid = false;
        try {
            isValid = signatureHelper.verify(certificate.getBytes(),
                    signatureValue);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        }
        return isValid;
    }

    private static void initSignatureHelper(KeyPair keyPair) {

        try {
            signatureHelper = new SignatureHelper("SHA1withRSA", keyPair);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }


    private static String getSignatureValue(String toSignCertificate) {
        try {
            String signatureValue = signatureHelper.sign(toSignCertificate.getBytes());
            return signatureValue;
        } catch (SignatureException | UnsupportedEncodingException | InvalidKeyException e) {
            e.printStackTrace();
            return null;
        }

    }
}

