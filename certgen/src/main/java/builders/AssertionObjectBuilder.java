package builders;

import org.incredible.pojos.ob.Assertion;

public class AssertionObjectBuilder implements IBuilder<Assertion> {


    private Assertion assertion;

    public AssertionObjectBuilder(String context) {
        assertion.setContext(context);
    }


    public AssertionObjectBuilder setId(String id) {
        assertion.setId(id);
        return this;
    }

    public AssertionObjectBuilder setIssuedOn(String issuedOn) {
        assertion.setIssuedOn(issuedOn);
        return this;
    }

    public AssertionObjectBuilder setRecipient() {
        return this;
    }

    @Override
    public Assertion build() {
        return this.assertion;
    }
}
