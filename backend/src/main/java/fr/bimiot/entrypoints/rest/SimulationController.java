package fr.bimiot.entrypoints.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bimiot/simulation")
public class SimulationController {
    /*private final LoadIfcFile loadIfcFile;

    @Autowired
    public SimulationController(LoadIfcFile loadIfcFile) {
        this.loadIfcFile = loadIfcFile;
    }

    @GetMapping("/files/{projectName}")
    public ResponseEntity<byte[]> loadFile(@PathVariable("projectName") String projectName) throws BaseException {
        byte[] fileContent = loadIfcFile.execute(projectName);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentLength(fileContent.length);
        return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
    }*/
}
