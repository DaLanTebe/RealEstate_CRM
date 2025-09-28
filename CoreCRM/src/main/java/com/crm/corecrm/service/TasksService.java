package com.crm.corecrm.service;

import com.crm.corecrm.entities.Tasks;
import org.springframework.http.ResponseEntity;


public interface TasksService{

    public ResponseEntity<String> addTask(Tasks task);

    public ResponseEntity<String> updateTask(Long id,Tasks task);

    public void deleteTask(Long id);

    public ResponseEntity<Tasks> getTaskById(Long id);
}
