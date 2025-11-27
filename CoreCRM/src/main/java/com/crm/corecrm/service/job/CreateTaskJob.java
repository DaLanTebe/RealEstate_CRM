package com.crm.corecrm.service.job;

import com.crm.corecrm.DTO.Notification;
import com.crm.corecrm.entities.Building;
import com.crm.corecrm.entities.OutBoxEvent;
import com.crm.corecrm.entities.Tasks;
import com.crm.corecrm.entities.Users;
import com.crm.corecrm.handler.UserNotFoundException;
import com.crm.corecrm.repository.BuildingRepo;
import com.crm.corecrm.repository.OutBoxRepository;
import com.crm.corecrm.repository.TasksRepo;
import com.crm.corecrm.repository.UsersRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

@Component
@Slf4j
public class CreateTaskJob implements Job {

    private final TasksRepo tasksRepo;
    private final BuildingRepo buildingRepo;
    private final UsersRepo usersRepo;
    private final OutBoxRepository outBoxRepo;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private Scheduler scheduler;

    @Autowired
    public CreateTaskJob(TasksRepo tasksRepo, BuildingRepo buildingRepo, UsersRepo usersRepo, OutBoxRepository outBoxRepo) {
        this.tasksRepo = tasksRepo;
        this.buildingRepo = buildingRepo;
        this.usersRepo = usersRepo;
        this.outBoxRepo = outBoxRepo;
    }

    @Override
    @Transactional
    public void execute(JobExecutionContext jobExecutionContext) {
        log.info("🟡 Начало CreateTaskJob");

        try {
            List<Users> freeUsers = usersRepo.findAllByTasksListIsEmptyOrTasksInTasksListIsCompleted();
            List<Building> notSoldBuildings = buildingRepo.findAllByStatus(Building.Status.NOTASSIGNED);

            log.info("Найдено свободных пользователей: {}, нераспределенных зданий: {}",
                    freeUsers.size(), notSoldBuildings.size());

            if (freeUsers.isEmpty() || notSoldBuildings.isEmpty()) {
                log.info("❌ Недостаточно данных: пользователей={}, зданий={}",
                        freeUsers.size(), notSoldBuildings.size());
                return;
            }

            int createdTasks = 0;

            // Используем итератор для безопасного удаления
            Iterator<Building> buildingIterator = notSoldBuildings.iterator();
            Iterator<Users> userIterator = freeUsers.iterator();

            while (buildingIterator.hasNext() && userIterator.hasNext()) {
                Building building = buildingIterator.next();
                Users user = userIterator.next();

                try {
                    createTaskForBuilding(building, user);
                    createdTasks++;
                    log.info("✅ Создана задача для здания {} пользователю {}",
                            building.getId(), user.getId());

                } catch (Exception e) {
                    log.error("❌ Ошибка при создании задачи для здания {}: {}",
                            building.getId(), e.getMessage(), e);
                }
            }

            log.info("🟢 CreateTaskJob завершен. Создано задач: {}", createdTasks);

        } catch (Exception e) {
            log.error("🔴 Критическая ошибка в CreateTaskJob: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    private void createTaskForBuilding(Building building, Users user) {
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

        createOutboxEvent(task, user);

        log.debug("Создана задача ID: {} для пользователя ID: {}", task.getId(), user.getId());
    }

    private void createOutboxEvent(Tasks task, Users user) {
        OutBoxEvent outboxEvent = new OutBoxEvent();
        outboxEvent.setAggregateType("TASK");
        outboxEvent.setAggregateId(task.getId().toString());
        outboxEvent.setEventType("TASK_CREATED");

        Notification notification = new Notification();
        notification.setEmail(user.getEmail());
        notification.setPhoneNumber(user.getPhoneNumber());
        notification.setTitle("Вам назначена новая задача: " + task.getTitle());
        notification.setDescription("Описание: " + task.getDescription());

        try {
            outboxEvent.setPayload(objectMapper.writeValueAsString(notification));
        } catch (JsonProcessingException e) {
            log.error("Ошибка при создании outbox события", e);
            throw new UserNotFoundException(e.getMessage());
        }

        outboxEvent.setCreatedAt(LocalDateTime.now());

        outBoxRepo.save(outboxEvent);
    }
}
