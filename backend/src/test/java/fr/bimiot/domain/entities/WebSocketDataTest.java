package fr.bimiot.domain.entities;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class WebSocketDataTest {
    @Test
    void equals() {
        var webSocketData = new WebSocketData(
                "sensorIfcID",
                "value",
                "roomIfcID",
                "type",
                "color",
                "averageValue"
        );

        var webSocketData2 = new WebSocketData(
                "sensorIfcID",
                "value",
                "roomIfcID",
                "type",
                "color",
                "averageValue"
        );

        assertEquals(webSocketData, webSocketData2);
    }
}