package org.incredible.pojos.ob;

import java.util.UUID;

public class BadgeClass extends OBBase {
    /**
     * Unique IRI for the badge - HTTP URL or URN
     */
    private UUID id;

    public BadgeClass() {
    }

    /**
     * In most cases, this will simply be the string BadgeClass. An array including BadgeClass and other string
     * elements that are either URLs or compact IRIs within the current context are allowed.
     */
    private String[] type = new String[]{"BadgeClass"};

    /**
     * Name of the achievement represented by this class
     * Example: "Carpentry know-how level 1"
     */
    private String name;

    /**
     * Description of the badge
     */
    private String description;


    private String version;

    /**
     * HTTP URL to the image of this credential
     */
    private String image;

    private Criteria criteria;

    /**
     * HTTP URL to the issuer of this credential - Profile
     */
    private String issuer;

    private AlignmentObject alignment;

    public BadgeClass(String ctx) {
        setContext(ctx);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String[] getType() {
        return type;
    }

    public void setType(String[] type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public void setVersion(String version) {
        this.version = version;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Criteria getCriteria() {
        return criteria;
    }

    public void setCriteria(Criteria criteria) {
        this.criteria = criteria;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public AlignmentObject getAlignment() {
        return alignment;
    }

    public void setAlignment(AlignmentObject alignment) {
        this.alignment = alignment;
    }
}
