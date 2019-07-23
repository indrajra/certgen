package org.incredible;

import org.apache.velocity.runtime.RuntimeInstance;
import org.apache.velocity.runtime.parser.ParseException;
import org.apache.velocity.runtime.parser.node.ASTReference;
import org.apache.velocity.runtime.parser.node.ParserVisitor;
import org.apache.velocity.runtime.parser.node.SimpleNode;
import org.apache.velocity.runtime.visitor.BaseVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Iterator;

public abstract class HTMLTemplateProvider {

    abstract public String getTemplateContent();

    private static Logger logger = LoggerFactory.getLogger(HTMLTemplateProvider.class);
    /**
     * variables present in html template
     */
    private static HashSet<String> htmlReferenceVariable = new HashSet<>();

    private static HashSet<String> htmlTemplateVariables = new HashSet<>();


    //todo html template and model tight coupling must be there
    public Boolean checkHtmlTemplateIsValid(String htmlString) {
        storeAllHTMLTemplateVariables(htmlString);
        for (HTMLTemplateVariables htmlTemplateVariable : HTMLTemplateVariables.values()) {
            htmlTemplateVariables.add(htmlTemplateVariable.toString());
        }
        Iterator<String> iterator = htmlReferenceVariable.iterator();
        HashSet<String> invalidVariables = new HashSet<>();
        int validate = 0;
        try {
            while (iterator.hasNext()) {
                if (htmlTemplateVariables.contains(iterator.next()))
                    validate++;
                else invalidVariables.add(iterator.next());
            }
            if (validate <= htmlTemplateVariables.size() && validate == htmlReferenceVariable.size()) {
                logger.info("given HTML template is valid");
                htmlReferenceVariable.clear();
                return true;
            } else {
                throw new Exception("HTML template is not valid ");
            }
        } catch (Exception e) {
            logger.error("Exception while validating html template because of following variables {} {}", invalidVariables, e.getMessage());
            htmlReferenceVariable.clear();
            return false;
        }
    }


    /**
     * to get all the reference variables present in htmlString
     *
     * @param htmlString html file read in the form of string
     * @return set of reference variables
     */
    public static HashSet<String> storeAllHTMLTemplateVariables(String htmlString) {
        RuntimeInstance runtimeInstance = new RuntimeInstance();
        SimpleNode node = null;
        try {
            node = runtimeInstance.parse(htmlString, null);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        visitor.visit(node, null);
        return htmlReferenceVariable;
    }

    private static ParserVisitor visitor = new BaseVisitor() {
        @Override
        public Object visit(final ASTReference node, final Object data) {
            htmlReferenceVariable.add(node.literal());
            return null;
        }
    };

}

