package com.crm.corecrm.controller;

import com.crm.corecrm.DTO.UserDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public interface RegistrationController {

    public ResponseEntity<String> registerUser(UserDTO userDTO);

}
