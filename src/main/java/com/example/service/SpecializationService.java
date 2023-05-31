package com.example.service;

import com.example.dto.request.ListSpecializationsIdsRequest;
import com.example.dto.request.SpecializationsListRequest;
import com.example.dto.response.SpecializationResponse;

import java.util.List;
import java.util.UUID;

public interface SpecializationService {

    List<SpecializationResponse> addList(SpecializationsListRequest request, UUID developerId);

    void deleteAsList(ListSpecializationsIdsRequest list, UUID id);
}
