package com.crm.corecrm.service;

import com.crm.corecrm.DTO.UserDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

public interface RegistrationService {

    public ResponseEntity<String> registerUser(UserDTO user);
}
