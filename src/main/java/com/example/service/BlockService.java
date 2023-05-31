package com.example.service;

import com.example.dto.response.AccountResponse;

import java.util.UUID;

public interface BlockService {

    AccountResponse unblockAccount(UUID id);

    AccountResponse blockAccount(UUID request);
}
