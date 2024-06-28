package com.axis.billingmicroservice.service;

import com.axis.billingmicroservice.entity.Billing;
import com.itextpdf.io.font.constants.StandardFonts;
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

@Service
public class PdfService {

    public byte[] generateBillPdf(Billing billing) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(byteArrayOutputStream);
        PdfDocument pdfDocument = new PdfDocument(writer);
        Document document = new Document(pdfDocument);

        // Title
        Paragraph title = new Paragraph("Billing Statement")
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(20)
                .setBold()
                .setMarginBottom(20);
        document.add(title);

        // Billing Info Table
        Table table = new Table(UnitValue.createPercentArray(new float[]{1, 3}))
                .useAllAvailableWidth()
                .setMarginBottom(20);

        table.addCell(createCell("Billing ID", true));
        table.addCell(createCell(billing.getBillingId().toString(), false));
        table.addCell(createCell("Clinic ID", true));
        table.addCell(createCell(billing.getClinicId().toString(), false));
        table.addCell(createCell("Patient ID", true));
        table.addCell(createCell(billing.getPatientId().toString(), false));
        table.addCell(createCell("Patient Name", true));
        table.addCell(createCell(billing.getPatientFirstName() + " " + billing.getPatientLastName(), false)); // Ensure these fields are set in Billing
        table.addCell(createCell("Patient Email", true));
        table.addCell(createCell(billing.getPatientEmail(), false)); // Ensure this field is set in Billing
        table.addCell(createCell("Amount", true));
        table.addCell(createCell(billing.getAmount().toString(), false));
        table.addCell(createCell("Payment Date", true));
        table.addCell(createCell(billing.getPaymentDate().toString(), false));
        table.addCell(createCell("Payment Method", true));
        table.addCell(createCell(billing.getPaymentMethod(), false));

        document.add(table);

        // Footer
        Paragraph footer = new Paragraph("Thank you for your business!")
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
            cell.setBorder(new SolidBorder(ColorConstants.BLACK, 1));
        }
        return cell;
    }
}
