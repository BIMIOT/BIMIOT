package fr.bimiot.domain.entities;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SimulationTest {
    @Test
    void instantiate_shouldHasCorrectFieldValues() {
        var simulation = new Simulation("id", "name");
        assertEquals("id", simulation.id());
        assertEquals("name", simulation.name());
    }
}