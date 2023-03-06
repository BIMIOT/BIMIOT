package fr.bimiot.application.dtos;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProjectApiGetAllResponseTest {
    @Test
    void setterAndGetters_shouldHasCorrectFieldValues() {
        var dto = new ProjectApiGetAllResponse();
        dto.setName("ProjectX");
        dto.setIfcFilename("file.ifc");
        dto.setDatasetFilename("dataset.json");

        assertEquals("ProjectX", dto.getName());
        assertEquals("file.ifc", dto.getIfcFilename());
        assertEquals("dataset.json", dto.getDatasetFilename());
    }
}