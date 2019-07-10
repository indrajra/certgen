package org.incredible;

import java.io.File;
import java.io.IOException;

import org.incredible.pojos.ob.Assertion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.io.FileUtils;


public class HTMLGenerator {

    private static Logger logger = LoggerFactory.getLogger(HTMLGenerator.class);
    private static final String HTML_TEMPLATE_NAME = "template.html";

    public void generateTemplate(Assertion assertion) {

        File htmlTemplateFile = new File(getPath(HTML_TEMPLATE_NAME));
        String htmlString = null;

        String id = assertion.getId().split("Certificate/")[1];

        File file = new File(id + ".png");

        String path = file.getPath();

        try {
            htmlString = FileUtils.readFileToString(htmlTemplateFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
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


    private static String getPath(String file) {
        ClassLoader loader = HTMLGenerator.class.getClassLoader();

        String result = null;
        try {
            result = loader.getResource(file).getFile();
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            return result;
        }
    }


}