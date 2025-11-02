package com.crm.corecrm.service.impl;

import com.crm.corecrm.entities.Building;
import com.crm.corecrm.repository.BuildingRepo;
import com.crm.corecrm.service.BuildingService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    @Transactional
    public ResponseEntity<String> addBuilding(Building building) {
        if (buildingRepo.existsByCadastralNumber(building.getCadastralNumber())) {
            return ResponseEntity.badRequest().body("Такой кадастровый номер уже существует");
        }
        building.setStatus(Building.Status.NOTASSIGNED);
        buildingRepo.save(building);
        return ResponseEntity.ok("Building added");
    }

    @Override
    public ResponseEntity<Building> findBuildingByCadastralNumber(String cadastralNumber) {
        return new ResponseEntity<>(buildingRepo.findByCadastralNumber(cadastralNumber), HttpStatus.OK);
    }

}
