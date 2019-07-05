package org.incredible.pojos;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.incredible.pojos.ob.Assertion;

import java.util.*;


/**
 * An extension to Assertion class
 */
public class CertificateExtension extends Assertion {
    /**
     * Service or program through which the credential is awarded
     */
    private String awardedThrough;

    /**
     * List of IRIs to SignatoryExtension
     */
    private String[] signatory;

    /**
     * A HTTP URL to a printable version of this certificate.
     * The URL points to a base64 encoded data, like data:application/pdf;base64
     * or data:image/jpeg;base64
     */
    private String printUri;

    /**
     * DateTime string compatible with ISO 8601 guideline
     * For example, 2016-12-31T00:00:00+00:00
     * Set time to 00:00:00 if you only need the date
     */
    private String validFrom;

    /**
     * The signature value (hash typically generated using private key)
     */
    private Signature signature;

    private static ObjectMapper mapper = new ObjectMapper();


    public CertificateExtension(String ctx) {
        String[] type = new String[]{"Assertion", "Extension", "extensions:CertificateExtension"};
        setType(type);
        setContext(ctx);
    }

    public String getAwardedThrough() {
        return awardedThrough;
    }

    public void setAwardedThrough(String awardedThrough) {
        this.awardedThrough = awardedThrough;
    }

    public String[] getSignatory() {
        return signatory;
    }

    public void setSignatory(String[] signatory) {
        this.signatory = signatory;
    }

    public String getPrintUri() {
        return printUri;
    }

    public void setPrintUri(String printUri) {
        this.printUri = printUri;
    }

    public String getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(String validFrom) {
        this.validFrom = validFrom;
    }

    public Signature getSignature() {
        return signature;
    }

    public void setSignature(Signature signature) {
        this.signature = signature;
    }
    
//
//    @Override
//    public String toString() {
//
//        Map<String, Object> map = mapper.convertValue(this, new TypeReference<Map<String, Object>>() {
//        });
//        for (Map.Entry<String, Object> entry : map.entrySet()) {
//            if (entry.getValue() != null) {
//                if (!(entry.getValue() instanceof String) && !(entry.getValue() instanceof Boolean)) {
//                if (entry.getKey() != "id" && entry.getKey() != "type") {
//                    Map<String, Object> ChildObject = mapper.convertValue(entry.getValue(), new TypeReference<Map<String, Object>>() {
//                    });
//                    ChildObject.remove("@context");
//                    map.remove(entry.getValue());
//                    map.put(entry.getKey(), ChildObject);
//                }
//            }
//
//            }
//        }
//
//        try {
//            return mapper.writeValueAsString(map);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//            return null;
//        }
//
//    }
}
