package com.crm.corecrm.DTO;

import com.crm.corecrm.entities.Tasks;
import lombok.Data;

import java.util.List;

@Data
public class UserToRetrieveDTO {

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private List<Tasks> tasksList;
}
