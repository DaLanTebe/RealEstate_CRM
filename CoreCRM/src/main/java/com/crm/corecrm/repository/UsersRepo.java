package com.crm.corecrm.repository;

import com.crm.corecrm.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UsersRepo extends JpaRepository<Users, UUID> {

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

}
