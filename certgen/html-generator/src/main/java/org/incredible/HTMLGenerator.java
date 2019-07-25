package org.incredible;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Iterator;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.incredible.pojos.CertificateExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HTMLGenerator {

    private static Logger logger = LoggerFactory.getLogger(HTMLGenerator.class);

    private String HtmlString;

    private HashSet<String> htmlReferenceVariable;


    public HTMLGenerator(String htmlString) {
        HtmlString = htmlString;
        htmlReferenceVariable = HTMLTemplateProvider.storeAllHTMLTemplateVariables(HtmlString);
    }


    public void initVelocity() {
        Velocity.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        Velocity.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        Velocity.init();
    }

    /**
     * create velocity context  where Velocity.init() called once
     *
     * @param certificateExtension list of certificates
     */

    public void createContext(CertificateExtension certificateExtension) {
        initVelocity();
        VelocityContext context = new VelocityContext();
        HTMLVarResolver htmlVarResolver = new HTMLVarResolver(certificateExtension);
        htmlReferenceVariable = HTMLTemplateProvider.storeAllHTMLTemplateVariables(HtmlString);
        Iterator<String> iterator = htmlReferenceVariable.iterator();
        while (iterator.hasNext()) {
            String macro = iterator.next().substring(1);
            try {
                Method method = htmlVarResolver.getClass().getMethod("get" + capitalize(macro));
                method.setAccessible(true);
                context.put(macro, method.invoke(htmlVarResolver));
                generateHTML(context, certificateExtension.getId().split("Certificate/")[1] + ".html");
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                logger.info("exception while generating html for certificate {}", e.getMessage());
            }
        }

    }

    private void generateHTML(VelocityContext context, String id) {
        try {
            Writer writer = new FileWriter(new File(id));
            Velocity.evaluate(context, writer, "velocity", HtmlString);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            logger.info("IO exception while creating html file :{}", e.getMessage());
        }
    }

    private String capitalize(String input) {
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }


}