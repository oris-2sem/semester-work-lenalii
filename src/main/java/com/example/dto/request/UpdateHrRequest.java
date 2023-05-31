package com.example.dto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UpdateHrRequest {

    @Size(max = 20)
    private String firstName;

    @Size(max = 20)
    private String lastName;

    private String city;
}
