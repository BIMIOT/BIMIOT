package fr.bimiot.domain.use_cases;

import fr.bimiot.dataproviders.exception.DataBaseException;
import fr.bimiot.domain.entities.Project;
import fr.bimiot.domain.entities.SensorType;
import fr.bimiot.domain.use_cases.providers.ProjectProvider;
import fr.bimiot.fixtures.ProjectFixture;
import fr.bimiot.fixtures.SensorColorMapFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UpdateSensorsColorsTest {
    @InjectMocks
    UpdateSensorsColors updateSensorsColors;

    @Mock
    ProjectProvider projectProvider;

    @Test
    void execute_shouldReturnSameProjectWithColorsSensors() throws DataBaseException {
        //  Given
        Project project = ProjectFixture.aProjectWithoutSensorsAndWithoutId();
        BDDMockito.doReturn(ProjectFixture.aCompleteProject())
                .when(projectProvider)
                .updateSensorsColorsByProjectName(project.getName(), SensorColorMapFixture.sensorTypeListMapDomain());
        //  When
        Project result = updateSensorsColors.execute(project.getName(), SensorColorMapFixture.sensorTypeListMapDomain());

        //  Then
        assertNotNull(result);
        assertEquals(project, result);
        assertNotNull(result.getSensorColors());
        assertEquals(4, result.getSensorColors().size());
        assertTrue(result.getSensorColors().containsKey(SensorType.TEMPERATURE));
        assertTrue(result.getSensorColors().containsKey(SensorType.CO2));
        assertTrue(result.getSensorColors().containsKey(SensorType.LIGHT));
        assertTrue(result.getSensorColors().containsKey(SensorType.HUMIDITY));
    }

}