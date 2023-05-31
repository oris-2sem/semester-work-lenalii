package com.example.api.controller;

import com.example.api.ModeratorApi;
import com.example.dto.request.RequestID;
import com.example.dto.response.CompanyResponse;
import com.example.model.constant.Status;
import com.example.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class ModeratorController implements ModeratorApi {

    private final CompanyService companyService;

    @Override
    public Page<CompanyResponse> getModeratedCompanies(Integer size, Integer number) {

        return companyService.getCompanyWhereStatus(Status.MODERATED, size, number);
    }

    @Override
    public CompanyResponse confirmCompany(RequestID request) {

        return companyService.updateStatus(request.getId(), Status.CONFIRMED);
    }

    @Override
    public CompanyResponse blockCompany(RequestID request) {

        return companyService.updateStatus(request.getId(), Status.BLOCKED);
    }
}
