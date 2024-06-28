package com.hms.medicalrecordmicroservice.client;

import com.hms.medicalrecordmicroservice.config.FeignClientConfig;
import com.hms.medicalrecordmicroservice.dto.Clinic;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "usersmicroservice", configuration = FeignClientConfig.class)
public interface ClinicClient {

    @GetMapping("/clinics/{id}")
    Clinic getClinicById(@PathVariable("id") Long id);
}
