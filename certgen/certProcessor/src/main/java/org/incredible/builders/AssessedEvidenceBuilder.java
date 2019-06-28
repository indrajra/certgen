package org.incredible.builders;

import org.incredible.pojos.AssessedEvidence;
import org.incredible.pojos.Signature;


public class AssessedEvidenceBuilder implements IBuilder<AssessedEvidence> {



    private AssessedEvidence assessedEvidence;
    // TODO - Fix context - done

    public AssessedEvidenceBuilder(String context) {
//        this.context = context;
        assessedEvidence = new AssessedEvidence(context);
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
        assessedEvidence.setAssessedBy(assessedOn);
        return this;
    }

    private AssessedEvidenceBuilder setSignature(Signature signature) {
        assessedEvidence.setSignature(signature);
        return this;
    }

    @Override
    public AssessedEvidence build() {
        return this.assessedEvidence;
    }
}
