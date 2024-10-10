        package com.projectmanagement.ProjectManagement.service;
        import com.projectmanagement.ProjectManagement.DTO.TaskDto;
        import com.projectmanagement.ProjectManagement.entity.Pole;
        import com.projectmanagement.ProjectManagement.entity.Project;
        import com.projectmanagement.ProjectManagement.repository.PersonneRepository;
        import com.projectmanagement.ProjectManagement.entity.Personne;
        import com.projectmanagement.ProjectManagement.entity.Task;
        import com.projectmanagement.ProjectManagement.repository.PoleRepository;
        import com.projectmanagement.ProjectManagement.repository.ProjectRepository;
        import com.projectmanagement.ProjectManagement.repository.TaskRepository;

        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.scheduling.annotation.Scheduled;
        import org.springframework.stereotype.Service;
        import java.util.*;
        import java.util.stream.Collectors;

        @Service

        public class TaskService {
            @Autowired
            private TaskRepository taskRepository;
            @Autowired
            private PoleRepository poleRepository;
            @Autowired
            private PersonneRepository personneRepository;
            @Autowired
            private ProjectRepository projectRepository;

            public List<Task> getTasksByPoleName(String poleName) {
                return taskRepository.findByPoleName(poleName);
            }

            public List<Task> getTasksByProjectName(String projectName) {
                return taskRepository.findTasksByProjectName(projectName);
            }


            public Task createTask(TaskDto taskDto, String projectName) {
                System.out.println("Creating task with projectName: " + projectName);
                if (projectName == null || projectName.isEmpty()) {
                    throw new RuntimeException("Project name is required");
                }
                Task task = new Task();
                task.setText(taskDto.getText());
                task.setStart_date(taskDto.getStart_date());
                task.setProgress(taskDto.getProgress());
                task.setDuration(taskDto.getDuration());
                task.setParent(taskDto.getParent());
                task.setPoleName(taskDto.getPoleName());
                task.setDureeReelle(new Date());

                if (taskDto.getPersonnes() != null && !taskDto.getPersonnes().isEmpty()) {
                    Set<Long> personnesIds = taskDto.getPersonnes();
                    Set<Personne> personnes = new HashSet<>();
                    StringBuilder ressourceNames = new StringBuilder();

                    for (Long id : personnesIds) {
                        Personne personne = personneRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Personne not found with id " + id));
                        personnes.add(personne);
                        if (ressourceNames.length() > 0) {
                            ressourceNames.append(", ");
                        }
                        ressourceNames.append(personne.getName());
                    }

                    task.setPersonnes(personnes); // Save Personne entities
                    task.setRessource(ressourceNames.toString()); // Save person names as a comma-separated string
                } else {
                    task.setRessource(""); // Default value if no personnes
                }

                Project project = projectRepository.findByTitle(projectName);
                if (project != null) {
                    task.setProject(project);
                } else {
                    throw new RuntimeException("Project not found with name: " + projectName);
                }

                System.out.println("Saving task: " + task);
                Task savedTask = taskRepository.save(task);
                System.out.println("Task saved with ID: " + savedTask.getId());
                // Update pole progress
                updatePoleProgress(savedTask.getPoleName());
                // Recalculate project progress
                updateProjectProgress(savedTask.getProject());
                return savedTask;
            }

            public Task updateTask(Long id, TaskDto taskDto) {
                // Fetch the existing task from the database
                Task task = taskRepository.findById(id)
                        .orElseThrow(() -> new NoSuchElementException("Task not found with id " + id));

                // Update the task name and other fields that are provided
                task.setText(taskDto.getText());
                task.setStart_date(taskDto.getStart_date());
                task.setProgress(taskDto.getProgress());
                task.setDuration(taskDto.getDuration());
                task.setParent(taskDto.getParent());

                // Only update the poleName if it's provided, otherwise keep the existing one
                if (taskDto.getPoleName() != null && !taskDto.getPoleName().isEmpty()) {
                    task.setPoleName(taskDto.getPoleName());
                }

                // Only update 'personnes' and 'ressource' if they are provided
                if (taskDto.getPersonnes() != null && !taskDto.getPersonnes().isEmpty()) {
                    updatePersonnes(task, taskDto.getPersonnes());
                }

                // Save the updated task
                Task updatedTask = taskRepository.save(task);

                // Update pole progress
                updatePoleProgress(updatedTask.getPoleName());

                // Recalculate project progress

                updateProjectProgress(task.getProject());



                return updatedTask;
            }

            public Map<String, Object> getTasksDetailsByPersonName(String personName) {
                Long count = countTasksByPersonName(personName);
                List<String> taskNames = getAllTaskNamesByPersonName(personName);

                Map<String, Object> response = new HashMap<>();
                response.put("totalTasks", count);
                response.put("taskNames", taskNames);

                return response;
            }

            public List<String> getAllTaskNamesByPersonName(String personName) {
                return taskRepository.findAllTaskNamesByPersonName(personName);
            }

            public Long countTasksByPersonName(String personName) {
                return taskRepository.countTasksByPersonName(personName);
            }



            private void updateProjectProgress(Project project) {
                List<Task> tasks = project.getTasks();
                if (tasks.isEmpty()) {
                    project.setProgress(0); // No tasks, no progress
                } else {
                    double totalProgress = tasks.stream()
                            .mapToDouble(Task::getProgress)
                            .sum();
                    double averageProgress = totalProgress / tasks.size();
                    project.setProgress(averageProgress * 100); // Normalize by the number of tasks
                }
                projectRepository.save(project);
            }


            private void updatePersonnes(Task task, Set<Long> personneIds) {
                Set<Personne> updatedPersonnes = personneIds.stream()
                        .map(personneId -> personneRepository.findById(personneId)
                                .orElseThrow(() -> new NoSuchElementException("Personne not found with id " + personneId)))
                        .collect(Collectors.toSet());

                task.setPersonnes(updatedPersonnes);

                String ressourceNames = updatedPersonnes.stream()
                        .map(Personne::getName)
                        .collect(Collectors.joining(", "));

                task.setRessource(ressourceNames);
            }
            @Scheduled(cron = "0 0 0 * * ?") // This runs daily at midnight
            public void updateOverdueTasks() {
                List<Task> overdueTasks = taskRepository.findOverdueTasks();
                Date today = new Date();

                for (Task task : overdueTasks) {
                    // Only update dureeReelle if the task is not completed
                    if (task.getProgress() < 1) { // Assuming progress 1 indicates completion
                        task.setDureeReelle(today); // Update to current date
                        taskRepository.save(task);
                    }
                }
            }
            public List<Task> getCompletedTasks(Long projectId) {
                return taskRepository.findCompletedTasksByProject(projectId);
            }
            public List<Task> getIncompleteTasks(Long projectId) {
                return taskRepository.findIncompleteTasksByProject(projectId);
            }
            public void deleteTask(Long id) {
                Task task = taskRepository.findById(id)
                        .orElseThrow(() -> new NoSuchElementException("Task not found with id " + id));
                String poleName = task.getPoleName();
                Project project = task.getProject();

                taskRepository.deleteById(id);
                // Update pole progress
                updatePoleProgress(poleName);

                // Recalculate project progress after task deletion
                updateProjectProgress(project);
            }
            private void updatePoleProgress(String poleName) {
                List<Task> tasks = taskRepository.findByPoleName(poleName);
                if (tasks.isEmpty()) {
                    return;
                }

                double totalProgress = tasks.stream()
                        .mapToDouble(Task::getProgress)
                        .sum();
                double averageProgress = totalProgress / tasks.size();

                Pole pole = poleRepository.findByPoleName(poleName)
                        .orElseThrow(() -> new RuntimeException("Pole not found with name: " + poleName));
                pole.setPoleProgress(averageProgress * 100); // Normalize by the number of tasks

                poleRepository.save(pole);
            }


            public List<Task> findAll() {
            return taskRepository.findAll();
        }
    }
