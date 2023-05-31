package com.example.api;

import com.example.dto.request.ListIdsRequest;
import com.example.dto.request.ListTagsRequest;
import com.example.dto.response.ExceptionResponse;
import com.example.dto.response.ListResponse;
import com.example.dto.response.MessageResponse;
import com.example.dto.response.TagResponse;
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

@PreAuthorize("hasRole('ROLE_HR')")
@RequestMapping("/vacancy/tag")
public interface TagApi {

    @Operation(summary = "add tags as list to vacancy")
    @ApiResponse(responseCode = "201", description = "tags successfully added",
            content = @Content(schema = @Schema(implementation = ListResponse.class)))
    @ApiResponse(responseCode = "400", description = "company id and hr company id didn't match",
            content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    @ApiResponse(responseCode = "404", description = "vacancy not found",
            content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ListResponse<TagResponse> addTagToVacancy(@RequestBody @Valid ListTagsRequest list,
                                              @AuthenticationPrincipal UserDetailsImpl userDetails);

    @Operation(summary = "delete tags as list from vacancy")
    @ApiResponse(responseCode = "202", description = "tags successfully deleted",
            content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    @ApiResponse(responseCode = "400", description = "company id and hr company id didn't match",
            content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    @ApiResponse(responseCode = "400", description = "tag does not exist",
            content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    @ApiResponse(responseCode = "404", description = "vacancy not found",
            content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    @DeleteMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    MessageResponse deleteTagsFromVacancy(@RequestBody @Valid ListIdsRequest list,
                                          @AuthenticationPrincipal UserDetailsImpl userDetails);
}
