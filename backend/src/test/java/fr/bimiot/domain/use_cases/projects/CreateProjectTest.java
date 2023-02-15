package fr.bimiot.domain.use_cases.projects;

import fr.bimiot.domain.entities.Project;
import fr.bimiot.domain.exception.DomainException;
import fr.bimiot.domain.use_cases.providers.ProjectDatabaseProvider;
import fr.bimiot.domain.use_cases.providers.ProjectFileProvider;
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
    ProjectFileProvider projectFileProvider;

    @Mock
    ProjectDatabaseProvider projectDatabaseProvider;

    private static final String PROJECT_ID = "project_id";

    @Test
    void execute_shouldReturnProjectId() throws IOException, DomainException {
        Project project = new Project(null, "Project 1", null, null);
        BDDMockito.doReturn(PROJECT_ID).when(projectDatabaseProvider).create(project);
        String result = createProject.execute(project);
        assertNotNull(result);
        assertEquals(PROJECT_ID, result);
    }
}