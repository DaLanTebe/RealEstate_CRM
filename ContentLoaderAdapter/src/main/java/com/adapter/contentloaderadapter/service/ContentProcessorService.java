package com.adapter.contentloaderadapter.service;

import org.springframework.http.ResponseEntity;

import java.io.FileNotFoundException;

public interface ContentProcessorService {

    ResponseEntity<String> rosreestrUpload() throws FileNotFoundException;

    ResponseEntity<String> cianUpload();

    ResponseEntity<String> domClickUpload();
}
