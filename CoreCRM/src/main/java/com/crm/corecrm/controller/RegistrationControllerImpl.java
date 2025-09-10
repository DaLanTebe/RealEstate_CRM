package com.crm.corecrm.controller;

import com.crm.corecrm.DTO.UserDTO;
import com.crm.corecrm.service.RegistrationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/registration")
public class RegistrationControllerImpl{

    private final RegistrationService registrationService;

    public RegistrationControllerImpl(@RequestBody @Valid RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping
    public ResponseEntity<String> registerUser(UserDTO userDTO) {
        return registrationService.registerUser(userDTO);
    }
}
