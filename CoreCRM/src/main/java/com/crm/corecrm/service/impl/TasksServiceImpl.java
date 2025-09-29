package com.crm.corecrm.service.impl;

import com.crm.corecrm.entities.Tasks;
import com.crm.corecrm.repository.TasksRepo;
import com.crm.corecrm.service.TasksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class TasksServiceImpl implements TasksService {

    private final TasksRepo tasksRepo;

    @Autowired
    public TasksServiceImpl(TasksRepo tasksRepo) {
        this.tasksRepo = tasksRepo;
    }

    @Override
    public ResponseEntity<String> addTask(Tasks task) {
        task.setStatus(Tasks.Status.NEW);
        task.setPriority(Tasks.Priority.CONTACT);
        tasksRepo.save(task);
        return ResponseEntity.ok("Task added");
    }

    @Override
    public ResponseEntity<String> updateTask(Long id, Tasks task) {
        task.setId(id);
        tasksRepo.save(task);
        return ResponseEntity.ok("Task updated");
    }

    @Override
    public void deleteTask(Long id) {
        tasksRepo.deleteById(id);
    }

    @Override
    public ResponseEntity<Tasks> getTaskById(Long id) {
        return new ResponseEntity<>(tasksRepo.findById(id).orElse(null), HttpStatus.OK);
    }

}
