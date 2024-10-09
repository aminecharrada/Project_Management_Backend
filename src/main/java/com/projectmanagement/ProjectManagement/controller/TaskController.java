    package com.projectmanagement.ProjectManagement.controller;

    import com.projectmanagement.ProjectManagement.DTO.TaskDto;
    import com.projectmanagement.ProjectManagement.entity.Task;
    import com.projectmanagement.ProjectManagement.service.TaskService;
    import lombok.AllArgsConstructor;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import java.util.HashMap;
    import java.util.List;
    import java.util.Map;

    @RestController
    @AllArgsConstructor
    @CrossOrigin("*")
    @RequestMapping("/api/tasks")
    public class TaskController {

        private final TaskService taskService;


            @GetMapping("/pole/{poleName}")
            public ResponseEntity<List<Task>> getTasksByPoleName(@PathVariable String poleName) {
                List<Task> tasks = taskService.getTasksByPoleName(poleName);
                return ResponseEntity.ok(tasks);
            }

        @PostMapping("/gantt")
        public ResponseEntity<Task> createTask(@RequestBody TaskDto taskDto, @RequestParam String projectName) {
            Task task = taskService.createTask(taskDto, projectName);
            return new ResponseEntity<>(task, HttpStatus.CREATED);
        }

        @PutMapping("/{id}")
        public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody TaskDto taskDto) {
            Task updatedTask = taskService.updateTask(id, taskDto);
            return new ResponseEntity<>(updatedTask, HttpStatus.OK);
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
            taskService.deleteTask(id);
            return ResponseEntity.noContent().build();
        }

        @GetMapping("/project")
        public ResponseEntity<List<Task>> getTasksByProjectName(@RequestParam String projectName) {
            List<Task> tasks = taskService.getTasksByProjectName(projectName);
            return ResponseEntity.ok(tasks);
        }

        @GetMapping
        public ResponseEntity<List<Task>> getAllTasks() {
            List<Task> tasks = taskService.findAll();
            return ResponseEntity.ok(tasks);
        }
        @GetMapping("/{projectId}/tasks-status")
        public ResponseEntity<Map<String, Integer>> getTaskStatus(@PathVariable Long projectId) {
            List<Task> completedTasks = taskService.getCompletedTasks(projectId);
            List<Task> incompleteTasks = taskService.getIncompleteTasks(projectId);

            Map<String, Integer> taskStatus = new HashMap<>();
            taskStatus.put("completed", completedTasks.size());
            taskStatus.put("incomplete", incompleteTasks.size());

            return ResponseEntity.ok(taskStatus);
        }
        @GetMapping("/tasks/person/{name}/details")
        public ResponseEntity<Map<String, Object>> getTasksDetailsByPerson(@PathVariable String name) {
            Long count = taskService.countTasksByPersonName(name);
            List<String> taskNames = taskService.getAllTaskNamesByPersonName(name);

            Map<String, Object> response = new HashMap<>();
            response.put("totalTasks", count);
            response.put("taskNames", taskNames);

            return ResponseEntity.ok(response);
        }


    }
