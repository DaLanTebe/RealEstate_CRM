package com.crm.corecrm.repository;

import com.crm.corecrm.entities.Building;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BuildingRepo extends JpaRepository<Building, Long> {

    List<Building> findAllByStatus(Building.Status status);
}
