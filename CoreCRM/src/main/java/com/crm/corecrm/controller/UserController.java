package com.crm.corecrm.controller;

import com.crm.corecrm.DTO.UserToRetrieveDTO;
import com.crm.corecrm.DTO.UserToSaveDTO;
import com.crm.corecrm.service.RegistrationService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(value = "/users")
public class UserController {

    private final RegistrationService registrationService;

    @Autowired
    public UserController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody @Valid UserToSaveDTO userToSaveDTO) {
        return registrationService.registerUser(userToSaveDTO);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserToRetrieveDTO> getUsers(@PathVariable("userId") UUID userId) {
        return registrationService.getUsers(userId);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<String> updateUser(@PathVariable("userId") UUID userId, @RequestBody @Valid UserToSaveDTO userToSaveDTO) {
        return registrationService.updateUser(userId, userToSaveDTO);
    }
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable("userId") UUID userId) {
        return registrationService.deleteUser(userId);
    }
}
