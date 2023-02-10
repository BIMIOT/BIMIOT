package fr.bimiot.domain.use_cases;

import fr.bimiot.domain.entities.ProjectDirectory;
import fr.bimiot.domain.exception.DomainException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CreateProjectTest {
    @InjectMocks
    CreateProject createProject;

    @BeforeAll
    public void init() throws IOException {
        Files.createDirectory(Paths.get("Projects"));
        Files.createDirectory(Paths.get("Projects/tests"));
    }

    @AfterAll
    public void afterAll() throws IOException {
        Files.walkFileTree(Paths.get("Projects"), new SimpleFileVisitor<>(){
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.deleteIfExists(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.deleteIfExists(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    @Test
    public void givenProjectName_whenCreateProject_thenCreateDirectoryWithProjecName() throws DomainException {
        //  Given
        ProjectDirectory projectDirectory = new ProjectDirectory("tests/Bim");

        //  When
        createProject.createFolder(projectDirectory);

        //  Then
        assertTrue(Files.isDirectory(Paths.get("Projects/" + projectDirectory.name())));
    }
}