package com.hms.usersmicroservice.repository;

import com.hms.usersmicroservice.entity.Clinic;
import com.hms.usersmicroservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClinicRepository extends JpaRepository<Clinic, Long> {
    List<Clinic> findByDoctor(User doctor);
}
