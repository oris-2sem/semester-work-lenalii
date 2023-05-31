package com.example.mapper;


import com.example.dto.request.SpecializationRequest;
import com.example.dto.response.SpecializationResponse;
import com.example.model.entity.SpecializationEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SpecializationMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    SpecializationEntity fromRequestToEntity(SpecializationRequest request);

    @Mapping(source = "developer.id", target = "developerId")
    SpecializationResponse fromEntityToResponse(SpecializationEntity specializationEntity);

    List<SpecializationResponse> fromEntitiesToResponse(List<SpecializationEntity> entities);
}
