package fr.bimiot.domain.use_cases;

import fr.bimiot.domain.entities.Simulation;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Component
public class StartSimulation {
    public void execute(String simulation_name) {
        System.out.println(getSimulationId(simulation_name));
    }

    private String getSimulationId(String simulation_name) {
        RestTemplate restTemplate = new RestTemplate();
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
