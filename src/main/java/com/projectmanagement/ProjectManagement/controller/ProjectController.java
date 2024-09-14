package com.projectmanagement.ProjectManagement.controller;

import com.projectmanagement.ProjectManagement.DTO.ProjectDto;
import com.projectmanagement.ProjectManagement.entity.Personne;
import com.projectmanagement.ProjectManagement.entity.Project;
import com.projectmanagement.ProjectManagement.repository.ProjectRepository;
import com.projectmanagement.ProjectManagement.service.PersonneService;
import com.projectmanagement.ProjectManagement.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private PersonneService personneService;
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    public ResponseEntity<List<Project>> getAllProjects() {
        List<Project> projects = projectService.getAllProjects();
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable Long id) {
        Project project = projectService.getProjectById(id);
        return ResponseEntity.ok(project);
    }

    @PostMapping
    public ResponseEntity<?> createProject(@RequestBody ProjectDto projectDto) {
        Personne responsable = personneService.getPersonneById(projectDto.getResponsableId());
        if (responsable == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Responsible person not found");
        }

        Project newProject = new Project();
        newProject.setTitle(projectDto.getTitle());
        newProject.setDescription(projectDto.getDescription());
        newProject.setProgress(projectDto.getProgress());
        newProject.setRetardPercent(projectDto.getRetardPercent());
        newProject.setResponsableName(responsable.getName());
        newProject.setResponsableImage(responsable.getImage());

        projectRepository.save(newProject);
        return ResponseEntity.ok(newProject);
    }
}
