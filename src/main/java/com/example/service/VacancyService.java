package com.example.service;

import com.example.dto.request.UpdateVacancyRequest;
import com.example.dto.request.VacancyFilter;
import com.example.dto.request.VacancyRequest;
import com.example.dto.response.VacancyResponse;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface VacancyService {

    VacancyResponse save(VacancyRequest vacancyRequest, UUID hrId);

    VacancyResponse update(UpdateVacancyRequest request, UUID hrId);

    UUID delete(UUID id, UUID hrId);

    VacancyResponse getById(UUID id);

    Page<VacancyResponse> getAll(Integer size, Integer number);

    Page<VacancyResponse> getByCompany(UUID companyId, Integer size, Integer number);

    Page<VacancyResponse> getByHr(UUID hrId, Integer size, Integer number);

    Page<VacancyResponse> searchByFilter(VacancyFilter filter, Integer size, Integer number);
}
