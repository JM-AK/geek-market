package ru.geekbrains.market.utils.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CartReceiverRabbit {
    private final static String QUEUE_NAME_PREFIX = "add_to_cart_product";

    public void receiveProductDto () throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME_PREFIX, false, false, false, null);

        final DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String msg = new String(delivery.getBody(), "UTF-8");
            log.info("Reciver " + msg);
        };

        channel.basicConsume(QUEUE_NAME_PREFIX, true, deliverCallback, consumerTag -> {});
    }

}
