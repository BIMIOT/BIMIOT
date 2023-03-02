package fr.bimiot.application.controllers;

import fr.bimiot.application.dtos.ProjectApiGetAllResponse;
import fr.bimiot.domain.entities.Data;
import fr.bimiot.domain.entities.Project;
import fr.bimiot.domain.entities.Room;
import fr.bimiot.domain.use_cases.GetAllProjects;
import fr.bimiot.domain.use_cases.ManageData;
import fr.bimiot.domain.use_cases.ManageSimulation;
import fr.bimiot.simulator.ConverterEvent;
import fr.bimiot.utils.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        var responseBody = getAllProjects.execute().stream()
                .map(this::toProjectApiGetAllResponse)
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    private ProjectApiGetAllResponse toProjectApiGetAllResponse(Project project) {
        return Builder.of(ProjectApiGetAllResponse::new)
                .with(ProjectApiGetAllResponse::setName, project.getName())
                .with(ProjectApiGetAllResponse::setIfcFilename, project.getIfcFilename())
                .with(ProjectApiGetAllResponse::setDatasetFilename, project.getDatasetFilename())
                .build();
    }

    @PutMapping(value = "/sendData", consumes = "application/json")
    public void sendData(@RequestBody Data data) {
        var optData = manageData.execute(data);
        if (optData.isPresent()) {
            var event = new ConverterEvent(this, optData.get());
            applicationEventPublisher.publishEvent(event);
        }
    }

    @PutMapping(value = "/start/{simulation_name}")
    public int start(@PathVariable String simulation_name) {
        manageSimulation.executeStart(simulation_name);
        return 0;
    }

    @PutMapping(value="/pause/{simulation_name}")
    public int pause(@PathVariable String simulation_name) {
        manageSimulation.executePause(simulation_name);
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
