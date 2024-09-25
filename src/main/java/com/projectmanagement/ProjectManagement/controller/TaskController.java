    package com.projectmanagement.ProjectManagement.controller;

    import com.projectmanagement.ProjectManagement.DTO.TaskDto;
    import com.projectmanagement.ProjectManagement.entity.Task;
    import com.projectmanagement.ProjectManagement.service.TaskService;
    import lombok.AllArgsConstructor;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;

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
    }
