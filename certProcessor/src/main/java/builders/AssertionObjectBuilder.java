package builders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.incredible.pojos.ob.Assertion;

public class AssertionObjectBuilder implements IBuilder<Assertion> {

    // TODO - Fix context
    public Assertion assertion = new Assertion("");

    private static ObjectMapper mapper = new ObjectMapper();


    public AssertionObjectBuilder setContext(String context) {
        assertion.setContext(context);
        return this;
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
