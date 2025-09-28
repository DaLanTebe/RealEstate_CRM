package com.crm.corecrm.service;

import com.crm.corecrm.entities.Building;
import org.springframework.http.ResponseEntity;

public interface BuildingService {
    public ResponseEntity<String> addBuilding(Building building);

    public ResponseEntity<Building> findBuildingByCadastralNumber(String cadastralNumber);
}
