package com.example.dto.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VacancyResponse {

    private UUID id;

    private String name;

    private String description;

    private String city;

    private Boolean remote;

    private String address;

    private Long salary;

    private OffsetDateTime updateDate;

    private OffsetDateTime createdDate;

    private String status;

    private CompanyResponse companyResponse;

    private List<TagResponse> tags;
}
