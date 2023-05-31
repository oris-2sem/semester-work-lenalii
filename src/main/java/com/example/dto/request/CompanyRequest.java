package com.example.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CompanyRequest {

    @NotBlank
    private String name;

    private String description;

    private String websiteLink;

    @NotBlank
    private String email;

    @NotBlank
    private String type;
}
