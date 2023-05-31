package com.example.mapper;


import com.example.dto.response.PhotoResponse;
import com.example.model.entity.PhotoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PhotoMapper {

    @Mapping(source = "account.id", target = "accountId")
    PhotoResponse fromEntityToResponse(PhotoEntity savedPhoto);
}
