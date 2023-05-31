package com.example.mapper;


import com.example.dto.request.CreateDeveloperRequest;
import com.example.dto.request.UpdateDeveloperRequest;
import com.example.dto.response.DeveloperResponse;
import com.example.model.entity.DeveloperEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = {AccountMapper.class, DocumentMapper.class, SpecializationMapper.class})
public interface DeveloperMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "password", target = "hashPassword")
    DeveloperEntity fromRequestToEntity(CreateDeveloperRequest request);

    DeveloperResponse fromEntityToResponse(DeveloperEntity developer);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateDeveloperEntity(UpdateDeveloperRequest request, @MappingTarget DeveloperEntity developerEntity);

    List<DeveloperResponse> fromEntitiesToResponses(List<DeveloperEntity> entities);
}
