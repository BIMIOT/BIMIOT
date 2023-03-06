package fr.bimiot.domain.use_cases.simulation;

import fr.bimiot.domain.use_cases.providers.SimulatorProvider;
import fr.bimiot.fixtures.ProjectFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CreateSimulationTest {

    @InjectMocks
    CreateSimulation createSimulation;
    @Mock
    SimulatorProvider simulatorProvider;

    @Test
    void execute_shouldCallCreateSimulationOfSimulatorProvider() {
        var projectName = ProjectFixture.aCompleteProject().getName();
        var dataset = new MockMultipartFile("file.json", "Hello".getBytes());

        createSimulation.execute(projectName, dataset);

        verify(simulatorProvider, times(1)).createSimulation(projectName, dataset);
    }
}