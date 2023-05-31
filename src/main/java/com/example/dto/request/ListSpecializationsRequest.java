package com.example.dto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ListSpecializationsRequest {

    @NotNull(message = "vacancy id cannot be empty")
    private UUID vacancyId;

    private List<@NotBlank(message = "Specialization cannot be empty")String> specializations;
}
