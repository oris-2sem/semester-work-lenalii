package com.example.dto.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper=false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeveloperResponse extends AccountResponse{

    private String username;

    private LocalDate dateOfBirth;

    private String state;

    private Integer workExperience;

    private String city;

    private List<SpecializationResponse> specializations;
}
