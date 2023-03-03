package fr.bimiot.domain.use_cases;

import fr.bimiot.domain.entities.Project;
import fr.bimiot.domain.exception.DomainException;
import fr.bimiot.domain.use_cases.providers.ProjectProvider;
//import fr.bimiot.domain.use_cases.providers.ProjectFileProvider;
import fr.bimiot.fixtures.ProjectFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CreateProjectTest {
    @InjectMocks
    CreateProject createProject;

    @Mock
    ProjectProvider projectProvider;

    private static final String PROJECT_ID = "project_id";

    @Test
    void execute_shouldReturnProjectId() throws IOException, DomainException {
        Project project = ProjectFixture.aProjectWithoutSensorsAndWithoutId();
        BDDMockito.doReturn(PROJECT_ID).when(projectProvider).create(project);
        String result = createProject.execute(project);
        assertNotNull(result);
        assertEquals(PROJECT_ID, result);
    }

    @Test
    void execute_shouldThrowDomainException() {
        var project = ProjectFixture.aProjectWithoutSensorsAndWithoutId();
        BDDMockito.doReturn(true).when(projectProvider).isExistedProject(project.getName());
        var exception = assertThrows(DomainException.class, () -> createProject.execute(project));
        assertNotNull(exception);
        assertEquals("Project '" + project.getName() + "' already exists !", exception.getMessage());
        assertEquals("400", exception.getCode());
    }
}