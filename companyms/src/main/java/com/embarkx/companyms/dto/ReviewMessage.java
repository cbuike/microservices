package com.embarkx.companyms.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReviewMessage {
    private Long id;
    private String title;
    private String description;
    private double rating;
    private Long companyId;
}
