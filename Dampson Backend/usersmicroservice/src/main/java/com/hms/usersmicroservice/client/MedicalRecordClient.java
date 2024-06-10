package com.hms.usersmicroservice.client;

import com.hms.usersmicroservice.dto.MedicalRecord;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@FeignClient(name = "medicalrecordsmicroservice", url = "http://localhost:7006")
public interface MedicalRecordClient {
    @GetMapping("/medicalrecords")
    List<MedicalRecord> getAllMedicalRecords();

    @GetMapping("/medicalrecords/{id}")
    MedicalRecord getMedicalRecordById(@PathVariable("id") Long id);

    @GetMapping("/medicalrecords/patient/{patientId}")
    List<MedicalRecord> getMedicalRecordsByPatientId(@PathVariable("patientId") Long patientId);

    @PostMapping(value = "/medicalrecords", consumes = {"multipart/form-data"})
    ResponseEntity<MedicalRecord> createMedicalRecord(@RequestPart("file") MultipartFile file, @RequestPart("medicalRecord") String medicalRecordJson);

}