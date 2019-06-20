package org.incredible.pojos;

import org.incredible.pojos.ob.OBBase;

public class Assessment extends OBBase {
    private String[] type;
    private float value;

    public Assessment() {
        type = new String[] {"Extension", "extensions:Assessment"};
    }

    public String[] getType() {
        return type;
    }

    public void setType(String[] type) {
        this.type = type;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}
