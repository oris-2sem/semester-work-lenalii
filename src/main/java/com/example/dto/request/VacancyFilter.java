package com.example.dto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class VacancyFilter {

    private String name;

    private String city;

    private String sortBy;

    private String sortType;

    private String minSalary;

    private String maxSalary;
}
