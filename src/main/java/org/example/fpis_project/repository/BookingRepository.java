package org.example.fpis_project.repository;

import org.example.fpis_project.model.entity.Booking;
import org.springframework.data.repository.CrudRepository;

public interface BookingRepository extends CrudRepository<Booking, Long> {
}
