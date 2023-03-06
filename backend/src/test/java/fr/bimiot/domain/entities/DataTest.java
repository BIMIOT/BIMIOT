package fr.bimiot.domain.entities;

import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class DataTest {

    @Test
    void testToString() {
        var id = "id";
        var type = "type";
        var value = "value";
        Timestamp timestamp = null;
        var data = new Data(id, type, value, timestamp);
        assertEquals("Data{" +
                "dataSetID='" + id + '\'' +
                ", sensorType='" + type + '\'' +
                ", value='" + value + '\'' +
                ", timestamp=" + timestamp +
                '}', data.toString());
    }

    @Test
    void setIdTest() {
        var data = new Data(null, null, null, null);
        assertNull(data.getId());
        data.setId("id");
        assertEquals("id", data.getId());
    }

    @Test
    void setTypeTest() {
        var data = new Data(null, null, null, null);
        assertNull(data.getType());
        data.setType("type");
        assertEquals("type", data.getType());
    }

    @Test
    void setValueTest() {
        var data = new Data(null, null, null, null);
        assertNull(data.getValue());
        data.setValue("value");
        assertEquals("value", data.getValue());
    }

    @Test
    void setTimestampTest() {
        var data = new Data(null, null, null, null);
        assertNull(data.getTimestamp());
        var timestamp = Timestamp.from(Instant.now());
        data.setTimestamp(timestamp);
        assertEquals(timestamp, data.getTimestamp());
    }
}