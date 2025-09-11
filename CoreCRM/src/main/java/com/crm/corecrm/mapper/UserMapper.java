package com.crm.corecrm.mapper;

import com.crm.corecrm.DTO.UserToRetrieveDTO;
import com.crm.corecrm.DTO.UserToSaveDTO;
import com.crm.corecrm.entities.Users;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "username", source = "username")
    @Mapping(target = "password", source = "password")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "phoneNumber", source = "phoneNumber")
    Users toUser(UserToSaveDTO userToSaveDTO);

    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "phoneNumber", source = "phoneNumber")
    @Mapping(target = "tasksList", source = "tasksList")
    UserToRetrieveDTO toUserToRetrieveDTO(Users users);
}
