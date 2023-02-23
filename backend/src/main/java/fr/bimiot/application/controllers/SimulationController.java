package fr.bimiot.application.controllers;

import fr.bimiot.domain.use_cases.providers.ProjectDatabaseProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bimiot/simulation")
public class SimulationController {
    private final ProjectDatabaseProvider projectDatabaseProvider;

    @Autowired
    public SimulationController( ProjectDatabaseProvider projectDatabaseProvider) {
        this.projectDatabaseProvider = projectDatabaseProvider;
    }

    @GetMapping("/files/{projectName}")
    public ResponseEntity<byte[]> loadFile(@PathVariable("projectName") String projectName) {
        byte[] fileContent = projectDatabaseProvider.loadFile(projectName);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentLength(fileContent.length);
        return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
    }
}
