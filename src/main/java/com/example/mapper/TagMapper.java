package com.example.mapper;

import com.example.dto.request.TagRequest;
import com.example.dto.response.TagResponse;
import com.example.model.entity.TagEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TagMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    TagEntity fromStringToEntity(String tag);

    @Mapping(source = "tag", target = "tag")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    List<TagEntity> fromStringToEntity(List<String> tag);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    TagEntity fromRequestToEntity(TagRequest request);

    @Mapping(source = "vacancy.id", target = "vacancyId")
    TagResponse fromEntityToResponse(TagEntity tagEntity);

    @Mapping(source = "vacancy.id", target = "vacancyId")
    List<TagResponse> fromEntityToResponse(List<TagEntity> tagsEntities);
}
