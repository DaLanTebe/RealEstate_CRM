package com.crm.corecrm.controller;

import com.crm.corecrm.entities.Building;
import com.crm.corecrm.service.BuildingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/building")
public class BuildingController {

    private final BuildingService buildingService;

    @Autowired
    public BuildingController(BuildingService buildingService) {
        this.buildingService = buildingService;
    }

    @PostMapping
    public ResponseEntity<String> save(@RequestBody @Valid Building building) {
        return buildingService.addBuilding(building);
    }

    @GetMapping({"/{cadastralNumber}"})
    public ResponseEntity<Building> getBuildingByCadastralNumber(@PathVariable String cadastralNumber) {
        return buildingService.findBuildingByCadastralNumber(cadastralNumber);
    }
}
