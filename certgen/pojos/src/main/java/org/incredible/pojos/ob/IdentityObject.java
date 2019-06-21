package org.incredible.pojos.ob;

import java.util.UUID;

public class IdentityObject extends OBBase {
    /**
     * Value of the annotation
     * Example: father's or a spouse's name
     */
    private UUID identity;

    /**
     * IRI of the property by which the recipient of a badge is identified.
     */
    private String[] type;

    /**
     * Whether or not the identity value is hashed.
     */
    private boolean hashed;

    /**
     * If the recipient is hashed, this should contain the string used to
     * salt the hash.
     */
    private String salt;

    public UUID getIdentity() {
        return identity;
    }

    public void setIdentity(UUID identity) {
        this.identity = identity;
    }

    public String[] getType() {
        return type;
    }

    public void setType(String[] type) {
        this.type = type;
    }

    public boolean isHashed() {
        return hashed;
    }

    public void setHashed(boolean hashed) {
        this.hashed = hashed;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
}
