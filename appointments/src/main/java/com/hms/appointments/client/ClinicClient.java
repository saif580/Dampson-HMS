package com.hms.appointments.client;
import com.hms.appointments.dto.ClinicDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "usersmicroservice", url = "http://localhost:7000")
public interface ClinicClient {

    @GetMapping("/api/clinics/{clinicId}")
    ClinicDTO getClinicById(@PathVariable("clinicId") Long clinicId);
}
