package com.crm.corecrm.service.impl;

import com.crm.corecrm.DTO.UserDTO;
import com.crm.corecrm.mapper.UserMapper;
import com.crm.corecrm.repository.UsersRepo;
import com.crm.corecrm.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
        usersRepo.save(userMapper.toUser(user));
        return ResponseEntity.ok("Пользователь успешно зарегистрирован");
    }
}
