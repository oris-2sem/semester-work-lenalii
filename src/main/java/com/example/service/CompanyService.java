package com.example.service;

import com.example.dto.request.CompanyRequest;
import com.example.dto.request.UpdateCompanyRequest;
import com.example.dto.response.CompanyResponse;

import com.example.model.constant.Status;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface CompanyService {

    Page<CompanyResponse> getCompanyWhereStatus(Status status, Integer size, Integer number);

    CompanyResponse updateStatus(UUID id, Status status);

    CompanyResponse save(CompanyRequest companyRequest);

    CompanyResponse update(UpdateCompanyRequest updateCompanyRequest);

    UUID delete(UUID id);

    CompanyResponse getById(UUID id);

    Page<CompanyResponse> getAll(Integer size, Integer number);

    void checkHrCompany(UUID hrId, UUID id);

    Page<CompanyResponse> getConfirmedCompaniesByName(String query, Integer size, Integer number);
}
