package com.embarkx.jobms.job.impl;

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
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;
    private final RestTemplate restTemplate;

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
        Company company = restTemplate.getForObject("http://COMPANY-SERVICE/companies/" + job.getCompanyId(), Company.class);

        ResponseEntity<List<Review>> reviews = restTemplate
                .exchange("http://REVIEW-SERVICE/reviews?companyId=" + job.getCompanyId(),
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<Review>>() {});

        return JobMapper.toJobWithCompanyDTO(job, company, reviews.getBody());
    }
}
