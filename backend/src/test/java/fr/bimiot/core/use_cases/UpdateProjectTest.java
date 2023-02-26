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
class UpdateProjectTest {

    @InjectMocks
    UpdateProject updateProject;
    @Mock
    ProjectProvider projectProvider;

    @Test
    void execute_shouldReturnUpdatedProjectFromProvider() throws IOException {
        //  Given
        var updatedProject = ProjectFixture.aCompleteProject();
        BDDMockito.doReturn(updatedProject).when(projectProvider).save(updatedProject);
        //  When
        var result = updateProject.execute(updatedProject);
        //  Then
        assertThat(result).isEqualTo(updatedProject);
    }
}