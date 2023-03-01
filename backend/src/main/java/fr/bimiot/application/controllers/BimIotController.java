package fr.bimiot.application.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.bimiot.application.dtos.ProjectApiGetAllResponse;
import fr.bimiot.domain.entities.Data;
import fr.bimiot.domain.entities.Project;
import fr.bimiot.domain.entities.Room;
import fr.bimiot.domain.use_cases.GetAllProjects;
import fr.bimiot.domain.use_cases.ManageData;
import fr.bimiot.domain.use_cases.ManageSimulation;
import fr.bimiot.simulator.ConverterEvent;
import fr.bimiot.utils.Builder;

@RestController
@RequestMapping("/api/bimiot")
public class BimIotController {
    private final GetAllProjects getAllProjects;
    private final ManageData manageData;
    private final ManageSimulation manageSimulation;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public BimIotController(GetAllProjects getAllProjects, ManageData manageData, ManageSimulation manageSimulation) {
        this.getAllProjects = getAllProjects;
        this.manageData = manageData;
        this.manageSimulation = manageSimulation;
    }

    @Deprecated
    @GetMapping("/projects")
    public ResponseEntity<List<ProjectApiGetAllResponse>> getAllProjects() {
        return ResponseEntity.status(HttpStatus.OK).body(getAllProjects.execute());
    }

    private ProjectApiGetAllResponse toProjectApiGetAllResponse(Project project){
        return Builder.of(ProjectApiGetAllResponse::new)
        .with(ProjectApiGetAllResponse::setName, project)
        .with(ProjectApiGetAllResponse, null);
    }

    @PutMapping(value = "/sendData", consumes = "application/json")
    public void sendData(@RequestBody Data data) {
        var event = new ConverterEvent(this, manageData.execute(data));
        applicationEventPublisher.publishEvent(event);
    }

    @PutMapping(value="/start/{simulation_name}")
    public int start(@PathVariable String simulation_name) {
        manageSimulation.executeStart(simulation_name);
        return 0;
    }

    @PutMapping(value="/stop/{simulation_name}")
    public int stop(@PathVariable String simulation_name) {
        manageSimulation.executeStop(simulation_name);
        return 0;
    }

    @PostMapping("/mapping")
    public int createMapping(@RequestBody List<Room> roomListDTO) {
        manageData.setRoomListDTO(roomListDTO);
        return 0;
    }
}
