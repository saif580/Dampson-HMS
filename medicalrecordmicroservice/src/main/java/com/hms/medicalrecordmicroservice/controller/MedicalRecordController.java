package com.hms.medicalrecordmicroservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hms.medicalrecordmicroservice.model.MedicalRecord;
import com.hms.medicalrecordmicroservice.service.MedicalRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/medicalrecords")
public class MedicalRecordController {
    private static final Logger logger = LoggerFactory.getLogger(MedicalRecordController.class);

    @Autowired
    private MedicalRecordService medicalRecordService;


    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<MedicalRecord> createMedicalRecord(
            @RequestPart("file") MultipartFile file,
            @RequestPart("medicalRecord") String medicalRecordJson) {
        logger.debug("Received request to create medical record");
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            MedicalRecord medicalRecord = objectMapper.readValue(medicalRecordJson, MedicalRecord.class);
            MedicalRecord createdRecord = medicalRecordService.saveMedicalRecord(medicalRecord, file);
            logger.debug("Successfully created medical record");
            return ResponseEntity.ok(createdRecord);
        } catch (IOException e) {
            logger.error("Error creating medical record", e);
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping
    public List<MedicalRecord> getAllMedicalRecords() {
        return medicalRecordService.getAllMedicalRecords();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicalRecord> getMedicalRecordById(@PathVariable Long id) {
        MedicalRecord medicalRecord = medicalRecordService.getMedicalRecordById(id);
        return medicalRecord != null ? ResponseEntity.ok(medicalRecord) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedicalRecord(@PathVariable Long id) {
        medicalRecordService.deleteMedicalRecord(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/patient/{patientId}")
    public List<MedicalRecord> getMedicalRecordsByPatientId(@PathVariable Long patientId) {
        return medicalRecordService.getMedicalRecordsByPatientId(patientId);
    }
}
