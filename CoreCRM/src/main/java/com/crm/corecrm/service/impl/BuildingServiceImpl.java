package com.crm.corecrm.service.impl;

import com.crm.corecrm.entities.Building;
import com.crm.corecrm.repository.BuildingRepo;
import com.crm.corecrm.service.BuildingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class BuildingServiceImpl implements BuildingService {

    private final BuildingRepo buildingRepo;

    @Autowired
    public BuildingServiceImpl(BuildingRepo buildingRepo) {
        this.buildingRepo = buildingRepo;
    }

    @Override
    public ResponseEntity<String> addBuilding(Building building) {
        building.setStatus(Building.Status.ACTIVE);
        buildingRepo.save(building);
        return ResponseEntity.ok("Task added");
    }
}
