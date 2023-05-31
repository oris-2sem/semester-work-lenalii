package com.example.controller;


import com.example.dto.request.UpdateVacancyRequest;
import com.example.dto.request.VacancyFilter;
import com.example.dto.request.VacancyRequest;
import com.example.dto.response.VacancyResponse;
import com.example.security.details.UserDetailsImpl;
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


@RequestMapping("/vacancy")
@Controller
@RequiredArgsConstructor
public class VacancyController {

    private final VacancyService vacancyService;

    @PreAuthorize("hasRole('ROLE_HR')")
    @GetMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public String add(@ModelAttribute("request") VacancyRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return "vacancy/add";
    }

    @PreAuthorize("hasRole('ROLE_HR')")
    @PostMapping
    public String addHandler(@Valid @ModelAttribute("request") VacancyRequest request, BindingResult result, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        if (result.hasErrors()) {
            return "vacancy/add";
        }
        vacancyService.save(request, userDetails.getAccount().getId());
        return "redirect:/vacancy/by/hr";
    }

    @PreAuthorize("hasRole('ROLE_HR')")
    @GetMapping("/update/{vacancy-id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String update(@PathVariable("vacancy-id") UUID vacancyId,
                         @AuthenticationPrincipal UserDetailsImpl userDetails,
                         Model model) {

        VacancyResponse vacancy = vacancyService.getById(vacancyId);
        model.addAttribute("request", vacancy);
        model.addAttribute("vacancy", vacancy);
        return "vacancy/update";
    }

    @PreAuthorize("hasRole('ROLE_HR')")
    @PatchMapping
    public String updateHandler(@ModelAttribute("request") @Valid UpdateVacancyRequest request, BindingResult result, Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        if (result.hasErrors()) {
            VacancyResponse vacancy = vacancyService.getById(request.getId());
            model.addAttribute("request", request);
            model.addAttribute("vacancy", vacancy);
            return "vacancy/update";
        }
        vacancyService.update(request, userDetails.getAccount().getId());
        return "redirect:/vacancy/" + request.getId();
    }

    @PreAuthorize("hasRole('ROLE_HR')")
    @DeleteMapping
    public String delete(@ModelAttribute("vacancyId") UUID vacancyId, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        vacancyService.delete(vacancyId, userDetails.getAccount().getId());

        return "redirect:/vacancy/by/hr";
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String getById(@PathVariable("id") UUID id, Model model) {

        VacancyResponse vacancy = vacancyService.getById(id);
        model.addAttribute("vacancy", vacancy);
        return "vacancy/page";
    }

    @GetMapping
    public String getAllHandler(@RequestParam(value = "size", required = false) Integer size,
                                @RequestParam(value = "number", required = false) Integer number,
                                Model model) {

        Page<VacancyResponse> vacancyPage = vacancyService.getAll(size, number);
        model.addAttribute("vacancyPage", vacancyPage);
        model.addAttribute("filter", new VacancyFilter());
        return "vacancy/vacancies";
    }

    @GetMapping("/by/company/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Page<VacancyResponse> getByCompany(@PathVariable("id") UUID companyId,
                                              @RequestParam(value = "size", required = false) Integer size,
                                              @RequestParam(value = "number", required = false) Integer number) {

        return vacancyService.getByCompany(companyId, size, number);
    }

    @PreAuthorize("hasRole('ROLE_HR')")
    @GetMapping("/by/hr")
    @ResponseStatus(HttpStatus.OK)
    public String getByHr(@AuthenticationPrincipal UserDetailsImpl userDetails,
                          @RequestParam(value = "size", required = false) Integer size,
                          @RequestParam(value = "number", required = false) Integer number,
                          Model model) {


        Page<VacancyResponse> vacancyPage = vacancyService.getByHr(userDetails.getAccount().getId(), size, number);
        model.addAttribute("vacancyPage", vacancyPage);
        return "vacancy/hr-vacancies";
    }

    @PostMapping("/by/filter")
    public String getByFilter(@ModelAttribute("filter") VacancyFilter filter,
                              @RequestParam(value = "size", required = false) Integer size,
                              @RequestParam(value = "number", required = false) Integer number,
                              Model model) {

        Page<VacancyResponse> vacancyPage = vacancyService.searchByFilter(filter, size, number);
        model.addAttribute("vacancyPage", vacancyPage);
        return "vacancy/vacancies";
    }
}
