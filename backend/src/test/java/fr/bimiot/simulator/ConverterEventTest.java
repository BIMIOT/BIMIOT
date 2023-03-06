package fr.bimiot.simulator;

import fr.bimiot.domain.entities.WebSocketData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ConverterEventTest {

    @Test
    void getMessage() {
        var webSocketData = new WebSocketData(
                "sensorIfcID",
                "value",
                "roomIfcId",
                "type",
                "color",
                "averageValue"
        );
        var converterEvent = new ConverterEvent(" ", webSocketData);

        var message = converterEvent.getMessage();
        assertEquals(webSocketData, message);
    }
}