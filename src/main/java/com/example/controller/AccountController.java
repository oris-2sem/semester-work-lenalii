package com.example.controller;

import com.example.dto.response.AccountResponse;
import com.example.model.constant.Role;
import com.example.security.details.UserDetailsImpl;
import com.example.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/check-email")
    public String accountCheckEmail() {

        return "account/check-email";
    }

    @GetMapping("/login")
    public String login() {

        return "account/login";
    }

    @GetMapping("/confirm")
    public String confirm(String token) {

        accountService.confirmAccount(token);
        return "account/confirmed";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public String getAccount(Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        AccountResponse accountResponse = accountService.getById(userDetails.getAccount().getId());
        model.addAttribute("accountResponse", accountResponse);
        return "account/account-page";
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String getById(@PathVariable("id") UUID id, Model model) {

        AccountResponse accountResponse = accountService.getById(id);

        if(accountResponse.getRole().equals(Role.ROLE_HR.toString())){
            model.addAttribute("hrResponse", accountResponse);
            return "hr/hr-page";
        }
        if(accountResponse.getRole().equals(Role.ROLE_DEVELOPER.toString())){
            model.addAttribute("developerResponse", accountResponse);
            return "developer/developer-page";
        }

        model.addAttribute("accountResponse", accountResponse);
        return "account/account-page";
    }

    @GetMapping("/info/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public AccountResponse getById(@PathVariable("id") UUID id) {

        return accountService.getById(id);
    }
}
