package com.hms.medicalrecordmicroservice.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import com.hms.medicalrecordmicroservice.model.MedicalRecord;
import com.hms.medicalrecordmicroservice.repository.MedicalRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class MedicalRecordServiceTest {

    @Mock
    private MedicalRecordRepository medicalRecordRepository;

    @Mock
    private Cloudinary cloudinary;

    @Mock
    private Uploader uploader;

    @Mock
    private MultipartFile file;

    @InjectMocks
    private MedicalRecordService medicalRecordService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(cloudinary.uploader()).thenReturn(uploader);
    }

    @Test
    public void testSaveMedicalRecord_WithFile() throws IOException {
        MedicalRecord medicalRecord = new MedicalRecord();
        Map<String, Object> uploadResult = Map.of("url", "http://cloudinary.com/image.jpg");

        when(file.getBytes()).thenReturn(new byte[0]);
        when(uploader.upload(any(byte[].class), any(Map.class))).thenReturn(uploadResult);
        when(medicalRecordRepository.save(any(MedicalRecord.class))).thenReturn(medicalRecord);

        MedicalRecord result = medicalRecordService.saveMedicalRecord(medicalRecord, file);

        assertNotNull(result);
        assertEquals("http://cloudinary.com/image.jpg", medicalRecord.getImages());
        verify(medicalRecordRepository, times(1)).save(medicalRecord);
    }

    @Test
    public void testSaveMedicalRecord_WithoutFile() throws IOException {
        MedicalRecord medicalRecord = new MedicalRecord();

        when(medicalRecordRepository.save(any(MedicalRecord.class))).thenReturn(medicalRecord);

        MedicalRecord result = medicalRecordService.saveMedicalRecord(medicalRecord, null);

        assertNotNull(result);
        assertNull(medicalRecord.getImages());
        verify(medicalRecordRepository, times(1)).save(medicalRecord);
        verify(cloudinary, never()).uploader();
    }

    @Test
    public void testGetAllMedicalRecords() {
        List<MedicalRecord> medicalRecords = Arrays.asList(new MedicalRecord(), new MedicalRecord());

        when(medicalRecordRepository.findAll()).thenReturn(medicalRecords);

        List<MedicalRecord> result = medicalRecordService.getAllMedicalRecords();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(medicalRecordRepository, times(1)).findAll();
    }

    @Test
    public void testGetMedicalRecordById() {
        MedicalRecord medicalRecord = new MedicalRecord();

        when(medicalRecordRepository.findById(anyLong())).thenReturn(Optional.of(medicalRecord));

        MedicalRecord result = medicalRecordService.getMedicalRecordById(1L);

        assertNotNull(result);
        verify(medicalRecordRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetMedicalRecordById_NotFound() {
        when(medicalRecordRepository.findById(anyLong())).thenReturn(Optional.empty());

        MedicalRecord result = medicalRecordService.getMedicalRecordById(1L);

        assertNull(result);
        verify(medicalRecordRepository, times(1)).findById(1L);
    }

    @Test
    public void testDeleteMedicalRecord() {
        doNothing().when(medicalRecordRepository).deleteById(anyLong());

        medicalRecordService.deleteMedicalRecord(1L);

        verify(medicalRecordRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testGetMedicalRecordsByPatientId() {
        List<MedicalRecord> medicalRecords = Arrays.asList(new MedicalRecord(), new MedicalRecord());

        when(medicalRecordRepository.findByPatientId(anyLong())).thenReturn(medicalRecords);

        List<MedicalRecord> result = medicalRecordService.getMedicalRecordsByPatientId(1L);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(medicalRecordRepository, times(1)).findByPatientId(1L);
    }
}
