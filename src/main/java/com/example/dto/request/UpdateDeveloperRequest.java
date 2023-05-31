package com.example.dto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UpdateDeveloperRequest {

    @Size(max = 20)
    private String firstName;

    @Size(max = 20)
    private String lastName;

    private String state;

    private LocalDate dateOfBirth;

    @PositiveOrZero(message = "не может быть отрицательным")
    private Integer workExperience;

    @Size(max = 20)
    private String city;
}
