package ru.satird.mailsend.services;

public interface EmailService {

    void sendMessages(String emailTo, String subject, String message);
}
