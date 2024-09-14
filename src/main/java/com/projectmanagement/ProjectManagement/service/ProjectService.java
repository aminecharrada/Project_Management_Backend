package com.projectmanagement.ProjectManagement.service;
import com.projectmanagement.ProjectManagement.DTO.ProjectDto;
import com.projectmanagement.ProjectManagement.entity.Personne;
import com.projectmanagement.ProjectManagement.entity.Project;
import com.projectmanagement.ProjectManagement.repository.PoleRepository;
import com.projectmanagement.ProjectManagement.repository.ProjectRepository;
//import com.projectmanagement.ProjectManagement.repository.SubTaskRepository;
import com.projectmanagement.ProjectManagement.repository.TaskRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

//import com.projectmanagement.ProjectManagement.entity.SubTask;
@Service
public class ProjectService {

    @Autowired
    private final ProjectRepository projectRepository;
    private final PoleRepository poleRepository;
    private final TaskRepository taskRepository;
    @Autowired
    private PersonneService personneService;

    public ProjectService(ProjectRepository projectRepository, PoleRepository poleRepository, TaskRepository taskRepository) {
        this.projectRepository = projectRepository;
        this.poleRepository = poleRepository;
        this.taskRepository = taskRepository;
    }


    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }


    public Project getProjectById(Long id) {
        return projectRepository.findById(id).orElse(null);
    }


        public Project createProject(ProjectDto projectDto) {
            Project project = new Project();
            project.setTitle(projectDto.getTitle());
            project.setDescription(projectDto.getDescription());
            project.setProgress(0.0); // Set progress to 0 initially
            project.setRetardPercent(0.0); // Set retardPercent to 0 initially

            // Fetch the selected person from the database
            Personne responsable = personneService.getPersonneById(projectDto.getResponsableId());
            if (responsable != null) {
                project.setResponsableName(responsable.getName());
                project.setResponsableImage(responsable.getImage());
            }

            return projectRepository.save(project);
        }

}
