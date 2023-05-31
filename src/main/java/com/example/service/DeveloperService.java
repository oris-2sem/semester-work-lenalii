package com.example.service;

import com.example.dto.request.CreateDeveloperRequest;
import com.example.dto.request.UpdateDeveloperRequest;
import com.example.dto.response.DeveloperResponse;
import com.example.model.constant.DeveloperState;
import com.example.model.constant.Status;
import com.example.model.entity.DeveloperEntity;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface DeveloperService {

    DeveloperResponse convertToDeveloperResponseWithPhoto(DeveloperEntity developer);

    DeveloperResponse save(CreateDeveloperRequest request);

    DeveloperResponse update(UUID id, UpdateDeveloperRequest request);

    UUID delete(UUID id);

    DeveloperResponse findById(UUID id);

    Page<DeveloperResponse> findAllByStateAndStatus(Integer size, Integer number, DeveloperState confirmed, Status status);

    DeveloperResponse findByIdAndStatus(UUID id, Status confirmed);

    Page<DeveloperResponse> findDevelopersThatAddDocuments(Integer size, Integer number);
}

