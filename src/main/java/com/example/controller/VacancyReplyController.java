package com.example.controller;

import com.example.dto.request.RejectVacancyRequest;
import com.example.dto.request.RequestID;
import com.example.dto.response.*;
import com.example.security.details.UserDetailsImpl;
import com.example.service.VacancyReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/vacancy/reply")
public class VacancyReplyController {

    private final VacancyReplyService vacancyReplyService;

    @PreAuthorize("hasRole('ROLE_DEVELOPER')")
    @PostMapping("/{vacancy-id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public MessageResponse replyToVacancy(@PathVariable("vacancy-id") UUID vacancyId,
                                          @AuthenticationPrincipal UserDetailsImpl userDetails) {

        vacancyReplyService.replyToVacancy(vacancyId, userDetails.getAccount().getId());

        return MessageResponse.builder()
                .message("Respond to a vacancy successfully")
                .build();
    }

    @PreAuthorize("hasRole('ROLE_DEVELOPER')")
    @GetMapping("/my")
    @ResponseStatus(HttpStatus.OK)
    public String getAllRepliesByDeveloper(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                           @RequestParam(value = "size", required = false) Integer size,
                                           @RequestParam(value = "number", required = false) Integer number,
                                           Model model) {
        Page<VacancyResponse> page = vacancyReplyService.getAllRepliesByDeveloper(userDetails.getAccount().getId(), size, number);
        model.addAttribute("vacancyPage", page);

        return "vacancy/developer-vacancies";
    }

    @PreAuthorize("hasRole('ROLE_HR')")
    @DeleteMapping("/reject")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @ResponseBody
    public RejectedReply rejectVacancyReply(@RequestBody RejectVacancyRequest request,
                                              @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return vacancyReplyService.rejectVacancyReply(request, userDetails.getAccount().getId());
    }

    @PreAuthorize("hasRole('ROLE_DEVELOPER')")
    @DeleteMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    @ResponseBody
    public MessageResponse deleteVacancyReply(@RequestBody RequestID request,
                                              @AuthenticationPrincipal UserDetailsImpl userDetails) {

        UUID vacancyId = vacancyReplyService.deleteVacancyReply(request.getId(), userDetails.getAccount().getId());

        return MessageResponse.builder()
                .message(vacancyId.toString())
                .build();
    }

    @PreAuthorize("hasRole('ROLE_HR')")
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public String getAllReplies(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                @RequestParam(value = "size", required = false) Integer size,
                                @RequestParam(value = "number", required = false) Integer number,
                                Model model) {

        Page<VacancyReplyResponse> vacancyPage = vacancyReplyService.getAllReplies(userDetails.getAccount().getId(), size, number);

        model.addAttribute("vacancyPage", vacancyPage);
        return "vacancy/hr-replies";
    }
}
