package fr.bimiot.dataproviders.simulator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SimulationSimulatorTest {
    @Test
    void instantiate_shouldHasCorrectFieldValues() {
        var simulationSimulator = new SimulationSimulator("simulationId", "SimulationName");
        assertEquals("simulationId", simulationSimulator.id());
        assertEquals("SimulationName", simulationSimulator.name());
    }
}