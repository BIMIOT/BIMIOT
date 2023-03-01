package fr.bimiot.dataproviders.database;

import fr.bimiot.dataproviders.exception.DataBaseException;
import fr.bimiot.domain.entities.Project;
import fr.bimiot.fixtures.ProjectJpaFixture;
import fr.bimiot.fixtures.SensorColorMapFixture;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProjectDatabaseProviderTest {
    @InjectMocks
    ProjectDatabaseProvider projectDatabaseProvider;

    @Mock
    ProjectJpaRepository projectJpaRepository;

    private static final MultipartFile IFC_FILE = new MockMultipartFile("ifc", "Hello".getBytes());

    private static final MultipartFile DATASET_FILE = new MockMultipartFile("dataset", "Hello".getBytes());

    @Test
    void create_shouldInsertByUsingJpaRepository() throws IOException {
        //  Given
        Project project = new Project(null, "project 1", IFC_FILE, DATASET_FILE);
        ArgumentCaptor<ProjectJpa> projectJpaArgumentCaptor = ArgumentCaptor.forClass(ProjectJpa.class);
        ProjectJpa projectJpaInput = new ProjectJpa();
        projectJpaInput.setName(project.getName());
        projectJpaInput.setIfc(toBinary(IFC_FILE));
        projectJpaInput.setDataset(toBinary(DATASET_FILE));

        ProjectJpa projectJpaOutput = new ProjectJpa();
        projectJpaOutput.setId("projectId");
        projectJpaOutput.setName(project.getName());

        BDDMockito.doReturn(projectJpaOutput).when(projectJpaRepository).insert(projectJpaInput);

        //  When
        String projectId = projectDatabaseProvider.create(project);

        //  Then
        verify(projectJpaRepository).insert(projectJpaArgumentCaptor.capture());
        assertEquals(project.getName(), projectJpaArgumentCaptor.getValue().getName());
        assertEquals("projectId", projectId);
    }


    @Test
    void find_shouldFoundByProjectName() throws IOException {
        //Given
        Project project = new Project(null, "project1", IFC_FILE, DATASET_FILE);
        ProjectJpa projectJpaInput = new ProjectJpa();
        projectJpaInput.setName(project.getName());
        projectJpaInput.setIfc(toBinary(IFC_FILE));
        projectJpaInput.setDataset(toBinary(DATASET_FILE));

        long toReturn = 1;

        BDDMockito.doReturn(toReturn).when(projectJpaRepository).deleteByName(projectJpaInput.getName());

        //When
        var result = projectJpaRepository.deleteByName("project1");

        //Then
        assertEquals(1, result);
    }

    @Test
    void updateSensorsColors_shouldUpdateByUsingJpaRepository() throws DataBaseException {
        //  Given
        ArgumentCaptor<String> projectNameCaptor = ArgumentCaptor.forClass(String.class);

        ProjectJpa findByNameOutput = ProjectJpaFixture.aProjectJpaWithoutSensors();
        ProjectJpa saveOutput = ProjectJpaFixture.aCompleteProjectJpa();

        BDDMockito.doReturn(findByNameOutput).when(projectJpaRepository).findProjectJpaByName("Project X");
        BDDMockito.doReturn(saveOutput).when(projectJpaRepository).save(ProjectJpaFixture.aCompleteProjectJpa());

        //  When
        Project result = projectDatabaseProvider.updateSensorsColorsByProjectName("Project X", SensorColorMapFixture.sensorTypeListMapDomain());

        //  Then
        verify(projectJpaRepository).findProjectJpaByName(projectNameCaptor.capture());
        assertEquals("Project X", projectNameCaptor.getValue());
        assertEquals("ProjectID", result.getId());
        assertNotNull(result.getSensorColors());
    }

    @Test
    void updateSensorsColorsNoExistedProject_shouldThrowDomainException() {
        //  Given
        String projectName = "Project XX";
        BDDMockito.doReturn(null).when(projectJpaRepository).findProjectJpaByName(projectName);
        //  When
        Exception exception = assertThrows(DataBaseException.class, () -> projectDatabaseProvider.updateSensorsColorsByProjectName(projectName, SensorColorMapFixture.sensorTypeListMapDomain()));
        //  Then
        assertNotNull(exception);
        assertEquals("Project doesn't exist", exception.getMessage());
    }

    private Binary toBinary(MultipartFile file) throws IOException {
        return new Binary(BsonBinarySubType.BINARY, file.getBytes());
    }
}