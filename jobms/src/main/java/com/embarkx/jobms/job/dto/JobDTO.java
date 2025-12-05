package com.embarkx.jobms.job.dto;

import com.embarkx.jobms.external.Company;
import com.embarkx.jobms.external.Review;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JobDTO {

    private Long id;
    private String title;
    private String description;
    private String minSalary;
    private String maxSalary;
    private String location;
    private String companyId;
    private Company company;
    private List<Review> reviews;
}
