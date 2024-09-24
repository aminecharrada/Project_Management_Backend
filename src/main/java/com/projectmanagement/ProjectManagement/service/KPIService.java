package com.projectmanagement.ProjectManagement.service;
import com.projectmanagement.ProjectManagement.entity.Project;
import com.projectmanagement.ProjectManagement.entity.Task;
import com.projectmanagement.ProjectManagement.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class KPIService {

    @Autowired
    private ProjectRepository projectRepository;

    public Map<String, Double> calculateRespectDesDelaisPerDate(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        List<Task> tasks = project.getTasks();
        if (tasks.isEmpty()) {
            return Collections.emptyMap(); // Return empty if no tasks exist
        }

        // Group tasks by their start date
        Map<String, List<Task>> tasksByDate = tasks.stream()
                .collect(Collectors.groupingBy(Task::getStart_date));

        // Calculate respect des délais for each date
        Map<String, Double> respectDesDelaisPerDate = new HashMap<>();
        for (Map.Entry<String, List<Task>> entry : tasksByDate.entrySet()) {
            String date = entry.getKey();
            List<Task> tasksForDate = entry.getValue();

            // Calculate the percentage of tasks completed on that date
            long finishedTasks = tasksForDate.stream()
                    .filter(task -> task.getProgress() == 1.0)
                    .count();

            double respectDesDelais = ((double) finishedTasks / tasksForDate.size()) * 100;
            respectDesDelaisPerDate.put(date, respectDesDelais);
        }

        return respectDesDelaisPerDate; // Map of date -> respect des délais
    }
}
