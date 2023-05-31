package com.example.dto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UpdateVacancyRequest {

    @NotNull
    private UUID id;

    @Size(max = 50)
    private String name;

    private String description;

    private String city;

    private Boolean remote;

    private String address;

    @PositiveOrZero(message = "Отрицательное")
    private Long salary;

    private String status;
}
