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

public abstract class HTMLTemplateProvider {

    abstract public String getTemplateContent();

    private static Logger logger = LoggerFactory.getLogger(HTMLTemplateProvider.class);

    private static HashSet<String> htmlReferenceVariable = new HashSet<>();


    //todo html template and model tight coupling must be there
    public Boolean checkHtmlTemplateIsValid(String htmlString) {
        storeAllHTMLTemplateVariables(htmlString);
        HTMLTemplateVariables[] htmlMacros = HTMLTemplateVariables.values();
        HashSet<String> invalidVariables = new HashSet<>();
        int validate = 0;
        try {
            for (int index = 0; index < htmlMacros.length; index++) {
                if (htmlReferenceVariable.contains(htmlMacros[index].toString())) {
                    validate++;
                } else {
                    invalidVariables.add(htmlMacros[index].toString());
                }
            }
            if (validate <= htmlMacros.length && validate == htmlReferenceVariable.size()) {
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

    private static ParserVisitor visitor = new BaseVisitor() {
        @Override
        public Object visit(final ASTReference node, final Object data) {
            htmlReferenceVariable.add(node.literal());
            return null;
        }
    };


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

}

