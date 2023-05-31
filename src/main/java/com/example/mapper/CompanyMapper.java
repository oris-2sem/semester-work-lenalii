package com.example.mapper;


import com.example.dto.request.CompanyRequest;
import com.example.dto.request.UpdateCompanyRequest;
import com.example.dto.response.CompanyResponse;
import com.example.model.entity.CompanyEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CompanyMapper {

    CompanyResponse fromEntityToResponse(CompanyEntity companyEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    CompanyEntity fromRequestToEntity(CompanyRequest companyRequest);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(UpdateCompanyRequest projectRequest, @MappingTarget CompanyEntity companyEntity);
}
