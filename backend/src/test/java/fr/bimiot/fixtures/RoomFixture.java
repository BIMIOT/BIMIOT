package fr.bimiot.fixtures;

import fr.bimiot.domain.entities.Room;
import fr.bimiot.utils.Builder;

import java.util.Set;

public class RoomFixture {
    public static Room aCompleteRoom() {
        return Builder.of(Room::new)
                .with(Room::setRoomId, "roomId")
                .with(Room::setSensors, Set.of(SensorFixture.aSimpleSensor()))
                .build();
    }
}
