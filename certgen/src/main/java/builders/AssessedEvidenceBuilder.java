package builders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.incredible.pojos.AssessedEvidence;
import org.incredible.pojos.RankAssessment;
import org.incredible.pojos.Signature;

import java.util.UUID;


public class AssessedEvidenceBuilder implements IBuilder<AssessedEvidence> {

    private static ObjectMapper mapper = new ObjectMapper();

    private String context;
    private AssessedEvidence assessedEvidence = new AssessedEvidence(context);


    public AssessedEvidenceBuilder setId(UUID id) {
        assessedEvidence.setId(id);
        return this;
    }


    public AssessedEvidenceBuilder setDescription(String description) {
        assessedEvidence.setDescription(description);
        return this;
    }

    public AssessedEvidenceBuilder setSubject(String subject) {
        assessedEvidence.setSubject(subject);
        return this;
    }


    public AssessedEvidenceBuilder setAssessedBy(String assessedBy) {
        assessedEvidence.setAssessedBy(assessedBy);
        return this;
    }


    public AssessedEvidenceBuilder setAssessedOn(String assessedOn) {
        assessedEvidence.setAssessedOn(assessedOn);
        return this;
    }

    public AssessedEvidenceBuilder setSignature(Signature signature) {
        assessedEvidence.setSignature(signature);
        return this;
    }

    public AssessedEvidenceBuilder setAssessment(RankAssessment rankAssessment) {
        assessedEvidence.setAssessment(rankAssessment);
        return this;
    }

    @Override
    public AssessedEvidence build() {
        return this.assessedEvidence;
    }

    @Override
    public String toString() {
        String stringRep = null;
        try {
            stringRep = mapper.writeValueAsString(this.assessedEvidence);
        } catch (JsonProcessingException jpe) {
            jpe.printStackTrace();
        }
        return stringRep;
    }
}
