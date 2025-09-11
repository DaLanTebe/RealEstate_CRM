package com.crm.corecrm.repository;

import com.crm.corecrm.entities.Tasks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TasksRepo extends JpaRepository<Tasks, Long>{
}
