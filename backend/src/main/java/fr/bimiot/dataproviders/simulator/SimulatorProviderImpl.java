package fr.bimiot.dataproviders.simulator;

import fr.bimiot.domain.use_cases.providers.SimulatorProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Objects;

@Component
public class SimulatorProviderImpl implements SimulatorProvider {

    @Value("${simulator.host}")
    private String simulatorHost;
    @Value("${simulator.port}")
    private Integer simulatorPort;
    private final RestTemplate restTemplate = new RestTemplate();
    private static final String SESSIONS_PATH = "/api/sessions/";
    private static final String CREATE_PATH = SESSIONS_PATH + "/create/";
    private static final String STOP_PATH = SESSIONS_PATH + "/stop/";
    private static final String START_PATH = SESSIONS_PATH + "/start/";
    private static final String PAUSE_PATH = SESSIONS_PATH + "/pause/";

    @Override
    public void start(String projectName) {
        restTemplate.put(getBaseUrl() + START_PATH + getSimulationId(projectName), "");
    }

    @Override
    public void stop(String projectName) {
        restTemplate.put(getBaseUrl() + STOP_PATH + getSimulationId(projectName), "");
    }

    @Override
    public void pause(String projectName) {
        restTemplate.put(getBaseUrl() + PAUSE_PATH + getSimulationId(projectName), "");
    }

    @Override
    public void updateAddress(String simulatorAddress, Integer simulatorPort) {
        this.simulatorHost = simulatorAddress;
        this.simulatorPort = simulatorPort;
    }

    @Override
    public void createSimulation(String projectName, MultipartFile file) {
        var requestEntity = getHttpEntity(file);
        restTemplate.postForEntity(getBaseUrl() + CREATE_PATH + projectName, requestEntity, String.class);
    }

    @Override
    public String getCompleteSimulatorAddress() {
        return getBaseUrl();
    }

    private HttpEntity<MultiValueMap<String, Object>> getHttpEntity(MultipartFile file) {
        var headers = getHeaders(file);
        var multiValueMap = getMultiValueMap(file);
        return new HttpEntity<>(multiValueMap, headers);
    }

    private HttpHeaders getHeaders(MultipartFile file) {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.set("File-Size", Long.toString(file.getSize()));
        return headers;
    }

    private MultiValueMap<String, Object> getMultiValueMap(MultipartFile file) {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", file.getResource());
        return body;
    }

    @Override
    public void deleteSimulation(String projectName) {
        restTemplate.delete(getBaseUrl() + SESSIONS_PATH + getSimulationId(projectName), "");
    }

    private String getBaseUrl() {
        return simulatorHost + ":" + simulatorPort;
    }

    private String getSimulationId(String projectName) {
        SimulationSimulator[] result = restTemplate.getForObject(getBaseUrl() + SESSIONS_PATH, SimulationSimulator[].class);
        if (Objects.isNull(result)) {
            throw new IllegalArgumentException("Simulation with name : " + projectName + " doesn't exist.");
        }
        var simulation = Arrays.stream(result).filter(s -> s.name().equalsIgnoreCase(projectName)).findFirst();
        if (simulation.isEmpty()) {
            throw new IllegalArgumentException("Simulation with name : " + projectName + " doesn't exist.");
        }
        return simulation.get().id();
    }
}
