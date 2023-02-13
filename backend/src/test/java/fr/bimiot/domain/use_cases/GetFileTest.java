package fr.bimiot.domain.use_cases;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class GetFileTest {

    @InjectMocks
    GetFile getFile;

    private final static Path PROJECTS_FOLDER = Paths.get("Projects");
    private final static Path TEST_PROJECT = PROJECTS_FOLDER.resolve("test");
    private final static Path TEST_FILE = TEST_PROJECT.resolve("test.ifc");

    @Test
    public void testExecute() throws IOException {
        byte[] expected = "test".getBytes();
        Files.createDirectories(TEST_PROJECT);
        Files.write(TEST_FILE, expected);

        byte[] result = getFile.execute("test");
        assertArrayEquals(expected, result);

        Files.delete(TEST_FILE);
        Files.delete(TEST_PROJECT);
    }

    @Test
    public void testExecute_FileNotFound() {
        GetFile getFile = new GetFile();
        assertThrows(AssertionError.class, () -> getFile.execute("not-found"));
    }
}