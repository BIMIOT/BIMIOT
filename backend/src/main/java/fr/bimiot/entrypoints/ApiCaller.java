package fr.bimiot.entrypoints;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@RestController
@RequestMapping("/api/test")
public class ApiCaller {

    @GetMapping("/test")
    public String callApiEndpoint(String name) throws IOException {
        RestTemplate restTemplate = new RestTemplate();

        String filePath = "/home/etienne/Documents/Last Project/BIMIOT/Datasets/dataset-iot-1.json"; // absolute file path

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.set("File-Size", "1271");

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new FileSystemResource(filePath));

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://localhost:8090/api/sessions/create/Auto test", requestEntity, String.class);

        String dataset = responseEntity.getBody();
        System.out.println("Dataset created. Response: " + dataset);

        return dataset;
    }
}