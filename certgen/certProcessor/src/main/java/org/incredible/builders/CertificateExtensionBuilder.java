package org.incredible.builders;


import org.incredible.pojos.CertificateExtension;
import org.incredible.pojos.CompositeIdentityObject;
import org.incredible.pojos.Signature;
import org.incredible.pojos.ob.BadgeClass;
import org.incredible.pojos.ob.Evidence;
import org.incredible.pojos.ob.Issuer;
import org.incredible.pojos.ob.VerificationObject;
import org.incredible.pojos.ob.exeptions.InvalidDateFormatException;


public class CertificateExtensionBuilder implements IBuilder<CertificateExtension> {

    CertificateExtension certificateExtension;


    public CertificateExtensionBuilder(String context) {
        certificateExtension = new CertificateExtension(context);
    }

    public CertificateExtensionBuilder setAwardedThrough(String awardedThrough) {
        certificateExtension.setAwardedThrough(awardedThrough);
        return this;
    }

    public CertificateExtensionBuilder setId(String id) {
        certificateExtension.setId(id);
        return this;
    }


    public CertificateExtensionBuilder setSignatory(Issuer[] signatory) {
        certificateExtension.setSignatory(signatory);
        return this;
    }


    public CertificateExtensionBuilder setPrintUri(String printUri) {
        certificateExtension.setPrintUri(printUri);
        return this;
    }

    public CertificateExtensionBuilder setIssuedOn(String issuedOn) throws InvalidDateFormatException {
        certificateExtension.setIssuedOn(issuedOn);
        return this;

    }

    public CertificateExtensionBuilder setValidFrom(String validFrom) {
        certificateExtension.setValidFrom(validFrom);
        return this;
    }


    public CertificateExtensionBuilder setSignature(Signature signature) {
        certificateExtension.setSignature(signature);
        return this;
    }

    public CertificateExtensionBuilder setRecipient(CompositeIdentityObject objectBuilder) {
        certificateExtension.setRecipient(objectBuilder);
        return this;
    }

    public CertificateExtensionBuilder setBadge(BadgeClass badge) {
        certificateExtension.setBadge(badge);
        return this;
    }

    public CertificateExtensionBuilder setEvidence(Evidence evidence) {
        certificateExtension.setEvidence(evidence);
        return this;
    }

    public CertificateExtensionBuilder setExpires(String expires) throws InvalidDateFormatException {
        certificateExtension.setExpires(expires);
        return this;
    }


    public CertificateExtensionBuilder setVerification(VerificationObject verificationObject) {
        certificateExtension.setVerification(verificationObject);
        return this;
    }


    @Override
    public CertificateExtension build() {
        return certificateExtension;
    }

}
