package org.incredible;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class HTMLModel {

    private String recipient;
    private String course;
    private String img;
    private String title;
    private String dated;


    private ObjectMapper mapper = new ObjectMapper();

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDated() {
        return dated;
    }

    public void setDated(String dated) {
        this.dated = dated;
    }

    @Override
    public String toString() {
        String stringRep = null;
        try {
            stringRep = mapper.writeValueAsString(this);
        } catch (JsonProcessingException jpe) {
            jpe.printStackTrace();
        }
        return stringRep;
    }
}
