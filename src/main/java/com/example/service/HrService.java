package com.example.service;

import com.example.dto.request.CreateHrRequest;
import com.example.dto.request.UpdateHrRequest;
import com.example.dto.response.HrResponse;
import com.example.model.constant.Status;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface HrService {

    HrResponse save(CreateHrRequest request);

    HrResponse update(UUID id, UpdateHrRequest request);

    HrResponse getByIdAndStatus(UUID id, Status confirmed);

    Page<HrResponse> getAllByStatus(Status status, Integer size, Integer number);

    Page<HrResponse> getAllByStatusAndCompany(String companyName, Status confirmed, Integer size, Integer number);

    HrResponse getById(UUID id);
}
