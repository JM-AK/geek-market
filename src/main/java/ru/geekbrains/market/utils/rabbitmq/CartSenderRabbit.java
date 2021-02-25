package ru.geekbrains.market.utils.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.geekbrains.market.entities.Product;

@Service
@Slf4j
public class CartSenderRabbit {
    private final static String QUEUE_NAME_PREFIX = "add_to_cart_product";

    public void sendProduct(Product product) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (
                Connection connection = factory.newConnection();
                Channel channel = connection.createChannel()) {
            channel.queueDeclare(QUEUE_NAME_PREFIX, false, false, false, null);

            channel.basicPublish("", QUEUE_NAME_PREFIX, null, product.toString().getBytes());
            log.info("sent " + product.toString());
        }
    }
}
