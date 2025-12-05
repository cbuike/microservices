package com.embarkx.jobms.job.impl;

import com.embarkx.jobms.clients.CompanyClient;
import com.embarkx.jobms.clients.ReviewClient;
import com.embarkx.jobms.external.Company;
import com.embarkx.jobms.external.Review;
import com.embarkx.jobms.job.Job;
import com.embarkx.jobms.job.JobRepository;
import com.embarkx.jobms.job.JobService;
import com.embarkx.jobms.job.dto.JobDTO;
import com.embarkx.jobms.job.mapper.JobMapper;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;
    private final CompanyClient companyClient;
    private final ReviewClient reviewClient;

    @Override
    public List<JobDTO> findAll() {
        List<Job> jobs = jobRepository.findAll();
        return jobs.stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public void createJob(Job job) {
        jobRepository.save(job);
    }

    @Override
    public JobDTO getJobById(Long id) {
        Optional<Job> jobOptional = jobRepository.findById(id);
        return jobOptional.map(this::toDto).orElse(null);
    }

    @Override
    public boolean deleteJobById(Long id) {
        try {
            jobRepository.deleteById(id);
            return true;
        }catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean updateJob(Long id, Job updatedJob) {
        Optional<Job> jobOptional = jobRepository.findById(id);
        if (jobOptional.isPresent()) {
           Job job = jobOptional.get();
           job.setTitle(updatedJob.getTitle());
           job.setDescription(updatedJob.getDescription());
           job.setMinSalary(updatedJob.getMinSalary());
           job.setMaxSalary(updatedJob.getMaxSalary());
           job.setLocation(updatedJob.getLocation());
           jobRepository.save(job);
           return true;
        }
        return false;
    }

    private JobDTO toDto(Job job) {
        Company company = companyClient.getCompany (Long.valueOf(job.getCompanyId()));
        List<Review> reviews = reviewClient.getReviews(Long.valueOf(job.getCompanyId()));
        return JobMapper.toJobWithCompanyDTO(job, company, reviews);
    }
}
