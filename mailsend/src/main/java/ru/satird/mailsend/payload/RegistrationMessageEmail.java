package ru.satird.mailsend.payload;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RegistrationMessageEmail {

    private String body;
    private String subject;
    private String recipient;

    public RegistrationMessageEmail(
            @JsonProperty("body") String body,
            @JsonProperty("subject") String subject,
            @JsonProperty("recipient") String recipient) {
        this.body = body;
        this.subject = subject;
        this.recipient = recipient;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }
}
