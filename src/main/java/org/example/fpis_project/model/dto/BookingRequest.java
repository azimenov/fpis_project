package org.example.fpis_project.model.dto;

import lombok.Data;

@Data
public class BookingRequest {

    private final Long userId;

    private final Long slotId;
}
