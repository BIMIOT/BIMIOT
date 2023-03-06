package fr.bimiot.application.controllers;

import fr.bimiot.application.dtos.UpdateSimulationSettingsRequest;
import fr.bimiot.application.exception.type.BaseException;
import fr.bimiot.domain.exception.DomainException;
import fr.bimiot.domain.use_cases.*;
import fr.bimiot.domain.use_cases.simulation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bimiot/simulations")
public class SimulationController {
    private final LoadIfcFile loadIfcFile;
    private final StartSimulation startSimulation;
    private final StopSimulation stopSimulation;
    private final PauseSimulation pauseSimulation;
    private final UpdateSimulationSettings updateSimulationSettings;

    @Autowired
    public SimulationController(LoadIfcFile loadIfcFile, StartSimulation startSimulation, StopSimulation stopSimulation, PauseSimulation pauseSimulation, UpdateSimulationSettings updateSimulationSettings) {
        this.loadIfcFile = loadIfcFile;
        this.startSimulation = startSimulation;
        this.stopSimulation = stopSimulation;
        this.pauseSimulation = pauseSimulation;
        this.updateSimulationSettings = updateSimulationSettings;
    }

    @PutMapping("/start/{projectName}")
    public void startSimulation(@PathVariable String projectName) throws DomainException {
        startSimulation.execute(projectName);
    }

    @PutMapping("/stop/{projectName}")
    public void stopSimulation(@PathVariable String projectName) throws DomainException {
        stopSimulation.execute(projectName);
    }

    @PutMapping("/pause/{projectName}")
    public void pauseSimulation(@PathVariable String projectName) throws DomainException {
        pauseSimulation.execute(projectName);
    }

    @PutMapping
    public void updateSettings(@RequestBody UpdateSimulationSettingsRequest updateSimulationSettingsRequest) throws DomainException {
        updateSimulationSettings.execute(updateSimulationSettingsRequest.getHost(), updateSimulationSettingsRequest.getPort());
    }


    @GetMapping("/files/{projectName}")
    public ResponseEntity<byte[]> loadFile(@PathVariable String projectName) throws BaseException {
        byte[] fileContent = loadIfcFile.execute(projectName);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentLength(fileContent.length);
        return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
    }
}
