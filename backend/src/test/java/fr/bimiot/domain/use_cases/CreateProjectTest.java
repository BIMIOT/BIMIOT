package fr.bimiot.domain.use_cases;

import fr.bimiot.domain.use_cases.providers.ProjectDatabaseProvider;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateProjectTest {
    @InjectMocks
    CreateProject createProject;

    @Mock
    ProjectDatabaseProvider projectDatabaseProvider;

    private static final String PROJECT_ID = "project_id";
/*
    @Test
    void execute_shouldReturnProjectId() throws IOException, DomainException {
        Project project = ProjectFixture.aProjectWithoutSensorsAndWithoutId();
        BDDMockito.doReturn(PROJECT_ID).when(projectDatabaseProvider).create(project);
        String result = createProject.execute(project);
        assertNotNull(result);
        assertEquals(PROJECT_ID, result);
    }*/
}