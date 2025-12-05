package com.embarkx.jobms.job.impl;

import com.embarkx.jobms.external.Company;
import com.embarkx.jobms.job.Job;
import com.embarkx.jobms.job.JobRepository;
import com.embarkx.jobms.job.JobService;
import com.embarkx.jobms.job.dto.JobWithCompanyDTO;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;

    @Override
    public List<JobWithCompanyDTO> findAll() {
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
    public Job getJobById(Long id) {
        return jobRepository.findById(id).orElse(null);
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

    private JobWithCompanyDTO toDto(Job job) {
        RestTemplate restTemplate = new RestTemplate();
        Company company = restTemplate.getForObject("http://localhost:8081/companies/" + job.getCompanyId(), Company.class);
        return JobWithCompanyDTO.builder()
                .job(job)
                .company(company)
                .build();
    }
}
