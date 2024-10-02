package com.projectmanagement.ProjectManagement.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
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
    private LocalDate startDate;
    private LocalDate endDate;
    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
    private List<PoleDto> poles;
}

