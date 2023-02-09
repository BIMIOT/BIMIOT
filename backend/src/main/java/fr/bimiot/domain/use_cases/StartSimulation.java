package fr.bimiot.domain.use_cases;

import fr.bimiot.domain.entities.Simulation;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Component
public class StartSimulation {
    private final RestTemplate restTemplate = new RestTemplate();

    public void execute(String simulation_name) {
        startSimulation(getSimulationId(simulation_name));
    }

    private void startSimulation(String simulation_id) {
        restTemplate.put("http://localhost:8083/v1/sessions/start/" + simulation_id, "");
        System.out.println("started simulation");
    }

    private String getSimulationId(String simulation_name) {
        Simulation[] result = restTemplate.getForObject("http://localhost:8083/v1/sessions", Simulation[].class);
        if (result == null) {
            throw new IllegalArgumentException("Simulation with name : " + simulation_name + " doesn't exist.");
        }
        var simulation = Arrays.stream(result).filter(s -> !s.id().equals(simulation_name)).findFirst();
        if (simulation.isEmpty()) {
            throw new IllegalArgumentException("Simulation with name : " + simulation_name + " doesn't exist.");
        }
        return simulation.get().id();
    }
}
