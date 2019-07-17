package org.incredible;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class HTMLTemplateProvider {

    abstract public String getTemplateContent();

    private static Logger logger = LoggerFactory.getLogger(HTMLTemplateProvider.class);



    //todo html template and model tight coupling must be there
    public Boolean checkHtmlTemplateIsValid(String htmlString) {
        HTMLTemplateVariables htmlMacros[] = HTMLTemplateVariables.values();
        int validate = 0;
        try {
            for (int index = 0; index < htmlMacros.length; index++) {
                if (htmlString.contains(htmlMacros[index].toString())) {
                    validate++;
                }
            }
            if (htmlMacros.length == validate) {
                return true;
            } else {
                throw new Exception("Html template is not valid");
            }
        } catch (Exception e) {
            logger.error("Exception while validating html template {}", e.getMessage());
            return false;

        }
    }
}

