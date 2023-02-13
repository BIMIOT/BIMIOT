package fr.bimiot.domain.use_cases;

import fr.bimiot.domain.entities.Project;
import fr.bimiot.domain.exception.DomainException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CreateProjectTest {
    @InjectMocks
    CreateProject createProject;

    @Mock
    ProjectPort projectPort;

    private final static Path PROJECTS_FOLDER = Paths.get("Projects");
    private final static Path TEST_PROJECT = PROJECTS_FOLDER.resolve("test");


    @Test
    public void givenProjectName_whenCreateProject_thenCreateDirectoryWithProjecName() throws DomainException, IOException {
        //  Given
        Project project = new Project("test");

        //  When
        createProject.createFolder(project);

        //  Then
        assertTrue(Files.isDirectory(TEST_PROJECT));
        Files.delete(TEST_PROJECT);
    }
}