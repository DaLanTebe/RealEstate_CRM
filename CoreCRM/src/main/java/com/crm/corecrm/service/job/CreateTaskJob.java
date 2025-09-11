package com.crm.corecrm.service.job;

import com.crm.corecrm.repository.BuildingRepo;
import com.crm.corecrm.repository.TasksRepo;
import com.crm.corecrm.repository.UsersRepo;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CreateTaskJob implements Job {

    private final TasksRepo tasksRepo;
    private final BuildingRepo buildingRepo;
    private final UsersRepo usersRepo;

    @Autowired
    public CreateTaskJob(TasksRepo tasksRepo, BuildingRepo buildingRepo, UsersRepo usersRepo) {
        this.tasksRepo = tasksRepo;
        this.buildingRepo = buildingRepo;
        this.usersRepo = usersRepo;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

    }
}
