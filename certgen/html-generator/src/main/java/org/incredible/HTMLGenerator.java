package org.incredible;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.incredible.pojos.ob.Assertion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HTMLGenerator {

    private static Logger logger = LoggerFactory.getLogger(HTMLGenerator.class);

    private HTMLModel htmlModel = new HTMLModel();


    public void generateHTML(Assertion assertion, String htmlString) {

        String id = assertion.getId().split("Certificate/")[1];
        mapToHTMLModel(assertion);
        Velocity.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        Velocity.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        Velocity.init();

        /**  create a context and add data **/
        VelocityContext context = new VelocityContext();

        HTMLTemplateVariables htmlMacros[] = HTMLTemplateVariables.values();

        for (int i = 0; i < htmlMacros.length; i++) {
            String methodName = "get" + capitalize(htmlMacros[i].toString());
            try {
                Method method = htmlModel.getClass().getMethod(methodName);
                method.setAccessible(true);
                context.put(htmlMacros[i].toString(), method.invoke(htmlModel));
                Writer writer = new FileWriter(new File(id + ".html"));
                Velocity.evaluate(context, writer, "velocity", htmlString);
                writer.flush();
                writer.close();
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | IOException e) {
                e.printStackTrace();
                logger.info("exception while generating html for certificate {}", e.getMessage());
            }
        }
    }

    /**
     * to read html mapper
     */


    private void mapToHTMLModel(Assertion assertion) {
        String id = assertion.getId().split("Certificate/")[1];
        File file = new File(id + ".png");
        String path = file.getPath();
        htmlModel.setCourse(assertion.getBadge().getName());
        htmlModel.setRecipient(assertion.getRecipient().getName());
        htmlModel.setDated(assertion.getIssuedOn());
        htmlModel.setImg(path);
        htmlModel.setTitle("Certificate");
    }

    private String capitalize(String input) {
        String output = input.substring(0, 1).toUpperCase() + input.substring(1);
        return output;
    }


}