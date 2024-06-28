package com.hms.usersmicroservice.client;

import com.hms.usersmicroservice.dto.Billing;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "billingmicroservice")
public interface BillingClient {

    @PostMapping("/billings/create")
    Billing createBilling(@RequestBody Billing billing);

    @GetMapping("/billings/{billingId}")
    Billing getBilling(@PathVariable("billingId") Long billingId);

    @GetMapping("/billings/patient/{patientId}")
    List<Billing> getBillingsByPatientId(@PathVariable("patientId") Long patientId);

    @PutMapping("/billings/{billingId}")
    Billing updateBilling(@PathVariable("billingId") Long billingId, @RequestBody Billing billing);

    @GetMapping("/billings/{billingId}/generatePdf")
    byte[] generateBillPdf(@PathVariable("billingId") Long billingId);
}