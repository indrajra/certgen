package builders;

import org.incredible.pojos.CertificateExtension;
import org.incredible.pojos.Signature;

public class CertificateExtensionBuilder implements IBuilder<CertificateExtension> {

    CertificateExtension certificateExtension;


    public CertificateExtensionBuilder setAwardedThrough(String awardedThrough) {
        certificateExtension.setAwardedThrough(awardedThrough);
        return this;
    }


    public CertificateExtensionBuilder setSignatory(String[] signatory)
    {
        certificateExtension.setSignatory(signatory);
        return this;
    }


    public CertificateExtensionBuilder setPrintUri(String printUri) {
        certificateExtension.setPrintUri(printUri);
        return this;
    }


    public CertificateExtensionBuilder setValidFrom(String validFrom)
    {
        certificateExtension.setValidFrom(validFrom);
        return this;
    }



    public CertificateExtensionBuilder setSignature(Signature signature) {
        certificateExtension.setSignature(signature);
        return this;
    }


    @Override
    public CertificateExtension build() {
        return this.certificateExtension;
    }
}
