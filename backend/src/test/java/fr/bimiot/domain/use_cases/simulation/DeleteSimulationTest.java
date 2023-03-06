package fr.bimiot.domain.use_cases.simulation;

import fr.bimiot.domain.use_cases.providers.SimulatorProvider;
import fr.bimiot.fixtures.ProjectFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DeleteSimulationTest {
    @InjectMocks
    DeleteSimulation deleteSimulation;
    @Mock
    SimulatorProvider simulatorProvider;

    @Test
    void execute_shouldCallDeleteSimulationOfSimulatorProvider() {
        var projectName = ProjectFixture.aCompleteProject().getName();
        deleteSimulation.execute(projectName);
        verify(simulatorProvider, times(1)).deleteSimulation(projectName);
    }
}