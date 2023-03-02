package fr.bimiot.domain.use_cases;

import fr.bimiot.domain.entities.Simulation;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

@Component
public class ManageSimulation {
    private static final String URL = "http://188.166.194.147";
    private final RestTemplate restTemplate = new RestTemplate();

    public void executeStart(String simulation_name) {
        startSimulation(getSimulationId(simulation_name));
    }

    public void executeStop(String simulation_name) {
        stopSimulation(getSimulationId(simulation_name));
    }

    public void executePause(String simulation_name) {
        pauseSimulation(getSimulationId(simulation_name));
    }

    public void executeCreate(String simulation_name, MultipartFile dataset) {
        createSimulation(simulation_name, dataset);
    }
    public void executeDelete(String simulation_name) {
        deleteSimulation(getSimulationId(simulation_name));
    }

    private void deleteSimulation(String simulation_id) {
        restTemplate.delete(URL + "/api/sessions/" + simulation_id, "");
        System.out.println("deleted simulation " + simulation_id);
    }

    private void createSimulation(String simulation_name, MultipartFile dataset) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.set("File-Size", Long.toString(dataset.getSize()));

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", dataset.getResource());

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        restTemplate.postForEntity(URL + "/api/sessions/create/" + simulation_name, requestEntity, String.class);
        System.out.println("created simulation " + simulation_name);
    }

    private void stopSimulation(String simulation_id) {
        restTemplate.put(URL + "/api/sessions/stop/" + simulation_id, "");
        System.out.println("stopped simulation " + simulation_id);
    }

    private void startSimulation(String simulation_id) {
        restTemplate.put(URL + "/api/sessions/start/" + simulation_id, "");
        System.out.println("started simulation " + simulation_id);
    }

    private void pauseSimulation(String simulation_id) {
        restTemplate.put(URL + "/api/sessions/pause/" + simulation_id, "");
        System.out.println("paused simulation " + simulation_id);
    }

    private String getSimulationId(String simulation_name) {
        Simulation[] result = restTemplate.getForObject(URL + "/api/sessions", Simulation[].class);
        if (result == null) {
            throw new IllegalArgumentException("Simulation with name : " + simulation_name + " doesn't exist.");
        }
        var simulation = Arrays.stream(result).filter(s -> s.name().equalsIgnoreCase(simulation_name)).findFirst();
        if (simulation.isEmpty()) {
            throw new IllegalArgumentException("Simulation with name : " + simulation_name + " doesn't exist.");
        }
        return simulation.get().id();
    }
}
