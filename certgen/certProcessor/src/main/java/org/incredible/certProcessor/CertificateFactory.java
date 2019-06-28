package org.incredible.certProcessor;

import org.incredible.builders.AssessedEvidenceBuilder;
import org.incredible.builders.BadgeClassBuilder;
import org.incredible.builders.CertificateExtensionBuilder;
import org.incredible.builders.CompositeIdentityObjectBuilder;
import org.incredible.pojos.CertificateExtension;
import org.incredible.pojos.RankAssessment;
import org.incredible.pojos.ob.Criteria;

import java.io.IOException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CertificateFactory {

    private static String uuid;

    private static Logger logger = LoggerFactory.getLogger(CertificateFactory.class);


    /**
     * The domain that holds the contexts for public consumption
     */

    public CertificateExtension createCertificate(CertModel certModel, String context) {
        uuid = UUID.randomUUID().toString();
        System.out.println(uuid);
        CertificateExtensionBuilder certificateExtensionBuilder = new CertificateExtensionBuilder(context);
        CompositeIdentityObjectBuilder compositeIdentityObjectBuilder = new CompositeIdentityObjectBuilder();
        BadgeClassBuilder badgeClassBuilder = new BadgeClassBuilder();
        AssessedEvidenceBuilder assessedEvidenceBuilder = new AssessedEvidenceBuilder(context);


        Criteria criteria = new Criteria();
        criteria.setNarrative("For exhibiting outstanding performance");
        RankAssessment rankAssessment = new RankAssessment();
        rankAssessment.setValue(8);
        rankAssessment.setMaxValue(1);
        /**
         *  recipent object
         *  **/
        compositeIdentityObjectBuilder.setName(certModel.getRecipientName()).setId(uuid).setHashed(true).
                setType(new String[]{"urn"});

        /**
         * badge class object
         * **/

        badgeClassBuilder.setName(certModel.getCourseName()).setDescription(certModel.getCertificateDescription())
                .setId(uuid).setCriteria(criteria)
                .setImage(certModel.getCertificateLogo()).
                setIssuer(certModel.getIssuer());

        /**
         *  assessed evidence object
         **/

        assessedEvidenceBuilder.setAssessedBy("https://dgt.example.gov.in/iti-assessor.json");


        certificateExtensionBuilder.setId(uuid).setRecipent(compositeIdentityObjectBuilder.build())
                .setBadge(badgeClassBuilder.build()).setEvidence(assessedEvidenceBuilder.build());

        logger.info("certificate extension => {}", certificateExtensionBuilder.build());
        System.out.println(certificateExtensionBuilder.build());

        return certificateExtensionBuilder.build();
    }
    /**
     * Loads the JSON-LD context
     * @throws IOException
     */
}