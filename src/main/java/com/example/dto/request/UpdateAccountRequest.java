package com.example.dto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UpdateAccountRequest {

    @Size(max = 20)
    private String firstName;

    @Size(max = 20)
    private String lastName;
}
