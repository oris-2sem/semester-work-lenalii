package com.example.service.impl;

import com.example.dto.request.ListSpecializationsIdsRequest;
import com.example.dto.request.SpecializationsListRequest;
import com.example.dto.response.SpecializationResponse;
import com.example.exception.*;
import com.example.mapper.DeveloperMapper;
import com.example.mapper.SpecializationMapper;
import com.example.model.entity.DeveloperEntity;
import com.example.model.entity.SpecializationEntity;
import com.example.repository.DeveloperRepository;
import com.example.repository.SpecializationRepository;
import com.example.service.DeveloperService;
import com.example.service.SpecializationService;
import com.example.util.RequestParamUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SpecializationServiceImpl implements SpecializationService {

    private final SpecializationRepository specializationRepository;

    private final SpecializationMapper specializationMapper;

    private final RequestParamUtil requestParamUtil;

    private final DeveloperMapper developerMapper;

    private final DeveloperRepository developerRepository;

    private final DeveloperService developerService;

    @Transactional
    @Override
    public List<SpecializationResponse> addList(SpecializationsListRequest request, UUID developerId) {

        DeveloperEntity developer = developerRepository.findById(developerId).orElseThrow(DeveloperNotFoundException::new);

        List<SpecializationEntity> specializationEntities =
                request.getSpecializations()
                        .stream()
                        .map(specializationRequest -> {

                            specializationRequest.setName(specializationRequest
                                    .getName()
                                    .toLowerCase(Locale.ROOT));

                            checkDeveloperNotAddedThisYet(specializationRequest.getName(), developer);
                            SpecializationEntity specializationEntity = specializationMapper
                                    .fromRequestToEntity(specializationRequest);
                            specializationEntity.setDeveloper(developer);

                            developer.getSpecializations().add(specializationEntity);
                            return specializationEntity;
                        }).collect(Collectors.toList());

        List<SpecializationEntity> specializations = specializationRepository.saveAll(specializationEntities);
        developerRepository.save(developer);
        return specializationMapper.fromEntitiesToResponse(specializations);
    }

    @Transactional
    @Override
    public void deleteAsList(ListSpecializationsIdsRequest list, UUID developerId) {

        DeveloperEntity developer = developerRepository.getReferenceById(developerId);

        List<UUID> listSpecializations = developer.getSpecializations()
                .stream()
                .map(SpecializationEntity::getId)
                .toList();

        list.getIds().forEach(id -> {
            if(!listSpecializations.contains(id)){
                throw new SpecializationNotExistException(id);
            }
            SpecializationEntity specialization = specializationRepository.getReferenceById(id);

            specializationRepository.delete(specialization);
            developer.getSpecializations().remove(specialization);
        });
        developerRepository.save(developer);
    }

    private void checkDeveloperNotAddedThisYet(String name, DeveloperEntity developer) {

        Optional<SpecializationEntity> specializationEntity = specializationRepository
                .findAllByNameAndDeveloper(name, developer);
        if (specializationEntity.isPresent()) {
            throw new SpecializationAlreadyAddedException(name);
        }
    }
}
