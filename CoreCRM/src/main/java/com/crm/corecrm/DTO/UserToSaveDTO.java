package com.crm.corecrm.DTO;

import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
public class UserToSaveDTO implements Serializable {

    private UUID id;

    private String username;

    private String password;

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;
}
