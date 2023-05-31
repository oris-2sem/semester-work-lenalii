package com.example.exception.handler;


import com.example.api.controller.CompanyController;
import com.example.api.controller.ModeratorController;
import com.example.api.controller.SpecializationController;
import com.example.api.controller.TagController;
import com.example.controller.AdminController;
import com.example.controller.DeveloperController;
import com.example.controller.RefreshTokenController;
import com.example.controller.VacancyReplyController;
import com.example.dto.response.ExceptionResponse;
import com.example.dto.response.ResponseErrorMessage;
import com.example.exception.BadRequestException;
import com.example.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice(assignableTypes = {CompanyController.class,
        ModeratorController.class, SpecializationController.class, TagController.class, DeveloperController.class, AdminController.class,
        RefreshTokenController.class, VacancyReplyController.class})
public class GlobalExceptionHandlerRest {


    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public <T extends NotFoundException> ExceptionResponse handleNotFoundException(T exception) {

        return ExceptionResponse.builder()
                .message(exception.getMessage())
                .build();
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public <T extends BadRequestException> ExceptionResponse handleAlreadyExistException(T exception) {
        return ExceptionResponse.builder()
                .message(exception.getMessage())
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public final ResponseErrorMessage handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return ResponseErrorMessage.builder()
                .status(HttpStatus.BAD_REQUEST)
                .errors(ex.getBindingResult()
                        .getFieldErrors()
                        .stream()
                        .map(fieldError ->
                                new ResponseErrorMessage.Error(fieldError.getField(),
                                        fieldError.getCode(), fieldError.getDefaultMessage()))
                        .collect(Collectors.toList()))
                .build();
    }

}
