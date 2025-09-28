package com.crm.corecrm.service.job;

import com.crm.corecrm.DTO.Notification;
import com.crm.corecrm.entities.Building;
import com.crm.corecrm.entities.Tasks;
import com.crm.corecrm.entities.Users;
import com.crm.corecrm.repository.BuildingRepo;
import com.crm.corecrm.repository.TasksRepo;
import com.crm.corecrm.repository.UsersRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Component
@Slf4j
public class CreateTaskJob implements Job {

    private final TasksRepo tasksRepo;
    private final BuildingRepo buildingRepo;
    private final UsersRepo usersRepo;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper mapper = new ObjectMapper();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public CreateTaskJob(TasksRepo tasksRepo, BuildingRepo buildingRepo, UsersRepo usersRepo, KafkaTemplate<String, String> kafkaTemplate) {
        this.tasksRepo = tasksRepo;
        this.buildingRepo = buildingRepo;
        this.usersRepo = usersRepo;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        List<Users> freeUsers = usersRepo.findAllByTasksListIsEmptyOrTasksInTasksListIsCompleted();
        List<Building> notSoldBuildings = buildingRepo.findAllByStatus(Building.Status.NOTASSIGNED);

        notSoldBuildings.forEach(building -> {
            if (freeUsers.isEmpty() || notSoldBuildings.isEmpty()) {
                return;
            }

            Users user = freeUsers.getFirst();

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
            sendNotification(user.getEmail(), user.getPhoneNumber(), task.getTitle(), task.getDescription());
            freeUsers.remove(user);
        });
    }

    private void sendNotification(String email, String phoneNumber, String title, String description) {
        Notification notification = new Notification();
        notification.setEmail(email);
        notification.setPhoneNumber(phoneNumber);
        notification.setTitle("Вам назначена новая задача: " + title);
        notification.setDescription("Описание: " + description );
        try {
            String valueToSend = objectMapper.writeValueAsString(notification);
            kafkaTemplate.send("notifications", valueToSend);
        } catch (JsonProcessingException e) {
            log.error("Ошибка при парсинге JSON", e);
        }
    }
}
