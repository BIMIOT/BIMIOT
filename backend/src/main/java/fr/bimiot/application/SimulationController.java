package fr.bimiot.application;

import fr.bimiot.domain.use_cases.GetFile;
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
    private final GetFile getFile;

    @Autowired
    public SimulationController(GetFile getFile) {
        this.getFile = getFile;
    }

    @GetMapping("/files/{projectName}")
    public ResponseEntity<byte[]> loadFile(@PathVariable("projectName") String projectName) {
        byte[] filecontent = getFile.execute(projectName);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentLength(filecontent.length);
        return new ResponseEntity<>(filecontent, headers, HttpStatus.OK);
    }
}
