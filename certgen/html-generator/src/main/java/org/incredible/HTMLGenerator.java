package org.incredible;

import java.io.File;
import java.io.IOException;

import org.incredible.pojos.ob.Assertion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.io.FileUtils;


public class HTMLGenerator {

    private static Logger logger = LoggerFactory.getLogger(HTMLGenerator.class);


    public void generateTemplate(Assertion assertion, String htmlString) {

        String id = assertion.getId().split("Certificate/")[1];

        File file = new File(id + ".png");

        String path = file.getPath();

        htmlString = htmlString.replace("$title", "certificate");
        htmlString = htmlString.replace("$recipient", assertion.getRecipient().getName());
        htmlString = htmlString.replace("$img", path);
        htmlString = htmlString.replace("$course", assertion.getBadge().getName());
        htmlString = htmlString.replace("$dated", assertion.getIssuedOn());
        File newHtmlFile = new File(id + ".html");
        try {
            FileUtils.writeStringToFile(newHtmlFile, htmlString);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}