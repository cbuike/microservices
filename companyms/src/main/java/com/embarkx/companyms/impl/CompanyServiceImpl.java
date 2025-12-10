package com.embarkx.companyms.impl;

import com.embarkx.companyms.Company;
import com.embarkx.companyms.CompanyRepository;
import com.embarkx.companyms.CompanyService;
import com.embarkx.companyms.clients.ReviewClient;
import com.embarkx.companyms.dto.ReviewMessage;
import jakarta.ws.rs.NotFoundException;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final ReviewClient reviewClient;

    @Override
    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    @Override
    public boolean updateCompany(Company company, Long id) {
        Optional<Company> existingCompanyOptional  = companyRepository.findById(id);
        if (existingCompanyOptional.isPresent()) {
            Company companyToUpdate = existingCompanyOptional.get();
            companyToUpdate.setName(company.getName());
            companyToUpdate.setDescription(company.getDescription());
            companyRepository.save(companyToUpdate);
            return true;
        }
        return false;
    }

    @Override
    public void createCompany(Company company) {
        companyRepository.save(company);
    }

    @Override
    public boolean deleteCompany(Long id) {
        if (companyRepository.existsById(id)) {
            companyRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Company getCompanyById(Long id) {
        return companyRepository.findById(id).orElse(null);
    }

    @Override
    public void updateCompanyRating(ReviewMessage reviewMessage) {
        log.info("Message Description: {} ", reviewMessage.getDescription());
        Company company = companyRepository.findById(reviewMessage.getCompanyId())
                .orElseThrow(() ->
                        new NotFoundException("Company not found: " + reviewMessage.getCompanyId()));

        double averageRating = reviewClient.getAverageRatingForCompany(company.getId());
        company.setRating(averageRating);

        companyRepository.save(company);
    }
}
