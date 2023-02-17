package fr.bimiot.domain.use_cases;

import fr.bimiot.domain.entities.Simulation;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Component
public class ManageSimulation {
    private final RestTemplate restTemplate = new RestTemplate();

    public void executeStart(String simulation_name) {
        startSimulation(getSimulationId(simulation_name));
    }

    public void executeStop(String simulation_name) {
        stopSimulation(getSimulationId(simulation_name));
    }

    private void stopSimulation(String simulation_id) {
        restTemplate.put("http://host.docker.internal:8083/v1/sessions/stop/" + simulation_id, "");
        System.out.println("stopped simulation " + simulation_id);
    }

    private void startSimulation(String simulation_id) {
        restTemplate.put("http://host.docker.internal:8083/v1/sessions/start/" + simulation_id, "");
        System.out.println("started simulation " + simulation_id);
    }

    private String getSimulationId(String simulation_name) {
        Simulation[] result = restTemplate.getForObject("http://host.docker.internal:8083/v1/sessions", Simulation[].class);
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
