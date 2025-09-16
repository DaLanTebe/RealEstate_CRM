package com.crm.corecrm.service.impl;

import com.crm.corecrm.DTO.UserToRetrieveDTO;
import com.crm.corecrm.DTO.UserToSaveDTO;
import com.crm.corecrm.entities.Users;
import com.crm.corecrm.exception.UserAlreadyExistsException;
import com.crm.corecrm.exception.UserNotFoundException;
import com.crm.corecrm.mapper.UserMapper;
import com.crm.corecrm.repository.UsersRepo;
import com.crm.corecrm.service.RegistrationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.UUID;

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
    public ResponseEntity<String> registerUser(UserToSaveDTO user) {
        if (usersRepo.existsByEmail(user.getEmail()) || usersRepo.existsByUsername(user.getUsername())) {
            throw new UserAlreadyExistsException("Пользователь с таким email или username уже существует");
        }

        Users userToSave = userMapper.toUser(user);
        userToSave.setTasksList(new ArrayList<>());
        try {
            byte[] passwordsHash = MessageDigest.getInstance("SHA-256").digest(user.getPassword().getBytes());
            String encodedPassword = Base64.getEncoder().encodeToString(passwordsHash);
            userToSave.setPassword(encodedPassword);
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body("Ошибка при регистрации");
        }

        usersRepo.save(userToSave);

        return ResponseEntity.ok("Пользователь успешно зарегистрирован");
    }

    @Override
    public ResponseEntity<UserToRetrieveDTO> getUsers(UUID userId) {
        Users user = usersRepo.findById(userId).orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
        userMapper.toUserToRetrieveDTO(user);
        return ResponseEntity.ok(userMapper.toUserToRetrieveDTO(user));
    }

    @Override
    public ResponseEntity<String> updateUser(UUID userId,UserToSaveDTO user) {
        if (usersRepo.existsById(userId)){

            Users userToUpdate = userMapper.toUser(user);
            userToUpdate.setId(userId);

            log.info("Пользователь с id: " + userId + " успешно обновлен");
            usersRepo.save(userToUpdate);

            return ResponseEntity.ok("Пользователь успешно обновлен");

        }else throw new UserNotFoundException("Пользователь не найден");
    }

    @Override
    public ResponseEntity<String> deleteUser(UUID userId) {
        if (usersRepo.existsById(userId)){

            usersRepo.deleteById(userId);

            return ResponseEntity.ok("Пользователь успешно удален");

        }else throw new UserNotFoundException("Пользователь не найден");
    }
}
