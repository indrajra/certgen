import builders.AssessedEvidenceBuilder;
import builders.BadgeClassBuilder;
import builders.CertificateExtensionBuilder;
import builders.CompositeIdentityObjectBuilder;

import org.incredible.pojos.CertificateExtension;
import org.incredible.pojos.RankAssessment;
import org.incredible.pojos.ob.Criteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

public class CertificateFactory {

    private static String context;
    private static String uuid = UUID.randomUUID().toString();

    private static Logger logger = LoggerFactory.getLogger(CertificateFactory.class);


    /**
     * The domain that holds the contexts for public consumption
     */
    private static final String DOMAIN = "http://localhost:8080";
    private static final String CONTEXT_FILE_NAME = "v1/context.json";
    static CertificateExtension createCertificate(CertModel certModel, String context)  {

//        initContext();
        CertificateExtensionBuilder certificateExtensionBuilder = new CertificateExtensionBuilder(context);
        CompositeIdentityObjectBuilder compositeIdentityObjectBuilder = new CompositeIdentityObjectBuilder();
        BadgeClassBuilder badgeClassBuilder = new BadgeClassBuilder();
        AssessedEvidenceBuilder assessedEvidenceBuilder = new AssessedEvidenceBuilder();


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

        assessedEvidenceBuilder.setId(uuid).setDescription(certModel.getCertificateDescription())
                .setAssessment(rankAssessment).setAssessedOn(Instant.now().toString())
                .setAssessedBy("https://dgt.example.gov.in/iti-assessor.json");


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
//    private static void initContext()  {
//        try {
//            ClassLoader classLoader = CertificateFactory.class.getClassLoader();
//
//            File file = new File(classLoader.getResource(CONTEXT_FILE_NAME).getFile());
//            if (file == null) {
//                throw new IOException("Context file not found");
//            }

//            context = DOMAIN + "/" + CONTEXT_FILE_NAME;
//            System.out.println("Context file Found : " + file.exists());
//        }

//        catch (IOException e) {

//        }

//    }


}

