package org.example.fpis_project.repository;

import org.example.fpis_project.model.dto.BusinessDto;
import org.example.fpis_project.model.entity.Business;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BusinessRepository extends JpaRepository<Business, Long> {

}
