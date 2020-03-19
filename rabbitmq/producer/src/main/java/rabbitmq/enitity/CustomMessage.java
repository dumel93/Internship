package rabbitmq.enitity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CustomMessage {

    @JsonProperty("message")
    private String message;

}
