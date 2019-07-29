package org.incredible.certProcessor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.incredible.pojos.ob.Issuer;
import org.incredible.pojos.ob.Profile;


public class CertModel {

    /**
     * Mandatory, The course name for which the certificate is generated
     */
    private String courseName;

    /**
     * Mandatory, Name of the person receiving the certificate
     */
    private String recipientName;


    private String recipientEmail;
    private String recipientPhone;
    private String certificateName;
    private String certificateDescription;
    private String certificateLogo;
    /**
     * Mandatory, issuedDate of the certificate
     */
    private String issuedDate;
    /**
     * Mandatory, issuer  the certificate
     */
    private Issuer issuer;
    private String validFrom;
    private String expiry;
    private Profile[] signatoryList;
    private String assessedOn;

    private static ObjectMapper mapper = new ObjectMapper();

    public CertModel() {
    }

    public String getCourseName() {
        return courseName;
    }

    public CertModel setCourseName(String courseName) {
        this.courseName = courseName;
        return this;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public CertModel setRecipientName(String recipientName) {
        this.recipientName = recipientName;
        return this;
    }

    public String getRecipientEmail() {
        return recipientEmail;
    }

    public CertModel setRecipientEmail(String recipientEmail) {
        this.recipientEmail = recipientEmail;
        return this;
    }

    public String getRecipientPhone() {
        return recipientPhone;
    }

    public CertModel setRecipientPhone(String recipientPhone) {
        this.recipientPhone = recipientPhone;
        return this;

    }

    public String getCertificateName() {
        return certificateName;
    }

    public CertModel setCertificateName(String certificateName) {
        this.certificateName = certificateName;
        return this;
    }

    public String getCertificateDescription() {
        return certificateDescription;
    }

    public CertModel setCertificateDescription(String certificateDescription) {
        this.certificateDescription = certificateDescription;
        return this;
    }

    public String getCertificateLogo() {
        return certificateLogo;
    }

    public CertModel setCertificateLogo(String certificateLogo) {
        this.certificateLogo = certificateLogo;
        return this;
    }

    public Issuer getIssuer() {
        return issuer;
    }

    public CertModel setIssuer(Issuer issuer) {
        this.issuer = issuer;
        return this;
    }

    public Profile[] getSignatoryList() {
        return signatoryList;
    }

    public void setSignatoryList(Profile[] signatoryList) {
        this.signatoryList = signatoryList;
    }

    public String getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(String issuedDate) {
        this.issuedDate = issuedDate;

    }

    public String getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(String validFrom) {
        this.validFrom = validFrom;
    }


    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }

    public String getAssessedOn() {
        return assessedOn;
    }

    public void setAssessedOn(String assessedOn) {
        this.assessedOn = assessedOn;
    }


    @Override
    public String toString() {
        String stringRep = null;
        try {
            stringRep = mapper.writeValueAsString(this);
        } catch (JsonProcessingException jpe) {
            jpe.printStackTrace();
        }
        return stringRep;
    }
}
