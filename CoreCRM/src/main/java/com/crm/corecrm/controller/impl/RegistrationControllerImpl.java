package com.crm.corecrm.controller.impl;

import com.crm.corecrm.DTO.UserDTO;
import com.crm.corecrm.controller.RegistrationController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/registration")
public class RegistrationControllerImpl implements RegistrationController {


    @Override
    @PostMapping
    public ResponseEntity<String> registerUser(UserDTO userDTO) {

        return null;
    }
}
