package com.example.controller;


import com.example.dto.request.RequestID;
import com.example.dto.response.AccountResponse;


import com.example.service.AccountService;
import com.example.service.BlockService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@PreAuthorize("hasRole('ROLE_ADMIN')")
@Controller
@RequiredArgsConstructor
@RequestMapping("/account")
public class AdminController {

    private final AccountService accountService;

    private final BlockService blockService;

    @PatchMapping("/unblock")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @ResponseBody
    public AccountResponse unblockAccount(@RequestBody RequestID requestID) {

        return blockService.unblockAccount(requestID.getId());
    }

    @PatchMapping("/block")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @ResponseBody
    public AccountResponse blockAccount(@RequestBody RequestID request) {

        return blockService.blockAccount(request.getId());
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public String getAll(@RequestParam(value = "size", required = false) Integer size,
                         @RequestParam(value = "number", required = false) Integer number,
                         Model model) {

        Page<AccountResponse> accountPage = accountService.getAll(size, number);
        model.addAttribute("accountPage", accountPage);

        return "admin/accounts";
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Page<AccountResponse> search(@RequestParam(value = "query", required = false) String query,
                                        @RequestParam(value = "size", required = false) Integer size,
                                        @RequestParam(value = "number", required = false) Integer number) {

        return accountService.searchAccounts(query,size, number);
    }
}
