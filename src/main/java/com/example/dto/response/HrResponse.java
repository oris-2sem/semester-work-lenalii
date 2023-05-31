package com.example.dto.response;


import lombok.*;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper=false)
public class HrResponse extends AccountResponse{

    private CompanyResponse companyResponse;

    private String city;
}
