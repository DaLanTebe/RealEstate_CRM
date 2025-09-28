package com.adapter.contentloaderadapter.controller;

import com.adapter.contentloaderadapter.service.ContentProcessorService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

@RestController
@RequestMapping("/upload")
public class UploadController {
    private final ContentProcessorService service;
    private static final Random random = new Random();

    @Autowired
    public UploadController(ContentProcessorService service) {
        this.service = service;
    }

    @GetMapping("/ros")
    public ResponseEntity<String> enableUploadRosreestr() {
        return service.rosreestrUpload();
    }

    @GetMapping("/cian")
    public ResponseEntity<String> enableUploadCian() {
        return service.cianUpload();
    }

    @GetMapping("/dom")
    public ResponseEntity<String> enableUploadDomClick() {
        return service.domClickUpload();
    }
}
