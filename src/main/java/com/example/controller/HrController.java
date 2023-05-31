package com.example.controller;

import com.example.dto.request.CreateHrRequest;
import com.example.dto.request.UpdateHrRequest;
import com.example.dto.response.HrResponse;
import com.example.model.constant.Status;
import com.example.security.details.UserDetailsImpl;
import com.example.service.AccountService;
import com.example.service.HrService;
import com.example.service.VacancyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;


@Controller
@RequiredArgsConstructor
@RequestMapping("/hr")
public class HrController {

    private final HrService hrService;

    private final AccountService accountService;

    private final VacancyService vacancyService;

    @GetMapping("/sign-up/page")
    public String signUp(Model model) {

        model.addAttribute("createHrRequest", new CreateHrRequest());
        return "hr/sign-up";
    }

    @PostMapping("/sign-up")
    public String signUpHandler(@Valid @ModelAttribute("createHrRequest") CreateHrRequest request, BindingResult result, Model model) {
        if(result.hasErrors()){

            model.addAttribute("createHrRequest", request);
            return "hr/sign-up";
        }
        hrService.save(request);
        return "redirect:/account/check-email";
    }

    @PreAuthorize("hasRole('ROLE_HR')")
    @GetMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    public String update(Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        HrResponse hrResponse = hrService.getById(userDetails.getAccount().getId());
        model.addAttribute("updateHrRequest", hrResponse);
        model.addAttribute("hrResponse", hrResponse);
        return "hr/hr-update-page";
    }

    @PreAuthorize("hasRole('ROLE_HR')")
    @PatchMapping
    public String updateHandler(@Valid @ModelAttribute("updateHrRequest") UpdateHrRequest request, BindingResult result, Model model,  @AuthenticationPrincipal UserDetailsImpl userDetails) {

        if(result.hasErrors()){
            HrResponse hrResponse = hrService.getById(userDetails.getAccount().getId());
            model.addAttribute("updateHrRequest", hrResponse);
            model.addAttribute("hrResponse", hrResponse);
            return "hr/hr-update-page";
        }
        hrService.update(userDetails.getAccount().getId(), request);
        return "redirect:/hr";
    }

    @PreAuthorize("hasAnyRole('ROLE_HR', 'ROLE_MODERATOR', 'ROLE_ADMIN')")
    @GetMapping("/account")
    @ResponseStatus(HttpStatus.OK)
    public String getThisHr(@AuthenticationPrincipal UserDetailsImpl userDetails, Model model) {

        HrResponse hrResponse = hrService.getById(userDetails.getAccount().getId());
        model.addAttribute("hrResponse", hrResponse);
        return "hr/hr-account-page";
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ROLE_HR')")
    public String delete(@AuthenticationPrincipal UserDetailsImpl userDetails) {

        accountService.delete(userDetails.getAccount().getId());
        return "redirect:/logout";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping
    public String getAll(@RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "number", required = false) Integer number, Model model) {

        Page<HrResponse> hrsPage = hrService.getAllByStatus(Status.CONFIRMED, size, number);
        model.addAttribute("hrsPage", hrsPage);

        return "hr/hrs";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/by/company")
    public String getAllByCompany(@RequestParam("name") String companyName, Integer size, Integer number, Model model) {

        Page<HrResponse> hrsPage = hrService.getAllByStatusAndCompany(companyName, Status.CONFIRMED, size, number);
        model.addAttribute("hrsPage", hrsPage);

        return "hr/hrs";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public String getById(@PathVariable("id") UUID id, Model model) {

        HrResponse hrResponse = hrService.getByIdAndStatus(id, Status.CONFIRMED);
        model.addAttribute("hrResponse", hrResponse);
        return "hr/hr-page";
    }
}
