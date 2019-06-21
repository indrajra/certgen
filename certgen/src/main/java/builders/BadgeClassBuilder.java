package builders;

import org.incredible.pojos.ob.AlignmentObject;
import org.incredible.pojos.ob.BadgeClass;
import org.incredible.pojos.ob.Criteria;

import java.util.UUID;

public class BadgeClassBuilder implements IBuilder<BadgeClass> {

    private BadgeClass badgeClass = new BadgeClass();



    public BadgeClassBuilder setId(UUID id) {
        badgeClass.setId(id);
        return this;

    }


    public BadgeClassBuilder setType(String[] type) {
        badgeClass.setType(type);
        return this;
    }


    public BadgeClassBuilder setName(String name) {
        badgeClass.setName(name);
        return this;
    }


    public BadgeClassBuilder setDescription(String description) {
        badgeClass.setDescription(description);
        return this;
    }




    public BadgeClassBuilder setImage(String image) {
        badgeClass.setImage(image);
        return this;
    }


    public BadgeClassBuilder setCriteria(Criteria criteria) {
        badgeClass.setCriteria(criteria);
        return this;
    }


    public BadgeClassBuilder setIssuer(String issuer) {
        badgeClass.setIssuer(issuer);
        return this;
    }


    public BadgeClassBuilder setAlignment(AlignmentObject alignment) {
         badgeClass.setAlignment(alignment);
         return this;
    }


    @Override
    public BadgeClass build() {
        return this.badgeClass;
    }
}
