package com.crm.corecrm.service;

import com.crm.corecrm.DTO.UserToRetrieveDTO;
import com.crm.corecrm.DTO.UserToSaveDTO;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface RegistrationService {

    ResponseEntity<String> registerUser(UserToSaveDTO user);

    ResponseEntity<UserToRetrieveDTO> getUsers(UUID userId);

    ResponseEntity<String> updateUser(UUID id, UserToSaveDTO user);

    ResponseEntity<String> deleteUser(UUID userId);


}
