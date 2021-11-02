package ru.satird.pcs.registration.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import ru.satird.pcs.domains.User;
import ru.satird.pcs.registration.OnRegistrationCompleteEvent;
import ru.satird.pcs.services.Sender;
import ru.satird.pcs.services.UserService;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

    private UserService service;
    private MessageSource messages;
    private JavaMailSender mailSender;
    private Environment env;

    @Autowired
    public void setEnv(Environment env) {
        this.env = env;
    }

    @Autowired
    public void setService(UserService service) {
        this.service = service;
    }

    @Autowired
    public void setMessages(@Qualifier("messageSource") MessageSource messages) {
        this.messages = messages;
    }

    @Autowired
    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(OnRegistrationCompleteEvent event) {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        service.createVerificationTokenForUser(user, token);

//        final SimpleMailMessage email = constructEmailMessage(event, user, token);
        constructAndSendEmailMessage(event, user, token);
//        mailSender.send(email);
    }

    private void constructAndSendEmailMessage(final OnRegistrationCompleteEvent event, final User user, final String token) {
        final String recipientAddress = user.getEmail();
        final String subject = "Registration Confirmation";
        final String confirmationUrl = event.getAppUrl() + "/registrationConfirm?token=" + token;
        final String message = messages.getMessage("message.regSuccLink", null, "You registered successfully. To confirm your registration, please click on the below link.", event.getLocale());
        try {
            Sender.sendMessage(message + " \r\n" + confirmationUrl, subject, recipientAddress);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    private SimpleMailMessage constructEmailMessage(final OnRegistrationCompleteEvent event, final User user, final String token) {
//        final String recipientAddress = user.getEmail();
//        final String subject = "Registration Confirmation";
//        final String confirmationUrl = event.getAppUrl() + "/registrationConfirm?token=" + token;
//        final String message = messages.getMessage("message.regSuccLink", null, "You registered successfully. To confirm your registration, please click on the below link.", event.getLocale());
////        try {
////            Sender.sendMessage(message + " \r\n" + confirmationUrl, subject, recipientAddress);
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
//        final SimpleMailMessage email = new SimpleMailMessage();
//        email.setTo(recipientAddress);
//        email.setSubject(subject);
//        email.setText(message + " \r\n" + confirmationUrl);
//        email.setFrom(Objects.requireNonNull(env.getProperty("support.email")));
//        return email;
//    }
}
