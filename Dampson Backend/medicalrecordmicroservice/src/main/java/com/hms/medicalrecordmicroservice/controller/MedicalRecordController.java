package com.hms.medicalrecordmicroservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hms.medicalrecordmicroservice.model.MedicalRecord;
import com.hms.medicalrecordmicroservice.service.MedicalRecordService;
import com.hms.medicalrecordmicroservice.service.PdfService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/medicalrecords")
public class MedicalRecordController {
    private static final Logger logger = LoggerFactory.getLogger(MedicalRecordController.class);

    @Autowired
    private MedicalRecordService medicalRecordService;

    @Autowired
    private PdfService pdfService;

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<MedicalRecord> createMedicalRecord(
            @RequestPart(value="file", required=false) MultipartFile file,
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

    @GetMapping("/{recordId}/generatePdf")
    public ResponseEntity<byte[]> generateMedicalRecordPdf(@PathVariable Long recordId) {
        try {
            logger.info("Request received to generate PDF for record ID: {}", recordId);
            Optional<MedicalRecord> medicalRecordOptional = Optional.ofNullable(medicalRecordService.getMedicalRecordById(recordId));
            if (medicalRecordOptional.isPresent()) {
                byte[] pdfBytes = pdfService.generateMedicalRecordPdf(medicalRecordOptional.get());
                logger.info("Successfully generated PDF for record ID: {}", recordId);
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=medical_record_" + recordId + ".pdf")
                        .contentType(MediaType.APPLICATION_PDF)
                        .body(pdfBytes);
            } else {
                logger.warn("Medical record not found for ID: {}", recordId);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Error generating PDF for record ID: {}", recordId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

