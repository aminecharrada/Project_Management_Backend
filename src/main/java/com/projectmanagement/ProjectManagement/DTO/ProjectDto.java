package com.projectmanagement.ProjectManagement.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class ProjectDto {
    private Long id;
    private String title;
    private String description;
    private double progress;
    private double retardPercent;
    private String responsableName;
    private String responsableImage;
    private Long responsableId;
    private List<PoleDto> poles;
}

