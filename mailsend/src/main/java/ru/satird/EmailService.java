package ru.satird;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeoutException;

public class EmailService {
    private static final String QUEUE_NAME = "senla";
    private static final String HOST = "smtp.gmail.com";
    private static final int PORT = 465;
    private static final String FROM = "satirdima@gmail.com";
    private static final String PASSWORD = "gynydzygzmsxzjug";
    private final JSONParser parser;
    private String hostSender = "localhost";
//    private String hostSender = "rabbitmq";
    private Map<String, String> map = new HashMap<>();

    public EmailService() {
        parser = new JSONParser();
    }

    private void sendMail(String body, String subject, String recipient) {
        Properties prop = new Properties();
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", HOST);
        prop.put("mail.smtp.port", PORT);
        prop.put("mail.smtp.ssl.trust", HOST);

        prop.put("mail.debug", "true");
        prop.put("mail.smtp.debug", "true");
        prop.put("mail.smtp.socketFactory.port", PORT);
        prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
//        prop.put("mail.smtp.ssl.checkserveridentity", true);
        prop.put("mail.smtp.socketFactory.fallback", "false");

        Session session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM, PASSWORD);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject(subject, "UTF-8");

            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(body, "text/html;charset=utf-8");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);

            message.setContent(multipart);

            Transport.send(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String... args) throws IOException, TimeoutException {
        EmailService emailService = new EmailService();
        emailService.parseRecipient();
    }

    private void parseRecipient() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(hostSender);
        try {
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                JSONObject obj = null;
                try {
                    obj = (JSONObject) parser.parse(message);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                map.put("mail_body", (String) obj.get("body"));
                map.put("mail_subject", (String) obj.get("subject"));
                map.put("mail_recipient", (String) obj.get("recipient"));
                System.out.println(" [x] Received '" + obj.get("body") + "'" + " '" + obj.get("subject") + "' " + " '" + obj.get("recipient") + "'");

                Thread thread = new Thread(() -> {
                    EmailService emailService = new EmailService();
                    emailService.sendMail(map.get("mail_body"), map.get("mail_subject"), map.get("mail_recipient"));
                });
                thread.start();
            };
            channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {
            });
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }
}
