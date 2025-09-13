package com.crm.corecrm.repository;

import com.crm.corecrm.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface UsersRepo extends JpaRepository<Users, UUID> {

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    @Query("""
            SELECT u FROM Users u
            left join u.tasksList t
            group by u
            having count(t) = 0 or sum(case when t.status <> 'COMPLETED' then 1 else 0 end) = 0
            """)
    List<Users> findAllByTasksListIsEmptyOrTasksInTasksListIsCompleted();
}
