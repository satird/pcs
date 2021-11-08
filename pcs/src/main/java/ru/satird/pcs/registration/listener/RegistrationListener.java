package ru.satird.pcs.registration.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import ru.satird.pcs.domains.User;
import ru.satird.pcs.registration.OnRegistrationCompleteEvent;
import ru.satird.pcs.services.Sender;
import ru.satird.pcs.services.UserService;

import java.util.UUID;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

    private final UserService service;
    private final MessageSource messages;
    private final Sender sender;

    @Autowired
    public RegistrationListener(UserService service, @Qualifier("messageSource") MessageSource messages, Sender sender) {
        this.service = service;
        this.messages = messages;
        this.sender = sender;
    }

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(OnRegistrationCompleteEvent event) {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        service.createVerificationTokenForUser(user, token);

        constructAndSendEmailMessage(event, user, token);
    }

    private void constructAndSendEmailMessage(final OnRegistrationCompleteEvent event, final User user, final String token) {
        final String recipientAddress = user.getEmail();
        final String subject = "Registration Confirmation";
        final String confirmationUrl = event.getAppUrl() + "/registrationConfirm?token=" + token;
        final String message = messages.getMessage("message.regSuccLink", null, "You registered successfully. To confirm your registration, please click on the below link.", event.getLocale());
        sender.sendMessage(message + " \r\n" + confirmationUrl, subject, recipientAddress);
    }
}
