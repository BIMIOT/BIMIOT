package fr.bimiot.websocket;

import fr.bimiot.simulator.ConverterEvent;
import org.json.JSONObject;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.core.MessageSendingOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
public class DataPublisher implements ApplicationListener<ConverterEvent> {
    private final MessageSendingOperations<String> messageSendingOperations;

    public DataPublisher(MessageSendingOperations<String> messageSendingOperations) {
        this.messageSendingOperations = messageSendingOperations;
    }

    @Override
    public void onApplicationEvent(ConverterEvent event) {
        sendMessage(event);
    }
    public void sendMessage(ConverterEvent event) {
        var data = event.getMessage();
        var sensorJson = new JSONObject()
                .put("sensorIfcID", Integer.parseInt(data.sensorIfcID()))
                .put("value", Float.parseFloat(data.value()))
                .put("roomIfcID", Integer.parseInt(data.roomIfcID()))
                .put("sensorType", data.type())
                .put("color", data.color())
                .put("averageValue", data.averageValue());
        this.messageSendingOperations.convertAndSend("/data/sensors", sensorJson.toString());
    }
}
