package rabbitmq.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import rabbitmq.enitity.CustomMessage;
import rabbitmq.service.CustomMessageSender;


@RestController
@RequiredArgsConstructor
public class PublisherMQ {

    private final CustomMessageSender customMessageSender;

    @PostMapping("/addMessage")
    public String get(@RequestBody CustomMessage message) {
        customMessageSender.sendMessage(message);
        return "sent" + message.toString();
    }
}
