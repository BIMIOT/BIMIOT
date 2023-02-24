package fr.bimiot.simulator;

import fr.bimiot.core.entities.WebSocketData;
import org.springframework.context.ApplicationEvent;

public class ConverterEvent extends ApplicationEvent {
    private WebSocketData message;

    public ConverterEvent(Object source, WebSocketData message) {
        super(source);
        this.message = message;
    }
    public WebSocketData getMessage() {
        return message;
    }
}
