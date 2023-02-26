package fr.bimiot.entrypoints.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FindAllProjectsResponse {
    private String id;
    private String name;
}
