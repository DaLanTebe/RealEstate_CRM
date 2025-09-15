package com.adapter.contentloaderadapter.controller;

import com.adapter.contentloaderadapter.service.ContentProcessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/upload")
public class UploadController {
    private final ContentProcessorService service;

    @Autowired
    public UploadController(ContentProcessorService service) {
        this.service = service;
    }

    public ResponseEntity<String> enableUploadXlsxFiles(){
        return service.process();
    }
}
