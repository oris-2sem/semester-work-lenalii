package com.example.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class DocumentResponse {

    private UUID id;

    private String fileName;

    private String downloadUrl;

    private Long size;

    private String type;

    private String description;

    private UUID developerId;
}
