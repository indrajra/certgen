package org.incredible;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.incredible.pojos.CertificateExtension;
import org.incredible.pojos.ob.Assertion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HTMLGenerator {

    private static Logger logger = LoggerFactory.getLogger(HTMLGenerator.class);

    private static String HtmlString;

    public HTMLGenerator(String htmlString) {
        HtmlString = htmlString;
    }

    private HashSet<String> htmlReferenceVariable = new HashSet<>();

//todo velocity.init() should called once if template is same

    /**
     * generating html for list of certificates where Velocity.init() called once
     *
     * @param certificateExtension list of certificates
     */
    public void generateHTMLForListOfCertificate(ArrayList<CertificateExtension> certificateExtension) {
        htmlReferenceVariable = HTMLTemplateProvider.storeAllHTMLTemplateVariables(HtmlString);
        Velocity.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        Velocity.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        Velocity.init();
        for (int index = 0; index < certificateExtension.size(); index++) {
            createContext(certificateExtension.get(index));
        }
    }

    public void generateHTMLForSingleCertificate(CertificateExtension certificateExtension) {
        htmlReferenceVariable = HTMLTemplateProvider.storeAllHTMLTemplateVariables(HtmlString);
        Velocity.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        Velocity.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        Velocity.init();
        createContext(certificateExtension);
    }

    private void createContext(CertificateExtension certificateExtension) {
        VelocityContext context = new VelocityContext();
        String id = certificateExtension.getId().split("Certificate/")[1];
        HTMLVarResolver htmlVarResolver = new HTMLVarResolver(certificateExtension);
        Iterator<String> itr = htmlReferenceVariable.iterator();

        while (itr.hasNext()) {
            String macro = itr.next().substring(1);
            String methodName = "get" + capitalize(macro);
            try {
                Method method = htmlVarResolver.getClass().getMethod(methodName);
                method.setAccessible(true);
                context.put(macro, method.invoke(htmlVarResolver));
                Writer writer = new FileWriter(new File(id + ".html"));
                Velocity.evaluate(context, writer, "velocity", HtmlString);
                writer.flush();
                writer.close();
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | IOException e) {
                e.printStackTrace();
                logger.info("exception while generating html for certificate {}", e.getMessage());
            }
        }
    }


    private String capitalize(String input) {
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }


}