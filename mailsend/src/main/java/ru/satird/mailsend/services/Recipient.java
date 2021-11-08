package ru.satird.mailsend.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.satird.mailsend.config.RabbitConfiguration;
import ru.satird.mailsend.payload.AdMessageDto;
import ru.satird.mailsend.payload.RegistrationMessageEmail;

import java.util.List;


@Service
public class Recipient {

    private final Logger logger = LoggerFactory.getLogger(Recipient.class);
    private final EmailService emailService;
    @Value("${message.weekly.recipient}")
    private String weeklyReportsRecipient;

    @Autowired
    public Recipient(EmailService emailService) {
        this.emailService = emailService;
    }

    @RabbitListener(queues = RabbitConfiguration.MY_QUEUE_NAME)
    public void listener(RegistrationMessageEmail in) {
        emailService.sendMessages(in.getRecipient(), in.getSubject(), in.getBody());
        logger.info("Message read from myQueue : {}", in);
    }

    @RabbitListener(queues = RabbitConfiguration.ROUTING_KEY)
    public void listenerWeekly(List<AdMessageDto> in) throws JsonProcessingException {
        logger.info("Message read from myQueue : {}", in);
        ObjectMapper objectMapper = new ObjectMapper();
        final String value = objectMapper.writeValueAsString(in);
        emailService.sendMessages(weeklyReportsRecipient, "Weekly report", value);
    }

}
