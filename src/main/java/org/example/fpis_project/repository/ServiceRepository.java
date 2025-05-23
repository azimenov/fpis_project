package org.example.fpis_project.repository;

import org.example.fpis_project.model.entity.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {

    List<Service> findAllByBusinessId(Long businessId);

    Service findServiceById(Long businessId);

    List<Service> findByNameContaining(String searchWord);

}
