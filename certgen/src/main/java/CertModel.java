import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Date;

public class CertModel {

    private String CourseName;
    private String RecipientName;
    private String RecipientEmail;
    private String RecipientPhone;
    private String CertificateName;
    private String CertificateDescription;
    private String CertificateLogo;
    private String IssuedDate;
    private String Issuer;
    private String ValidFrom;
    private String Expiry;
    private String SignatoryList;
    private static ObjectMapper mapper = new ObjectMapper();






    public CertModel() {
    }

    public String getCourseName() {
        return CourseName;
    }

    public CertModel setCourseName(String courseName) {
        CourseName = courseName;
        return this;
    }

    public String getRecipientName() {
        return RecipientName;
    }

    public CertModel setRecipientName(String recipientName) {
        RecipientName = recipientName;
        return this;
    }

    public String getRecipientEmail() {
        return RecipientEmail;
    }

    public CertModel setRecipientEmail(String recipientEmail) {
        RecipientEmail = recipientEmail;
        return this;
    }

    public String getRecipientPhone() {
        return RecipientPhone;
    }

    public CertModel setRecipientPhone(String recipientPhone) {
        RecipientPhone = recipientPhone;
        return this;

    }

    public String getCertificateName() {
        return CertificateName;
    }

    public CertModel setCertificateName(String certificateName) {
        CertificateName = certificateName;
        return this;
    }

    public String getCertificateDescription() {
        return CertificateDescription;
    }

    public CertModel setCertificateDescription(String certificateDescription) {
        CertificateDescription = certificateDescription;
        return this;
    }

    public String getCertificateLogo() {
        return CertificateLogo;
    }

    public CertModel setCertificateLogo(String certificateLogo) {
        CertificateLogo = certificateLogo;
        return this;
    }

    public String getIssuer() {
        return Issuer;
    }

    public CertModel setIssuer(String issuer) {
        Issuer = issuer;
        return this;
    }

    public String getSignatoryList() {
        return SignatoryList;
    }

    public void setSignatoryList(String signatoryList) {
        SignatoryList = signatoryList;
    }

    public String  getIssuedDate() {
        return IssuedDate;
    }

    public void setIssuedDate(String issuedDate) {
        IssuedDate = issuedDate;
    }

    public String getValidFrom() {
        return ValidFrom;
    }

    public void setValidFrom(String validFrom) {
        ValidFrom = validFrom;
    }


    public String getExpiry() {
        return Expiry;
    }

    public void setExpiry(String expiry) {
        Expiry = expiry;
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
