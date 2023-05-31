package com.example.service.impl;


import com.example.dto.model.MailMessageModel;
import com.example.dto.request.*;
import com.example.dto.response.AccountResponse;
import com.example.dto.response.PhotoResponse;
import com.example.exception.*;
import com.example.mapper.AccountMapper;
import com.example.mapper.DeveloperMapper;
import com.example.mapper.HrMapper;
import com.example.model.constant.Role;
import com.example.model.constant.Status;
import com.example.model.entity.AccountEntity;
import com.example.model.entity.ConfirmationTokenEntity;
import com.example.model.entity.RefreshTokenEntity;
import com.example.repository.AccountRepository;
import com.example.repository.DeveloperRepository;
import com.example.repository.HrRepository;
import com.example.repository.RefreshTokenRepository;
import com.example.security.model.AccessAndRefreshTokens;
import com.example.service.AccountService;
import com.example.service.ConfirmationTokenService;
import com.example.service.EmailSenderService;
import com.example.service.PhotoService;
import com.example.util.RequestParamUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static com.example.util.constants.Constants.LOCALHOST_8080;


@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    private final ConfirmationTokenService confirmationTokenService;

    private final AccountMapper accountMapper;

    private final PasswordEncoder passwordEncoder;

    private final RequestParamUtil requestParamUtil;

    private final DeveloperRepository developerRepository;

    private final DeveloperMapper developerMapper;

    private final HrRepository hrRepository;

    private final PhotoService photoService;

    private final EmailSenderService emailSenderService;

    private final HrMapper hrMapper;

    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional(readOnly = true)
    @Override
    public void checkAccountWithSuchEmailIsNotExist(String email) {

        Optional<AccountEntity> accountEntity = accountRepository.findByEmail(email);
        if (accountEntity.isPresent()) {
            throw new UserAlreadyExistException();
        }
    }

    @Transactional
    @Override
    public AccountResponse confirmAccount(String token) {

        ConfirmationTokenEntity confirmationToken = confirmationTokenService.getByToken(token);
        checkConfirmationTokenIsNotExpired(confirmationToken);

        AccountEntity account = confirmationToken.getAccount();

        account.setStatus(Status.CONFIRMED);

        confirmationTokenService.delete(confirmationToken);

        AccountEntity savedEntity = accountRepository.save(account);
        return convertToAccountResponse(savedEntity);
    }

    @Transactional
    @Override
    public AccountResponse save(CreateAccountRequest request) {

        AccountEntity account = accountMapper.fromRequestToEntity(request);
        checkAccountWithSuchEmailIsNotExist(request.getEmail());
        AccountEntity savedAccount = save(account);
        return accountMapper.fromEntityToResponse(savedAccount);
    }


    @Override
    public Page<AccountResponse> searchAccounts(String query, Integer size, Integer number) {

        PageRequest pageRequest = requestParamUtil.getPageRequest(size, number);

        Page<AccountEntity> page;

        String[] array = query.split(" ");
        if (array.length == 2) {

            page = accountRepository.findByFirstNameLikeOrLastNameLike(array[0] + '%', array[1] + '%', pageRequest);
            if (page.getContent().isEmpty()) {
                page = accountRepository.findByFirstNameLikeOrLastNameLike(array[1] + '%', array[0] + '%', pageRequest);
            }
        } else {
            page = accountRepository.findByFirstNameLike(array[0] + '%', pageRequest);
            if (page.getContent().isEmpty()) {
                page = accountRepository.findByLastNameLike(array[0] + '%', pageRequest);
            }
        }

        return page.map(this::convertToAccountResponse);
    }

    @Transactional
    @Override
    public AccountEntity save(AccountEntity accountEntity) {

        accountEntity.setStatus(Status.NOT_CONFIRMED);
        accountEntity.setHashPassword(passwordEncoder.encode(accountEntity.getHashPassword()));
        accountEntity.setConfirmationTokens(new ArrayList<>());
        accountEntity.setRefreshTokens(new ArrayList<>());
        AccountEntity account = accountRepository.save(accountEntity);

        ConfirmationTokenEntity tokenForAccount = confirmationTokenService
                .createTokenForAccount(account);

        emailSenderService.sendEmail(createMailMessageToConfirmAccount(tokenForAccount, account));

        return account;
    }

    public static MailMessageModel createMailMessageToConfirmAccount(ConfirmationTokenEntity tokenForAccount, AccountEntity account) {
        return MailMessageModel.builder()
                .to(account.getEmail())
                .message(LOCALHOST_8080 + "/account/confirm?token=" + tokenForAccount.getToken().toString())
                .subject("PLEASE CONFIRM YOUR ACCOUNT")
                .build();
    }

    @Transactional
    @Override
    public UUID delete(UUID id) {

        AccountEntity account = accountRepository.getReferenceById(id);
        account.setStatus(Status.DELETED);
        accountRepository.save(account);
        return account.getId();
    }

    @Transactional(readOnly = true)
    @Override
    public AccountResponse getById(UUID id) {

        AccountEntity accountEntity = accountRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);

        return convertToAccountResponse(accountEntity);
    }

    private void checkConfirmationTokenIsNotExpired(ConfirmationTokenEntity confirmationToken) {

        if (OffsetDateTime.now().isAfter(confirmationToken.getExpiredDate())) {
            throw new TokenExpiredException();
        }
    }

    @Transactional
    @Override
    public AccountResponse update(UUID accountId, UpdateAccountRequest request) {

        AccountEntity account = accountRepository.getReferenceById(accountId);
        accountMapper.updateAccountEntity(request, account);
        AccountEntity savedEntity = accountRepository.save(account);

        return convertToAccountResponse(savedEntity);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<AccountResponse> getAll(Integer size, Integer number) {

        PageRequest pageRequest = requestParamUtil.getPageRequest(size, number);

        Page<AccountEntity> page = accountRepository.findAll(pageRequest);

        return page.map(this::convertToAccountResponse);
    }

    @Override
    public AccountResponse convertToAccountResponse(AccountEntity account) {

        AccountResponse accountResponse;
        if (account.getRole().equals(Role.ROLE_HR)) {

            accountResponse = hrMapper.fromEntityToResponse(hrRepository.findById(account.getId()).orElseThrow(HrNotFoundException::new));
        } else if (account.getRole().equals(Role.ROLE_DEVELOPER)) {

            accountResponse = developerMapper.fromEntityToResponse(developerRepository.findById(account.getId())
                    .orElseThrow(UserNotFoundException::new));
        } else {
            accountResponse = accountMapper.fromEntityToResponse(account);
        }
        return setPhotoInResponse(account, accountResponse);
    }

    public AccountResponse setPhotoInResponse(AccountEntity accountEntity, AccountResponse accountResponse) {

        if (accountEntity.getPhoto() != null) {
            PhotoResponse photo = photoService.getPhotoResponse(accountEntity.getPhoto());
            accountResponse.setPhoto(photo);
        }
        return accountResponse;
    }


    @Transactional
    @Override
    public void saveRefreshToken(UUID accountId, AccessAndRefreshTokens tokens) {

        AccountEntity account = accountRepository
                .findById(accountId)
                .orElseThrow(UserNotFoundException::new);

        RefreshTokenEntity refreshToken = RefreshTokenEntity.builder()
                .value(tokens.getRefreshToken())
                .account(account)
                .build();

        refreshTokenRepository.save(refreshToken);

        account.getRefreshTokens().add(refreshToken);
        accountRepository.save(account);
    }
}
