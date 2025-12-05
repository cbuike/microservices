package com.embarkx.jobms.job.mapper;

import com.embarkx.jobms.external.Company;
import com.embarkx.jobms.external.Review;
import com.embarkx.jobms.job.Job;
import com.embarkx.jobms.job.dto.JobDTO;
import java.util.List;

public class JobMapper {

    public static JobDTO toJobWithCompanyDTO(Job job, Company company, List<Review> reviews) {
        return JobDTO.builder()
                .id(job.getId())
                .title(job.getTitle())
                .description(job.getDescription())
                .minSalary(job.getMinSalary())
                .maxSalary(job.getMaxSalary())
                .location(job.getLocation())
                .company(company)
                .reviews(reviews)
                .build();
    }
}
