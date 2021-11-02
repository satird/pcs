package ru.satird.pcs.services;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Component
public class Sender {

    private static final String QUEUE_NAME = "senla";
    @Value("${sender.hostname}")
    private static String hostSender;

    private Sender() {
    }

    public static void sendMessage(String body, String subject, String recipient) throws IOException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(hostSender);
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);

            JSONObject obj = new JSONObject();
            obj.put("body", body);
            obj.put("subject", subject);
            obj.put("recipient", recipient);
            byte[] data = obj.toJSONString().getBytes();
            channel.basicPublish("", QUEUE_NAME, null, data);
            System.out.println(" [x] Sent '" + subject + "'");
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}
