package com.crm.corecrm.service.impl;

import com.crm.corecrm.DTO.UserDTO;
import com.crm.corecrm.entities.Users;
import com.crm.corecrm.mapper.UserMapper;
import com.crm.corecrm.repository.UsersRepo;
import com.crm.corecrm.service.RegistrationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RegistrationServiceImpl implements RegistrationService {

    private final UsersRepo usersRepo;

    private final UserMapper userMapper;

    @Autowired
    public RegistrationServiceImpl(UsersRepo usersRepo, UserMapper userMapper) {
        this.usersRepo = usersRepo;
        this.userMapper = userMapper;
    }

    @Override
    public ResponseEntity<String> registerUser(UserDTO user) {
        Users user1 = userMapper.toUser(user);
        usersRepo.save(user1);
        return ResponseEntity.ok("Пользователь успешно зарегистрирован");
    }
}
