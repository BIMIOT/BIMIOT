package fr.bimiot.domain.use_cases;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.bimiot.domain.entities.Data;
import fr.bimiot.domain.entities.Room;
import fr.bimiot.domain.entities.TypesColors;
import fr.bimiot.fixtures.SensorColorMapFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class ColorManagingTest {

    @InjectMocks
    ManageData manageData;

    @Test
    void testExecute() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        var rooms = objectMapper.readValue(
                "[{\"roomId\":207,\"sensors\":[{\"sensorIFCid\":688,\"sensorDataSetId\":\"sensor3\",\"type\":\"TEMPERATURE\"}]}," +
                        "{\"roomId\":252,\"sensors\":[{\"sensorIFCid\":801,\"sensorDataSetId\":\"sensor4\",\"type\":\"TEMPERATURE\"}]}," +
                        "{\"roomId\":299,\"sensors\":[]}," +
                        "{\"roomId\":329,\"sensors\":[{\"sensorIFCid\":1717,\"sensorDataSetId\":\"sensor6\",\"type\":\"TEMPERATURE\"}]}," +
                        "{\"roomId\":359,\"sensors\":[{\"sensorIFCid\":1672,\"sensorDataSetId\":\"sensor5\",\"type\":\"TEMPERATURE\"}]}," +
                        "{\"roomId\":400,\"sensors\":[{\"sensorIFCid\":1762,\"sensorDataSetId\":\"sensor7\",\"type\":\"TEMPERATURE\"}]}]"
                , Room[].class);
        var colors = objectMapper.readValue(
                "{\"typesColor\":{\"TEMPERATURE\":{\"colors\":[\"#ff0000\",\"#00FFF1\",\"#0000ff\",\"#ffff00\"]," +
                        "\"values\":[3,10,20]},\"HUMIDITY\":{\"colors\":[\"#ff0000\",\"#00ff00\",\"#0000ff\",\"#ffff00\"]," +
                        "\"values\":[3,10,20]},\"LIGHT\":{\"colors\":[\"#ff0000\",\"#00ff00\",\"#0000ff\",\"#ffff00\"]," +
                        "\"values\":[3,10,20]},\"CO2\":{\"colors\":[\"#ff0000\",\"#00ff00\",\"#0000ff\",\"#ffff00\"]," +
                        "\"values\":[3,10,20]}}}"
                , TypesColors.class);
        manageData.setRoomListDTO(Arrays.stream(rooms).toList());
        manageData.setSensorTypeListMap(SensorColorMapFixture.sensorTypeListMapDomain());

        var data = new Data("sensor3", "TEMPERATURE", "4", new Timestamp(1675847798));
    }
}
