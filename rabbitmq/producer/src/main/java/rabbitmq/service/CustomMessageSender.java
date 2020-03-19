package rabbitmq.service;


import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import rabbitmq.enitity.CustomMessage;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomMessageSender {

    private final RabbitTemplate rabbitTemplate;

    public void sendMessage(CustomMessage customMessage) {

        log.info("Sending message...");
        rabbitTemplate.convertAndSend("eggs", customMessage);
    }

    public void sendMessages(List<CustomMessage> customMessages) {
        customMessages.forEach(customMessage -> log.info(" sending message.."));
        rabbitTemplate.convertAndSend("eggs", customMessages);
    }
}
