package com.example.exception.handler;

import com.example.controller.*;
import com.example.dto.response.ResponseErrorMessage;
import com.example.exception.BadRequestException;
import com.example.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import java.sql.SQLException;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice(assignableTypes = {AccountController.class,
        AccountPhotoController.class,  DocumentController.class, HrController.class, MainController.class,
        VacancyController.class
})
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleAccessDeniedException(IllegalArgumentException exception, Model model){

        model.addAttribute("exceptionMessage", "Вы ввели не корректные данные");
        return "exception/page";
    }

    @ExceptionHandler(SQLException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleSqlException(SQLException exception, Model model){

        model.addAttribute("Грустно, но у нас что-то пошло не так, мы работаем над решением проблемы");
        return "exception/page";
    }



    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public <T extends NotFoundException> String handleNotFoundException(T exception, Model model) {


        model.addAttribute("exceptionMessage", exception.getMessage());
        return "exception/page";
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public <T extends BadRequestException> String handleAlreadyExistException(T exception, Model model) {

        model.addAttribute("exceptionMessage", exception.getMessage());
        return "exception/page";
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
