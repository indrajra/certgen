package org.incredible.certProcessor;

import org.incredible.builders.*;
import org.incredible.pojos.CertificateExtension;
import org.incredible.pojos.RankAssessment;
import org.incredible.pojos.ob.Criteria;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.Properties;
import java.util.UUID;

import org.incredible.pojos.ob.VerificationObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CertificateFactory {

    private static String uuid;

    private static Logger logger = LoggerFactory.getLogger(CertificateFactory.class);

    final String resourceName = "application.properties";


    public CertificateExtension createCertificate(CertModel certModel, String context) {

        /**
         * to read application.properties
         */

        Properties properties = readPropertiesFile();


        uuid = properties.get("DOMAIN") + UUID.randomUUID().toString();

        CertificateExtensionBuilder certificateExtensionBuilder = new CertificateExtensionBuilder(context);
        CompositeIdentityObjectBuilder compositeIdentityObjectBuilder = new CompositeIdentityObjectBuilder(context);
        BadgeClassBuilder badgeClassBuilder = new BadgeClassBuilder(context);
        AssessedEvidenceBuilder assessedEvidenceBuilder = new AssessedEvidenceBuilder(properties.getProperty("AssessedDomain"));


        Criteria criteria = new Criteria();
        criteria.setNarrative("For exhibiting outstanding performance");
        criteria.setId(uuid);

        RankAssessment rankAssessment = new RankAssessment();
        rankAssessment.setValue(8);
        rankAssessment.setMaxValue(1);


        String[] type = new String[]{"hosted"};
        VerificationObject verificationObject = new VerificationObject();
        verificationObject.setType(type);

        /**
         *  recipent object
         *  **/
        compositeIdentityObjectBuilder.setName(certModel.getRecipientName()).setId(certModel.getRecipientPhone())
                .setHashed(false).
                setType(new String[]{"phone"});

        /**
         * badge class object
         * **/

        badgeClassBuilder.setName(certModel.getCourseName()).setDescription(certModel.getCertificateDescription())
                .setId(properties.getProperty("BadgeUrl")).setCriteria(criteria)
                .setImage(certModel.getCertificateLogo()).
                setIssuer(properties.getProperty("IssuerUrl"));


        /**
         *  assessed evidence object
         **/
        AssessmentBuilder assessmentBuilder = new AssessmentBuilder(context);
        assessmentBuilder.setValue(21);

        assessedEvidenceBuilder.setAssessedBy("https://dgt.example.gov.in/iti-assessor.json").setId(uuid)
                .setAssessedOn(Instant.now().toString()).setAssessment(assessmentBuilder.build());

        /**
         * Certificate extension object
         */
        certificateExtensionBuilder.setId(uuid).setRecipient(compositeIdentityObjectBuilder.build())
                .setBadge(badgeClassBuilder.build()).setEvidence(assessedEvidenceBuilder.build())
                .setIssuedOn(certModel.getIssuedDate()).setExpires(certModel.getExpiry())
                .setValidFrom(certModel.getValidFrom()).setVerification(verificationObject);

        logger.info("certificate extension => {}", certificateExtensionBuilder.build());

        return certificateExtensionBuilder.build();
    }

    /**
     * Loads the JSON-LD context
     *
     * @throws IOException
     */

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

}