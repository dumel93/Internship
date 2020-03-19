package rabbitmq.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import rabbitmq.enitity.CustomMessage;
import rabbitmq.service.CustomMessageReceiver;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class ClientMq {

    private final CustomMessageReceiver customMessageReceiver;

    @GetMapping("/receiveMessage")
    public CustomMessage receive() {
        return customMessageReceiver.receiveMessage();

    }

    @GetMapping("/receiveMessages")
    public List<CustomMessage> receiveMessages() {
        return customMessageReceiver.receiveMessages();

    }

}
