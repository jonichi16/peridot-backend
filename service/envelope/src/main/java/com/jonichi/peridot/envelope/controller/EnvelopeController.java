package com.jonichi.peridot.envelope.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/envelopes")
@RequiredArgsConstructor
public class EnvelopeController {

    private static final Logger logger = LoggerFactory.getLogger(EnvelopeController.class);

    @PostMapping("")
    public ResponseEntity<?> createEnvelope() {
        logger.info("Start - Controller - createBudget");
        logger.info("End - Controller - createBudget");
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
