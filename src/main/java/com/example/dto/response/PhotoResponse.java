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
public class PhotoResponse {

    private UUID id;

    private UUID accountId;

    private String fileName;

    private String link;

    private Long size;

    private String type;
}
