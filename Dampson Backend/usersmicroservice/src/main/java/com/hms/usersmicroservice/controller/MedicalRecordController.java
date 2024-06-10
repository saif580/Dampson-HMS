package com.hms.usersmicroservice.controller;

import com.hms.usersmicroservice.client.MedicalRecordClient;
import com.hms.usersmicroservice.client.PatientClient;
import com.hms.usersmicroservice.dto.MedicalRecord;
import com.hms.usersmicroservice.dto.Patient;
import com.hms.usersmicroservice.entity.Clinic;
import com.hms.usersmicroservice.repository.ClinicRepository;
import com.hms.usersmicroservice.service.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/medicalrecords")
public class MedicalRecordController {

    @Autowired
    private MedicalRecordClient medicalRecordClient;

    @Autowired
    private PatientClient patientClient;

    @Autowired
    private ClinicRepository clinicRepository;

    @Autowired
    private PdfService pdfService;

    @GetMapping
    public List<MedicalRecord> getAllMedicalRecords() {
        return medicalRecordClient.getAllMedicalRecords();
    }

    @GetMapping("/{id}")
    public MedicalRecord getMedicalRecordById(@PathVariable Long id) {
        return medicalRecordClient.getMedicalRecordById(id);
    }

    @GetMapping("/patient/{patientId}")
    public List<MedicalRecord> getMedicalRecordsByPatientId(@PathVariable Long patientId) {
        return medicalRecordClient.getMedicalRecordsByPatientId(patientId);
    }


    @PreAuthorize("hasRole('DOCTOR')")
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<MedicalRecord> createMedicalRecord(@RequestPart("file") MultipartFile file, @RequestPart("medicalRecord") String medicalRecordJson) {
        return medicalRecordClient.createMedicalRecord(file, medicalRecordJson);
    }

    @PreAuthorize("hasAnyRole('RECEPTIONIST', 'DOCTOR')")
    @GetMapping("/{recordId}/generatePdf")
    public ResponseEntity<byte[]> generateMedicalRecordPdf(@PathVariable Long recordId) throws IOException, ParseException {
        MedicalRecord medicalRecord = medicalRecordClient.getMedicalRecordById(recordId);
        Patient patient = patientClient.getPatientById(medicalRecord.getPatientId());

        // Fetching clinic information
        Clinic clinic = clinicRepository.findById(medicalRecord.getClinicId()).orElseThrow(() -> new RuntimeException("Clinic not found"));

        byte[] pdfBytes = pdfService.generatePrescriptionPdf(patient, clinic, medicalRecord);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=prescription_" + recordId + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }

}