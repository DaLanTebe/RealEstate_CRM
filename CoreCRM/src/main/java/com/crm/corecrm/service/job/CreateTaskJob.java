package com.crm.corecrm.service.job;

import com.crm.corecrm.entities.Building;
import com.crm.corecrm.entities.Tasks;
import com.crm.corecrm.entities.Users;
import com.crm.corecrm.repository.BuildingRepo;
import com.crm.corecrm.repository.TasksRepo;
import com.crm.corecrm.repository.UsersRepo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

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
        List<Users> freeUsers = usersRepo.findAllByTasksListIsEmptyOrTasksInTasksListIsCompleted();
        List<Building> notSoldBuildings = buildingRepo.findAllByStatus(Building.Status.NOTASSIGNED);
        if (freeUsers.isEmpty() || notSoldBuildings.isEmpty()) {
            return;
        }
        notSoldBuildings.forEach(building -> {
            Users user = freeUsers.get(new Random().nextInt(freeUsers.size()));

            Tasks task = new Tasks();
            task.setBuilding(building);
            task.setAssignedTo(user);
            task.setTitle("Связаться с владельцем");
            task.setDescription("Номер владельца: " + building.getDescription());
            task.setPriority(Tasks.Priority.CONTACT);
            task.setDueDate(LocalDateTime.now().toLocalDate().plusDays(60));
            task.setStatus(Tasks.Status.IN_PROGRESS);

            tasksRepo.save(task);
            building.setStatus(Building.Status.ASSIGNED);
            buildingRepo.save(building);
            freeUsers.remove(user);
        });
    }
}
