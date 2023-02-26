package fr.bimiot.core.use_cases;

import fr.bimiot.core.use_cases.providers.ProjectProvider;
import fr.bimiot.fixtures.ProjectFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DeleteProjectTest {
    @InjectMocks
    DeleteProject deleteProject;
    @Mock
    ProjectProvider projectProvider;

    @Test
    void execute_shouldCallDeleteByIdOfProvider() {
        //  Given
        var id = ProjectFixture.aCompleteProject().getId();
        //  When
        deleteProject.execute(id);
        //  Then
        verify(projectProvider, times(1)).deleteById(id);
    }
}