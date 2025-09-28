package com.crm.corecrm.controller;

import com.crm.corecrm.entities.Tasks;
import com.crm.corecrm.service.TasksService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tasks")
public class TasksController {

    private final TasksService tasksService;

    public TasksController(TasksService tasksService) {
        this.tasksService = tasksService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tasks> getTaskById(@PathVariable Long id) {
        return tasksService.getTaskById(id);
    }

    @PostMapping
    public ResponseEntity<String> createTask(@RequestBody Tasks task) {
        return tasksService.addTask(task);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateTask(@PathVariable Long id, @RequestBody Tasks task) {
        return tasksService.updateTask(id, task);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        tasksService.deleteTask(id);
    }
}
