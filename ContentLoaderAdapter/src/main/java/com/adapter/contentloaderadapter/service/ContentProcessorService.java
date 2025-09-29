package com.adapter.contentloaderadapter.service;

import org.springframework.http.ResponseEntity;


public interface ContentProcessorService {

    ResponseEntity<String> rosreestrUpload();

    ResponseEntity<String> cianUpload();

    ResponseEntity<String> domClickUpload();
}
