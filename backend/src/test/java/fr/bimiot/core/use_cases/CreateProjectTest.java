package fr.bimiot.core.use_cases;

import fr.bimiot.core.use_cases.providers.ProjectProvider;
import fr.bimiot.fixtures.ProjectFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class CreateProjectTest {
    @InjectMocks
    CreateProject createProject;
    @Mock
    ProjectProvider projectProvider;
    @Test
    void execute_shouldReturnCreatedProjectFromProvider() throws IOException {
        //  Given
        var project = ProjectFixture.aCompleteProject();
        BDDMockito.doReturn(project).when(projectProvider).save(ProjectFixture.aProjectWithoutSensorsAndWithoutId());
        //  When
        var result = createProject.execute(ProjectFixture.aProjectWithoutSensorsAndWithoutId());
        //  Then
        assertThat(result).isEqualTo(project);
        assertThat(result.getId()).isNotNull();
        assertThat(result.getSensorColors()).isNotNull();
    }
}