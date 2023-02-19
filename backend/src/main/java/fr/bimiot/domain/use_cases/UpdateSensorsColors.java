package fr.bimiot.domain.use_cases;

import fr.bimiot.dataproviders.exception.DataBaseException;
import fr.bimiot.domain.entities.Project;
import fr.bimiot.domain.entities.TypesColors;
import fr.bimiot.domain.use_cases.providers.ProjectDatabaseProvider;
import org.springframework.stereotype.Component;

@Component
public class UpdateSensorsColors {
    private final ProjectDatabaseProvider projectDatabaseProvider;

    public UpdateSensorsColors(ProjectDatabaseProvider projectDatabaseProvider) {
        this.projectDatabaseProvider = projectDatabaseProvider;
    }

    public Project execute(String projectName, TypesColors typesColors) throws DataBaseException {
        return projectDatabaseProvider.updateSensorsColorsByProjectName(projectName, typesColors);
    }
}
