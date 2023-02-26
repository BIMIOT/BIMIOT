package fr.bimiot.dataproviders.database;

import fr.bimiot.fixtures.ProjectFixture;
import fr.bimiot.fixtures.ProjectJpaFixture;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProjectDatabaseProviderTest {

    @InjectMocks
    ProjectDatabaseProvider projectDatabaseProvider;
    @Mock
    ProjectJpaRepository projectJpaRepository;

    @Test
    void save_shouldReturnCreatedOrUpdatedProjectFromRepository() throws IOException {
        //  Given
        var projectJpa = ProjectJpaFixture.aCompleteProjectJpa();
        var projectJpaWithoutId = ProjectJpaFixture.aCompleteProjectJpa();
        projectJpaWithoutId.setId(null);
        BDDMockito.doReturn(projectJpa).when(projectJpaRepository).save(projectJpaWithoutId);
        var projectWithoutId = ProjectFixture.aProjectWithoutSensorsAndWithoutId();
        //  When
        var result = projectDatabaseProvider.save(projectWithoutId);
        //  Then
        assertThat(result.getId()).isEqualTo(projectJpa.getId());
        assertThat(result.getName()).isEqualTo(projectJpa.getName());
        assertThat(result.getIfc()).isEqualTo(projectJpa.getIfc().getData());
    }

    @Test
    void deleteById_shouldCallDeleteByIdOfRepository() {
        //  Given
        var id = ProjectJpaFixture.aCompleteProjectJpa().getId();
        //  When
        projectDatabaseProvider.deleteById(id);
        //  Then
        verify(projectJpaRepository, times(1)).deleteById(id);
    }

    @Test
    void findById_shouldReturnProjectWithIdFromRepository() {
        //  Given
        var projectJpa = ProjectJpaFixture.aCompleteProjectJpa();
        BDDMockito.doReturn(Optional.of(projectJpa)).when(projectJpaRepository).findById(projectJpa.getId());
        //  When
        var optionalResult = projectDatabaseProvider.findById(projectJpa.getId());
        //  Then
        assertThat(optionalResult).isPresent();
        var result = optionalResult.get();
        assertThat(result.getIfc()).isEqualTo(projectJpa.getIfc().getData());
        assertThat(result.getId()).isEqualTo(projectJpa.getId());
        assertThat(result.getName()).isEqualTo(projectJpa.getName());
    }

    @Test
    void findAll_shouldReturnProjectsFromRepository() {
        //  Given
        var projectJpa = ProjectJpaFixture.aCompleteProjectJpa();
        var projectJpas = Lists.newArrayList(projectJpa);
        BDDMockito.doReturn(projectJpas).when(projectJpaRepository).findAll();
        //  When
        var results = projectDatabaseProvider.findAll();
        //  Then
        var project = results.get(0);
        assertThat(project.getId()).isEqualTo(projectJpa.getId());
        assertThat(project.getName()).isEqualTo(projectJpa.getName());
        assertThat(project.getIfc()).isEqualTo(projectJpa.getIfc().getData());
    }
}