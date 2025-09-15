package com.adapter.contentloaderadapter.service;

import org.springframework.http.ResponseEntity;

public interface ContentProcessorService {

    ResponseEntity<String> process();
}
