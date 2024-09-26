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
    public Map<String, Double> calculateDailyProductivity(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        List<Task> tasks = project.getTasks();
        Map<String, Double> dailyProductivity = new HashMap<>();

        LocalDate startDate = tasks.stream()
                .map(Task::getStart_date)
                .map(date -> LocalDate.parse(date.split(" ")[0], DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .min(LocalDate::compareTo)
                .orElse(LocalDate.now());

        LocalDate endDate = tasks.stream()
                .map(Task::getStart_date)
                .map(date -> LocalDate.parse(date.split(" ")[0], DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .max(LocalDate::compareTo)
                .orElse(LocalDate.now());

        // Iterate through each date in the project duration
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            double totalProductivityForDate = 0.0;

            for (Task task : tasks) {
                LocalDate taskStartDate = LocalDate.parse(task.getStart_date().split(" ")[0], DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                LocalDate taskEndDate = taskStartDate.plusDays(task.getDuration());

                // If the task is ongoing on the current date
                if (!date.isBefore(taskStartDate) && !date.isAfter(taskEndDate)) {
                    // Calculate the productivity for the task on the given day
                    double productivity = 1.0 * (task.getProgress() * 100);  // 1 day * % of task completion
                    totalProductivityForDate += productivity;
                }
            }

            // Add the total productivity for the date
            dailyProductivity.put(date.toString(), totalProductivityForDate);
        }

        return dailyProductivity;  // Map of date -> total productivity for the date
    }
    public Map<String, Double> calculateEcartDureePerDate(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found with id " + projectId));

        List<Task> tasks = project.getTasks();
        if (tasks.isEmpty()) {
            return Collections.emptyMap(); // Return empty if no tasks exist
        }

        // Group tasks by their start date
        Map<String, List<Task>> tasksByDate = tasks.stream()
                .collect(Collectors.groupingBy(task -> task.getStart_date().split(" ")[0])); // Group by the date part

        // Create a map to store Ecart de durée for each date
        Map<String, Double> ecartDureePerDate = new HashMap<>();

        for (Map.Entry<String, List<Task>> entry : tasksByDate.entrySet()) {
            String date = entry.getKey();
            List<Task> tasksForDate = entry.getValue();

            // Calculate the total ecart duree for all tasks on this date
            double totalEcart = 0;
            int count = 0;

            for (Task task : tasksForDate) {
                if (task.getDuration() != null && task.getDureeReelle() != null && task.getDuration() > 0) {
                    double ecartDuree = (task.getDureeReelle().getTime() - task.getDuration()) / (double) task.getDuration();
                    totalEcart += ecartDuree;
                    count++;
                }
            }

            // Calculate average Ecart de durée for the date
            if (count > 0) {
                double averageEcart = totalEcart / count;
                ecartDureePerDate.put(date, averageEcart);
            }
        }

        return ecartDureePerDate; // Map of date -> ecart duree
    }




}