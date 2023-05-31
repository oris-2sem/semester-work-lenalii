package com.example.api;

import com.example.dto.request.*;
import com.example.dto.response.ExceptionResponse;
import com.example.dto.response.MessageResponse;
import com.example.dto.response.SpecializationResponse;
import com.example.security.details.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/developer/spec")
public interface SpecializationApi {

    @Operation(summary = "adding specializations as a list")
    @ApiResponse(responseCode = "201", description = "specializations successfully added" ,
            content = @Content(schema = @Schema(implementation = List.class)))
    @ApiResponse(responseCode = "400", description = "specialization already added",
            content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    @ApiResponse(responseCode = "404", description = "developer not found",
            content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    @PreAuthorize("hasRole('ROLE_DEVELOPER')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    List<SpecializationResponse> addList(@RequestBody @Valid SpecializationsListRequest request,
                                         @AuthenticationPrincipal UserDetailsImpl userDetails);

    @Operation(summary = "delete specialization")
    @ApiResponse(responseCode = "202", description = "specialization successfully deleted" ,
            content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    @ApiResponse(responseCode = "404", description = "specialization not found",
            content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    @PreAuthorize("hasRole('ROLE_DEVELOPER')")
    @DeleteMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    MessageResponse deleteSpecializations(@RequestBody @Valid ListSpecializationsIdsRequest list,
                                          @AuthenticationPrincipal UserDetailsImpl userDetails);
}
