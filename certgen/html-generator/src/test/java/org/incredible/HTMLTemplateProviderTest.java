package org.incredible;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;



import static org.junit.Assert.*;

public class HTMLTemplateProviderTest {

    HTMLTemplateProvider htmlTemplateProvider = new HTMLTemplateProvider() {
        @Override
        public String getTemplateContent() {
            return null;
        }
    };

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }


    //todo rename - done
    @Test
    public void TestValidHTMlTemplate() {

        HTMLTemplateFile htmlTemplateFile = new HTMLTemplateFile("ValidTemplate.html");
        Boolean valid = htmlTemplateProvider.checkHtmlTemplateIsValid(htmlTemplateFile.getTemplateContent());
        assertEquals(true, valid);

    }

    @Test
    public void TestInValidHTMLTemplate() {

        HTMLTemplateFile htmlTemplateFile = new HTMLTemplateFile("InvalidTemplate.html");
        Boolean valid = htmlTemplateProvider.checkHtmlTemplateIsValid(htmlTemplateFile.getTemplateContent());
        assertEquals(false, valid);

    }
}