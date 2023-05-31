package com.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.List;


@Getter
@Builder
@Setter
public class ResponseErrorMessage {

    private final long timestamp = Instant.now().getEpochSecond();
    private HttpStatus status;

    private final List<Error> errors;

    @AllArgsConstructor
    @Getter
    public static class Error {
        private final String fieldName;
        private final String code;
        private final String detailMessage;
    }
}
