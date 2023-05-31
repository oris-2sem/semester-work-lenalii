package com.example.mapper;


import com.example.dto.request.CreateAccountRequest;
import com.example.dto.request.UpdateAccountRequest;
import com.example.dto.response.AccountResponse;
import com.example.model.entity.AccountEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    AccountResponse fromEntityToResponse(AccountEntity accountEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateAccountEntity(UpdateAccountRequest request, @MappingTarget AccountEntity accountEntity);

    @Mapping(source = "password", target = "hashPassword")
    AccountEntity fromRequestToEntity(CreateAccountRequest request);
}
