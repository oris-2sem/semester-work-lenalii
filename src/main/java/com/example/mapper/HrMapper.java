package com.example.mapper;


import com.example.dto.request.CreateHrRequest;
import com.example.dto.request.UpdateHrRequest;
import com.example.dto.response.HrResponse;
import com.example.model.entity.HrEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface HrMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "password", target = "hashPassword")
    HrEntity fromRequestToEntity(CreateHrRequest request);

    @Mapping(source = "company", target = "companyResponse")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    HrResponse fromEntityToResponse(HrEntity hrEntity);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateHrEntity(UpdateHrRequest request, @MappingTarget HrEntity hrEntity);
}
