package org.incredible.builders;

import org.incredible.pojos.Assessment;

public class AssessmentBuilder implements IBuilder<Assessment> {

    Assessment assessment = new Assessment();


    public AssessmentBuilder setType(String[] type) {
        assessment.setType(type);
        return this;
    }


    public AssessmentBuilder setValue(float value) {
        assessment.setValue(value);
        return this;
    }
    @Override
    public Assessment build() {
        return this.assessment;
    }
}
