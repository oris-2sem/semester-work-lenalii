package com.example.service.impl;

import com.example.exception.TokenNotFoundException;
import com.example.model.entity.AccountEntity;
import com.example.model.entity.ConfirmationTokenEntity;
import com.example.repository.ConfirmationTokenRepository;
import com.example.service.ConfirmationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConfirmationTokenServiceImpl implements ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;

    @Transactional
    @Override
    public ConfirmationTokenEntity createTokenForAccount(AccountEntity account) {

        ConfirmationTokenEntity token = ConfirmationTokenEntity
                .builder()
                .token(UUID.randomUUID())
                .account(account)
                .expiredDate(OffsetDateTime.now().plusMinutes(10))
                .build();

        account.getConfirmationTokens().add(token);
        return token;
    }

    @Transactional(readOnly = true)
    @Override
    public ConfirmationTokenEntity getByToken(String token) {

        return confirmationTokenRepository
                .findByToken(UUID.fromString(token))
                .orElseThrow(TokenNotFoundException::new);
    }

    @Transactional
    @Override
    public void delete(ConfirmationTokenEntity confirmationToken) {

        confirmationTokenRepository.delete(confirmationToken);
    }

    @Transactional
    @Override
    public void deleteExpiredTokens(){

        List<ConfirmationTokenEntity> tokens = confirmationTokenRepository
                .findConfirmationTokenEntitiesByExpiredDateLessThan(OffsetDateTime.now());
        confirmationTokenRepository.deleteAll(tokens);
    }
}
