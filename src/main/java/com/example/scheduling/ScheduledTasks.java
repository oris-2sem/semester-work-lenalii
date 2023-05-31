package com.example.scheduling;



import com.example.service.ConfirmationTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class ScheduledTasks {

    private final ConfirmationTokenService confirmationTokenService;

    @Scheduled(fixedRate = 2592000000L)
    public void deleteTokens() {

        confirmationTokenService.deleteExpiredTokens();
    }
}
