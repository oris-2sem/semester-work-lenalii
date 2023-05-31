package com.example.api.controller;

import com.example.api.CompanyApi;
import com.example.dto.request.CompanyRequest;
import com.example.dto.request.RequestID;
import com.example.dto.request.UpdateCompanyRequest;
import com.example.dto.response.CompanyResponse;
import com.example.dto.response.MessageResponse;
import com.example.model.constant.Status;
import com.example.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/company")
public class CompanyController implements CompanyApi {

    private final CompanyService companyService;

    @Override
    public CompanyResponse save(CompanyRequest companyRequest) {

        return companyService.save(companyRequest);
    }

    @Override
    public CompanyResponse update(UpdateCompanyRequest updateCompanyRequest) {

        return companyService.update(updateCompanyRequest);
    }

    @Override
    public MessageResponse delete(RequestID request) {

        UUID id = companyService.delete(request.getId());
        return MessageResponse.builder()
                .message(String.format("Company with id %s successfully deleted", id))
                .build();
    }

    @Override
    public CompanyResponse getById(UUID id) {

        return companyService.getById(id);
    }

    @Override
    public Page<CompanyResponse> getConfirmedCompanies(Integer size, Integer number) {

        return companyService.getCompanyWhereStatus(Status.CONFIRMED, size, number);
    }

    @Override
    public Page<CompanyResponse> getCompaniesByName(String query, Integer size, Integer number) {

        return companyService.getConfirmedCompaniesByName(query, size, number);
    }

    @Override
    public Page<CompanyResponse> getAll(Integer size, Integer number) {

        return companyService.getAll(size, number);
    }
}
