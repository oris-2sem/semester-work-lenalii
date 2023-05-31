package com.example.api;


import com.example.dto.request.CompanyRequest;
import com.example.dto.request.RequestID;
import com.example.dto.request.UpdateCompanyRequest;
import com.example.dto.response.CompanyResponse;
import com.example.dto.response.ExceptionResponse;
import com.example.dto.response.MessageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RequestMapping("/company")
public interface CompanyApi {

    @Operation(summary = "add company")
    @ApiResponse(responseCode = "201", description = "company successfully added",
            content = @Content(schema = @Schema(implementation = CompanyResponse.class)))
    @ApiResponse(responseCode = "400", description = "company with this name already exist",
            content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CompanyResponse save(@RequestBody @Valid CompanyRequest companyRequest);

    @Operation(summary = "update company")
    @ApiResponse(responseCode = "202", description = "company successfully updated",
            content = @Content(schema = @Schema(implementation = CompanyResponse.class)))
    @ApiResponse(responseCode = "404", description = "company not found",
            content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    CompanyResponse update(@RequestBody @Valid UpdateCompanyRequest updateCompanyRequest);

    @Operation(summary = "admin delete company")
    @ApiResponse(responseCode = "202", description = "company successfully deleted",
            content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    @ApiResponse(responseCode = "404", description = "company not found",
            content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    MessageResponse delete(@RequestBody @Valid RequestID request);

    @Operation(summary = "get company by id")
    @ApiResponse(responseCode = "200", description = "company successfully received",
            content = @Content(schema = @Schema(implementation = CompanyResponse.class)))
    @ApiResponse(responseCode = "404", description = "company not found",
            content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    CompanyResponse getById(@PathVariable("id") UUID id);

    @Operation(summary = "get all confirmed companies")
    @ApiResponse(responseCode = "200", description = "companies successfully received",
            content = @Content(schema = @Schema(implementation = Page.class)))
    @GetMapping("/confirmed")
    @ResponseStatus(HttpStatus.OK)
    Page<CompanyResponse> getConfirmedCompanies(@RequestParam(value = "size", required = false) Integer size,
                                                @RequestParam(value = "number", required = false) Integer number);

    @Operation(summary = "get companies by name")
    @ApiResponse(responseCode = "200", description = "companies successfully received",
            content = @Content(schema = @Schema(implementation = Page.class)))
    @GetMapping("/by/name")
    @ResponseStatus(HttpStatus.OK)
    Page<CompanyResponse> getCompaniesByName(@RequestParam(value = "query", required = false) String query,
                                             @RequestParam(value = "size", required = false) Integer size,
                                             @RequestParam(value = "number", required = false) Integer number);

    @Operation(summary = "get all companies")
    @ApiResponse(responseCode = "200", description = "companies successfully received",
            content = @Content(schema = @Schema(implementation = Page.class)))
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    Page<CompanyResponse> getAll(@RequestParam(value = "size", required = false) Integer size,
                                 @RequestParam(value = "number", required = false) Integer number);
}
