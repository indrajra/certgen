package org.incredible.certProcessor;

import org.incredible.builders.*;
import org.incredible.pojos.Assessment;
import org.incredible.pojos.CertificateExtension;
import org.incredible.pojos.RankAssessment;
import org.incredible.pojos.ob.Criteria;

import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

import org.incredible.pojos.ob.Evidence;
import org.incredible.pojos.ob.VerificationObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CertificateFactory {

    private static String uuid;

    private static Logger logger = LoggerFactory.getLogger(CertificateFactory.class);


    /**
     * The domain that holds the contexts for public consumption
     */

    public CertificateExtension createCertificate(CertModel certModel, String context) {

        uuid = "http://localhost:8080/_schemas/Certificate.json";

        CertificateExtensionBuilder certificateExtensionBuilder = new CertificateExtensionBuilder(context);
        CompositeIdentityObjectBuilder compositeIdentityObjectBuilder = new CompositeIdentityObjectBuilder(context);
        BadgeClassBuilder badgeClassBuilder = new BadgeClassBuilder(context);
        AssessedEvidenceBuilder assessedEvidenceBuilder = new AssessedEvidenceBuilder("http://localhost:8080/_schemas/Extensions/AssessedEvidence/Context.json");


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
                .setId("http://localhost:8080/_schemas/Badge.json").setCriteria(criteria)
                .setImage(certModel.getCertificateLogo()).
                setIssuer("http://localhost:8080/_schemas/Issuer.json");


        /**
         *  assessed evidence object
         **/
        AssessmentBuilder assessmentBuilder = new AssessmentBuilder();
        assessmentBuilder.setValue(21);

        assessedEvidenceBuilder.setAssessedBy("https://dgt.example.gov.in/iti-assessor.json").setId(uuid)
                .setAssessedOn(Instant.now().toString()).setSubject("maths").setAssessment(assessmentBuilder.build());

        /**
         * Certificate extension object
         */
        certificateExtensionBuilder.setId(uuid).setRecipent(compositeIdentityObjectBuilder.build())
                .setBadge(badgeClassBuilder.build()).setEvidence(assessedEvidenceBuilder.build())
                .setIssuedOn(certModel.getIssuedDate()).setExpires(certModel.getExpiry())
                .setValidFrom(certModel.getValidFrom()).setVerification(verificationObject);

        logger.info("certificate extension => {}", certificateExtensionBuilder.build());

        return certificateExtensionBuilder.build();
    }
    /**
     * Loads the JSON-LD context
     * @throws IOException
     */
}