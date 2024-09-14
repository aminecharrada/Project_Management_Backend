package com.projectmanagement.ProjectManagement.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PoleDto {
    private Long id;
    private String poleName;
    private String elemPole;
    private String elemPoleImage;
    private Long personId;
    private List<TaskDto> tasks;
    private double poleProgress;
}
