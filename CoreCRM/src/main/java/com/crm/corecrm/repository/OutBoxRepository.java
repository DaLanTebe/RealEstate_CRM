package com.crm.corecrm.repository;

import com.crm.corecrm.entities.OutBoxEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OutBoxRepository extends JpaRepository<OutBoxEvent, Integer> {

    List<OutBoxEvent> findByProcessedFalse();
}
