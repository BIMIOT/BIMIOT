package fr.bimiot.domain.entities;

import fr.bimiot.fixtures.RoomFixture;
import fr.bimiot.fixtures.SensorFixture;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RoomTest {
    @Test
    void toString_should() {
        var roomId = "roomID";
        var sensors = Set.of(SensorFixture.aSimpleSensor());
        var room = new Room(roomId, sensors);
        assertEquals("Room{" +
                "roomId='" + roomId + '\'' +
                ", sensors=" + sensors +
                '}', room.toString());
    }

    @Test
    void testHashCode() {
        var room = RoomFixture.aCompleteRoom();
        var room2 = RoomFixture.aCompleteRoom();
        assertEquals(room.hashCode(), room2.hashCode());
    }
}