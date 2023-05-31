package com.example.mapper;

import com.example.dto.request.UpdateVacancyRequest;
import com.example.dto.request.VacancyRequest;
import com.example.dto.response.VacancyResponse;
import com.example.model.entity.VacancyEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {SpecializationMapper.class, TagMapper.class})
public interface VacancyMapper {

    @Mapping(source = "status", target = "status", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    VacancyEntity fromRequestToEntity(VacancyRequest vacancyRequest);

    @Mapping(source = "company", target = "companyResponse")
    VacancyResponse fromEntityToResponse(VacancyEntity savedEntity);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(UpdateVacancyRequest request, @MappingTarget VacancyEntity vacancyEntity);
}
