package com.example.service;

import com.example.dto.request.*;
import com.example.dto.response.AccountResponse;
import com.example.model.entity.AccountEntity;
import com.example.security.model.AccessAndRefreshTokens;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface AccountService {

    void checkAccountWithSuchEmailIsNotExist(String email);

    AccountResponse confirmAccount(String token);

    AccountEntity save(AccountEntity accountEntity);

    UUID delete(UUID id);

    AccountResponse getById(UUID id);

    AccountResponse convertToAccountResponse(AccountEntity account);

    AccountResponse setPhotoInResponse(AccountEntity accountEntity, AccountResponse accountResponse);

    AccountResponse update(UUID account, UpdateAccountRequest request);

    Page<AccountResponse> getAll(Integer size, Integer number);

    AccountResponse save(CreateAccountRequest request);

    Page<AccountResponse> searchAccounts(String query, Integer size, Integer number);

    void saveRefreshToken(UUID accountId, AccessAndRefreshTokens tokens);
}
