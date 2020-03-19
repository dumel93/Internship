package rabbitmq.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import rabbitmq.enitity.CustomMessage;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomMessageReceiver {

    private final RabbitTemplate rabbitTemplate;


    public CustomMessage receiveMessage() {

        return (CustomMessage) rabbitTemplate.receiveAndConvert("eggs");
    }

    public List<CustomMessage> receiveMessages() {
        return (List<CustomMessage>) rabbitTemplate.receiveAndConvert("eggs");
    }
}
