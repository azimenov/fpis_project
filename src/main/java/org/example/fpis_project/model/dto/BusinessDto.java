package org.example.fpis_project.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BusinessDto {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String address;

    @NotNull
    private String phone;

    @NotNull
    private String description;

    @NotNull
    private String topic;

    @NotNull
    private Long ownerId;

    private List<ServiceDto> services;

    private Double rating;

    private List<String> imageUrls;

    @JsonIgnore
    private transient List<MultipartFile> images;

}
