package com.example.service.impl;


import com.example.dto.response.AccountResponse;
import com.example.exception.AccountAlreadyBlockedException;
import com.example.exception.AccountIsNotBlocked;
import com.example.exception.UserNotFoundException;
import com.example.model.constant.DeveloperState;
import com.example.model.constant.Role;
import com.example.model.constant.Status;
import com.example.model.entity.AccountEntity;
import com.example.model.entity.DeveloperEntity;
import com.example.redis.service.RedisAccountService;
import com.example.repository.AccountRepository;
import com.example.repository.DeveloperRepository;
import com.example.service.AccountService;
import com.example.service.BlockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BlockServiceImpl implements BlockService {

    private final AccountRepository accountRepository;

    private final RedisAccountService redisAccountService;

    private final AccountService accountService;

    private final DeveloperRepository developerRepository;

    @Transactional
    @Override
    public AccountResponse unblockAccount(UUID id) {

        AccountEntity account = accountRepository.findById(id).orElseThrow(UserNotFoundException::new);
        checkAccountIsBlocked(account);

        if(account.getRole().equals(Role.ROLE_DEVELOPER)){

            DeveloperEntity developer = developerRepository.getReferenceById(account.getId());
            developer.setState(DeveloperState.ACTIVE);
        }

        account.setStatus(Status.CONFIRMED);
        AccountEntity unblockedAccount = accountRepository.save(account);

        return accountService.convertToAccountResponse(unblockedAccount);
    }

    private void checkAccountIsBlocked(AccountEntity account) {

        if(!account.getStatus().equals(Status.BLOCKED)){

            throw new AccountIsNotBlocked(account.getId());
        }
    }

    @Transactional
    @Override
    public AccountResponse blockAccount(UUID id) {

        AccountEntity account = accountRepository.findById(id).orElseThrow(UserNotFoundException::new);
        checkAccountIsNotBlocked(account);
        account.setStatus(Status.BLOCKED);

        if(account.getRole().equals(Role.ROLE_DEVELOPER)){

            DeveloperEntity developer = developerRepository.getReferenceById(account.getId());
            developer.setState(DeveloperState.NOT_ACTIVE);
        }

        AccountEntity blockedAccount = accountRepository.save(account);
        redisAccountService.addAllTokensToBlackList(account);

        return accountService.convertToAccountResponse(blockedAccount);
    }

    private void checkAccountIsNotBlocked(AccountEntity account) {

        if(account.getStatus().equals(Status.BLOCKED)){
            throw new AccountAlreadyBlockedException(account.getId());
        }
    }
}
