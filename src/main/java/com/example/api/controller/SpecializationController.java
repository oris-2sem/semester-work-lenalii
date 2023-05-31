package com.example.api.controller;

import com.example.api.SpecializationApi;
import com.example.dto.request.*;
import com.example.dto.response.MessageResponse;
import com.example.dto.response.SpecializationResponse;
import com.example.security.details.UserDetailsImpl;
import com.example.service.SpecializationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SpecializationController implements SpecializationApi {

    private final SpecializationService specializationService;

    @Override
    public List<SpecializationResponse> addList(SpecializationsListRequest request, UserDetailsImpl userDetails) {

        return specializationService.addList(request, userDetails.getAccount().getId());
    }

    @Override
    public MessageResponse deleteSpecializations(ListSpecializationsIdsRequest list, UserDetailsImpl userDetails) {

        specializationService.deleteAsList(list, userDetails.getAccount().getId());
        return MessageResponse.builder().message("Specializations successfully deleted").build();
    }
}
