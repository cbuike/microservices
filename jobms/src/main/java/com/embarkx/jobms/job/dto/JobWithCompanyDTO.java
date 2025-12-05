package com.embarkx.jobms.job.dto;

import com.embarkx.jobms.external.Company;
import com.embarkx.jobms.job.Job;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JobWithCompanyDTO {
    private Job job;
    private Company company;
}
