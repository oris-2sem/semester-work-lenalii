package com.example.controller;

import com.example.dto.request.DocumentRequest;
import com.example.security.details.UserDetailsImpl;
import com.example.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@RequestMapping("/document")
@Controller
@RequiredArgsConstructor
public class DocumentController{

    private final DocumentService documentService;

    @PreAuthorize("hasRole('ROLE_DEVELOPER')")
    @PostMapping("/upload")
    public String uploadDocument(@ModelAttribute("file") MultipartFile file,
                                           DocumentRequest request,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {

        documentService.uploadFile(file, request, userDetails.getAccount().getId());
        return "redirect:/developer";
    }

    @GetMapping("/download/{document-id}")
    public void downloadDocument(@PathVariable("document-id") UUID documentId, HttpServletResponse response) {

        documentService.downloadDocument(documentId, response);
    }

    @PreAuthorize("hasRole('ROLE_DEVELOPER')")
    @DeleteMapping
    public String delete(@ModelAttribute("documentId") UUID documentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        documentService.delete(documentId, userDetails.getAccount().getId());
        return "redirect:/developer";
    }
}
