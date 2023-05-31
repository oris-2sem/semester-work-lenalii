package com.example.dto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class VacancyRequest {

    @NotBlank
    @Size(max = 50)
    private String name;

    private String description;

    private String city;

    @NotNull
    @PositiveOrZero
    private Long salary;

    private String status;

    private String address;

    private List<String> tags;

    private Boolean remote;
}
