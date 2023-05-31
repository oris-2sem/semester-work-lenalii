package com.example.controller;


import com.example.dto.request.CreateDeveloperRequest;
import com.example.dto.request.UpdateDeveloperRequest;
import com.example.dto.response.DeveloperResponse;
import com.example.dto.response.DocumentResponse;
import com.example.model.constant.DeveloperState;
import com.example.model.constant.Status;
import com.example.security.details.UserDetailsImpl;
import com.example.service.DeveloperService;
import com.example.service.DocumentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequestMapping("/developer")
@Controller
@RequiredArgsConstructor
public class DeveloperController {

    private final DeveloperService developerService;

    @GetMapping("/sign-up/page")
    public String signUpPage(@ModelAttribute("createDeveloperRequest") CreateDeveloperRequest request) {

        return "developer/sign-up";
    }

    @PostMapping("/sign-up")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public DeveloperResponse register(@RequestBody @Valid CreateDeveloperRequest request) {

        return developerService.save(request);
    }

    @PreAuthorize("hasRole('ROLE_DEVELOPER')")
    @GetMapping("/update")
    public String update(Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("update developer page");
        DeveloperResponse developerResponse = developerService.findById(userDetails.getAccount().getId());
        model.addAttribute("developer", developerResponse);
        model.addAttribute("updateDeveloperRequest", developerResponse);

        List<DocumentResponse> documents = documentService.getDocumentsByDeveloper(developerResponse.getId());
        model.addAttribute("documents", documents);
        return "developer/developer-update-page";
    }

    @PreAuthorize("hasRole('ROLE_DEVELOPER')")
    @PatchMapping
    public String updateHandler(@Valid @ModelAttribute("updateDeveloperRequest") UpdateDeveloperRequest request, BindingResult result,Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        if (result.hasErrors()) {

            DeveloperResponse developerResponse = developerService.findById(userDetails.getAccount().getId());
            model.addAttribute("developer", developerResponse);
            model.addAttribute("updateDeveloperRequest", request);

            List<DocumentResponse> documents = documentService.getDocumentsByDeveloper(developerResponse.getId());
            model.addAttribute("documents", documents);
            return "developer/developer-update-page";
        }

        developerService.update(userDetails.getAccount().getId(), request);
        return "redirect:/developer";
    }

    @PreAuthorize("hasRole('ROLE_DEVELOPER')")
    @DeleteMapping
    public String delete(@AuthenticationPrincipal UserDetailsImpl userDetails) {

        developerService.delete(userDetails.getAccount().getId());
        return "redirect:/logout";
    }

    private final DocumentService documentService;

    @PreAuthorize("hasAnyRole('ROLE_DEVELOPER')")
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public String getThisDeveloper(@AuthenticationPrincipal UserDetailsImpl userDetails, Model model) {

        DeveloperResponse developerResponse = developerService.findByIdAndStatus(userDetails.getAccount().getId(), Status.CONFIRMED);
        model.addAttribute("developerResponse", developerResponse);

        List<DocumentResponse> documents = documentService.getDocumentsByDeveloper(developerResponse.getId());
        model.addAttribute("documents", documents);

        return "developer/developer-account-page";
    }

    @PreAuthorize("hasAnyRole('ROLE_HR')")
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public String getAllDevelopersByStateActive(@RequestParam(name = "size", required = false) Integer size, @RequestParam(name = "number", required = false)Integer number, Model model) {

        Page<DeveloperResponse> developerPage = developerService.findAllByStateAndStatus(size, number, DeveloperState.ACTIVE, Status.CONFIRMED);
        model.addAttribute("developerPage", developerPage);

        return "developer/developers";
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String getDeveloperById(@PathVariable("id")UUID id, Model model) {

        DeveloperResponse developerResponse = developerService.findById(id);
        model.addAttribute("developerResponse", developerResponse);

        List<DocumentResponse> documents = documentService.getDocumentsByDeveloper(developerResponse.getId());
        model.addAttribute("documents", documents);
        return "developer/developer-page";
    }

    @PreAuthorize("hasAnyRole('ROLE_HR')")
    @GetMapping("/by/document")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Page<DeveloperResponse> getDevelopersThatAddDocuments(@RequestParam(name = "size", required = false) Integer size, @RequestParam(name = "number", required = false)Integer number, Model model) {

        return developerService.findDevelopersThatAddDocuments(size, number);
    }
}
