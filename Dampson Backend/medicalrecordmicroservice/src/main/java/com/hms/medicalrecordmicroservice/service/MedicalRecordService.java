package com.hms.medicalrecordmicroservice.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.hms.medicalrecordmicroservice.model.MedicalRecord;
import com.hms.medicalrecordmicroservice.repository.MedicalRecordRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class MedicalRecordService {

    private static final Logger logger = LoggerFactory.getLogger(MedicalRecordService.class);

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    @Autowired
    private Cloudinary cloudinary;

    public MedicalRecord saveMedicalRecord(MedicalRecord medicalRecord, MultipartFile file) throws IOException {
        logger.debug("Saving medical record");
        if (file != null && !file.isEmpty()) {
            logger.debug("Uploading file to Cloudinary");
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            medicalRecord.setImages((String) uploadResult.get("url"));
        }
        return medicalRecordRepository.save(medicalRecord);
    }

    public String uploadFile(MultipartFile file) {
        try {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            return (String) uploadResult.get("url");
        } catch (IOException e) {
            throw new RuntimeException("Error uploading file", e);
        }
    }

    public List<MedicalRecord> getAllMedicalRecords() {
        logger.debug("Fetching all medical records");
        return medicalRecordRepository.findAll();
    }

    public MedicalRecord getMedicalRecordById(Long id) {
        logger.debug("Fetching medical record with ID: {}", id);
        return medicalRecordRepository.findById(id).orElse(null);
    }

    public void deleteMedicalRecord(Long id) {
        logger.debug("Deleting medical record with ID: {}", id);
        medicalRecordRepository.deleteById(id);
    }

    public List<MedicalRecord> getMedicalRecordsByPatientId(Long patientId) {
        logger.debug("Fetching medical records for patient ID: {}", patientId);
        return medicalRecordRepository.findByPatientId(patientId);
    }
}
