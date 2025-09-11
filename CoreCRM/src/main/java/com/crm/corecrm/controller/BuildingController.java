package com.crm.corecrm.controller;

import com.crm.corecrm.entities.Building;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/building")
public class BuildingController {

    public ResponseEntity<String> save(Building building) {
return null;
    }
}
