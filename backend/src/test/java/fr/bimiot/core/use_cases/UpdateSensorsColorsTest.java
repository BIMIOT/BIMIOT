package fr.bimiot.core.use_cases;

import fr.bimiot.dataproviders.exception.DataBaseException;
import fr.bimiot.core.entities.Project;
import fr.bimiot.core.entities.SensorType;
import fr.bimiot.core.use_cases.providers.ProjectDatabaseProvider;
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
    ProjectDatabaseProvider projectDatabaseProvider;

    @Test
    void execute_shouldReturnSameProjectWithColorsSensors() throws DataBaseException {
        //  Given
        Project project = ProjectFixture.aProjectWithoutSensorsAndWithoutId();
        BDDMockito.doReturn(ProjectFixture.aCompleteProject())
                .when(projectDatabaseProvider)
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