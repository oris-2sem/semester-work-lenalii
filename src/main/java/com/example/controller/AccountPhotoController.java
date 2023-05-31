package com.example.controller;


import com.example.dto.request.PhotoRequest;
import com.example.dto.response.PhotoResponse;
import com.example.model.constant.Role;
import com.example.security.details.UserDetailsImpl;
import com.example.service.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@PreAuthorize("hasAnyRole('ROLE_DEVELOPER', 'ROLE_HR')")
@Controller
@RequiredArgsConstructor
@RequestMapping("/photo")
public class AccountPhotoController {

    private final PhotoService photoService;

    @PostMapping
    public String add(@ModelAttribute PhotoRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails, Model model) {

        PhotoResponse photoResponse = photoService.uploadPhoto(request, userDetails.getAccount().getId());
        model.addAttribute("photo", photoResponse);
       if (userDetails.getAccount().getRole().equals(Role.ROLE_DEVELOPER)){
           return "redirect:/developer";
       }else{
           return "redirect:/hr/account";
       }
    }

    @DeleteMapping
    public String delete(@AuthenticationPrincipal UserDetailsImpl userDetails) {

        photoService.delete(userDetails.getAccount().getId());
        if (userDetails.getAccount().getRole().equals(Role.ROLE_DEVELOPER)){
            return "redirect:/developer";
        }else{
            return "redirect:/hr/account";
        }
    }
}
