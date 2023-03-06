package fr.bimiot.application.controllers;

import fr.bimiot.domain.entities.Data;
import fr.bimiot.domain.entities.Room;
import fr.bimiot.domain.use_cases.ManageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bimiot")
public class BimIotController {
    private final ManageData manageData;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public BimIotController(ManageData manageData) {
        this.manageData = manageData;
    }

    @PutMapping(value = "/sendData", consumes = "application/json")
    public void sendData(@RequestBody Data data) {
        manageData.execute(data);
    }

    @PostMapping("/mapping")
    public void createMapping(@RequestBody List<Room> roomListDTO) {
        manageData.setRoomListDTO(roomListDTO);
    }

    @PutMapping("/reset")
    public void resetValues() {
        manageData.resetRoomListDTO();
    }
}
