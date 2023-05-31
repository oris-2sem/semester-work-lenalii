package com.example.mapper;


import com.example.dto.response.DocumentResponse;
import com.example.model.entity.DocumentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DocumentMapper {

    @Mapping(source ="developer.id", target = "developerId")
    DocumentResponse fromEntityToResponse(DocumentEntity savedEntity);

    @Mapping(source ="developer.id", target = "developerId")
    List<DocumentResponse> fromEntityToResponse(List<DocumentEntity> savedEntity);

}
