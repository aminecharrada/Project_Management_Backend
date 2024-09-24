package com.projectmanagement.ProjectManagement.service;
import com.projectmanagement.ProjectManagement.entity.Project;
import com.projectmanagement.ProjectManagement.entity.Task;
import com.projectmanagement.ProjectManagement.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

    public Map<String, Double> calculateDailyProgress(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        List<Task> tasks = project.getTasks();
        if (tasks.isEmpty()) {
            return Collections.emptyMap(); // Return empty if no tasks exist
        }

        Map<String, Double> dailyProgress = new HashMap<>();

        // Calculate progress for each day within the project's duration
        LocalDate startDate = tasks.stream()
                .map(Task::getStart_date)
                .map(date -> LocalDate.parse(date.split(" ")[0], DateTimeFormatter.ofPattern("yyyy-MM-dd"))) // Extract date part
                .min(LocalDate::compareTo)
                .orElse(LocalDate.now());

        LocalDate endDate = tasks.stream()
                .map(Task::getStart_date)
                .map(date -> LocalDate.parse(date.split(" ")[0], DateTimeFormatter.ofPattern("yyyy-MM-dd"))) // Extract date part
                .max(LocalDate::compareTo)
                .orElse(LocalDate.now());

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            double totalProgress = 0;
            int totalTasks = 0;

            for (Task task : tasks) {
                LocalDate taskStartDate = LocalDate.parse(task.getStart_date().split(" ")[0], DateTimeFormatter.ofPattern("yyyy-MM-dd")); // Extract date part
                LocalDate taskEndDate = taskStartDate.plusDays(task.getDuration());

                if ((date.isEqual(taskStartDate) || date.isAfter(taskStartDate)) && date.isBefore(taskEndDate)) {
                    totalProgress += task.getProgress();
                    totalTasks++;
                }
            }

            if (totalTasks > 0) {
                dailyProgress.put(date.toString(), (totalProgress / totalTasks) * 100);
            }
        }

        return dailyProgress;
    }
}