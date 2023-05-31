package com.example.api;

import com.example.dto.request.RequestID;
import com.example.dto.response.CompanyResponse;
import com.example.dto.response.ExceptionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@PreAuthorize("hasRole('ROLE_MODERATOR')")
@RequestMapping("/company")
public interface ModeratorApi {

    @Operation(summary = "get all company with status MODERATED and sort by company created date")
    @ApiResponse(responseCode = "200", description = "companies successfully received",
            content = @Content(schema = @Schema(implementation = Page.class)))
    @GetMapping("/moderated")
    @ResponseStatus(HttpStatus.OK)
    Page<CompanyResponse> getModeratedCompanies(@RequestParam(value = "size", required = false) Integer size,
                                                @RequestParam(value = "number", required = false) Integer number);

    @Operation(summary = "confirm company, company status change to CONFIRMED")
    @ApiResponse(responseCode = "202", description = "company successfully confirmed",
            content = @Content(schema = @Schema(implementation = CompanyResponse.class)))
    @ApiResponse(responseCode = "404", description = "company not found",
            content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    @PatchMapping("/confirm")
    @ResponseStatus(HttpStatus.ACCEPTED)
    CompanyResponse confirmCompany(@RequestBody RequestID request);

    @Operation(summary = "block company, company status change to BLOCKED")
    @ApiResponse(responseCode = "202", description = "company successfully blocked",
            content = @Content(schema = @Schema(implementation = CompanyResponse.class)))
    @ApiResponse(responseCode = "404", description = "company not found",
            content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    @PatchMapping("/block")
    @ResponseStatus(HttpStatus.ACCEPTED)
    CompanyResponse blockCompany(@RequestBody RequestID request);
}
