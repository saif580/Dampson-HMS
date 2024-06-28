package com.hms.usersmicroservice.service;

import com.hms.usersmicroservice.dto.MedicalRecord;
import com.hms.usersmicroservice.dto.Patient;
import com.hms.usersmicroservice.entity.Clinic;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;

@Service
public class PdfService {

    public byte[] generatePrescriptionPdf(Patient patient, Clinic clinic, MedicalRecord medicalRecord) throws IOException, ParseException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(byteArrayOutputStream);
        PdfDocument pdfDocument = new PdfDocument(writer);
        Document document = new Document(pdfDocument);

        // Format the date of birth and calculate age
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        LocalDate birthDate = dateFormat.parse(patient.getDateOfBirth()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int age = Period.between(birthDate, LocalDate.now()).getYears();

        // Title
        Paragraph title = new Paragraph("Prescription")
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(20)
                .setBold()
                .setMarginBottom(20);
        document.add(title);

        // Patient Info Table
        Table patientTable = new Table(UnitValue.createPercentArray(new float[]{1, 3}))
                .useAllAvailableWidth()
                .setMarginBottom(20);

        patientTable.addCell(createCell("Patient ID", true));
        patientTable.addCell(createCell(String.valueOf(patient.getPatientId()), false));
        patientTable.addCell(createCell("First Name", true));
        patientTable.addCell(createCell(patient.getFirstName(), false));
        patientTable.addCell(createCell("Last Name", true));
        patientTable.addCell(createCell(patient.getLastName(), false));
        patientTable.addCell(createCell("Phone", true));
        patientTable.addCell(createCell(patient.getPhone(), false));
        patientTable.addCell(createCell("Patient Age", true));
        patientTable.addCell(createCell(String.valueOf(age), false));


        document.add(patientTable);

        // Clinic Info Table
        Table clinicTable = new Table(UnitValue.createPercentArray(new float[]{1, 3}))
                .useAllAvailableWidth()
                .setMarginBottom(20);

        clinicTable.addCell(createCell("Clinic Name", true));
        clinicTable.addCell(createCell(clinic.getClinicName(), false));
        clinicTable.addCell(createCell("Doctor Name", true));
        clinicTable.addCell(createCell(clinic.getDoctorName(), false));
        clinicTable.addCell(createCell("Address", true));
        clinicTable.addCell(createCell(clinic.getAddress(), false));
        clinicTable.addCell(createCell("Phone Number", true));
        clinicTable.addCell(createCell(clinic.getContactNumber(), false));
        clinicTable.addCell(createCell("Speciality", true));
        clinicTable.addCell(createCell(clinic.getClinicSpeciality(), false));
        clinicTable.addCell(createCell("Operating Days", true));
        clinicTable.addCell(createCell(clinic.getOperatingDays(), false));

        document.add(clinicTable);

        // Medical Record Info Table
        Table medicalRecordTable = new Table(UnitValue.createPercentArray(new float[]{1, 3}))
                .useAllAvailableWidth()
                .setMarginBottom(20);

        medicalRecordTable.addCell(createCell("Diagnosis", true));
        medicalRecordTable.addCell(createCell(medicalRecord.getDiagnosis(), false));
        medicalRecordTable.addCell(createCell("Prescription", true));
        medicalRecordTable.addCell(createCell(medicalRecord.getPrescription(), false));
        medicalRecordTable.addCell(createCell("Tests", true));
        medicalRecordTable.addCell(createCell(medicalRecord.getTests(), false));
        medicalRecordTable.addCell(createCell("Date", true));
        medicalRecordTable.addCell(createCell(medicalRecord.getDate().toString(), false));

        document.add(medicalRecordTable);

        // Footer
        Paragraph footer = new Paragraph("End of Prescription")
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(12)
                .setMarginTop(30);
        document.add(footer);

        document.close();
        return byteArrayOutputStream.toByteArray();
    }

    private Cell createCell(String content, boolean isHeader) {
        Cell cell = new Cell().add(new Paragraph(content));
        if (isHeader) {
            cell.setBackgroundColor(ColorConstants.LIGHT_GRAY)
                    .setBorder(new SolidBorder(ColorConstants.BLACK, 1))
                    .setBold();
        } else {
            cell.setBorder(Border.NO_BORDER);
        }
        return cell;
    }
}
