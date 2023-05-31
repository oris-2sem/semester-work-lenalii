package com.example.service;

import com.example.model.entity.AccountEntity;
import com.example.model.entity.ConfirmationTokenEntity;

public interface ConfirmationTokenService {

    ConfirmationTokenEntity createTokenForAccount(AccountEntity account);

    ConfirmationTokenEntity getByToken(String token);

    void delete(ConfirmationTokenEntity confirmationToken);

    void deleteExpiredTokens();
}
