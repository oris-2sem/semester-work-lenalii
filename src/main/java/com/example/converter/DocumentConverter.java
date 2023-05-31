package com.example.converter;


import com.example.dto.request.DocumentRequest;
import com.example.helper.MinioHelper;
import com.example.model.entity.DocumentEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Component
public class DocumentConverter {

    public static DocumentEntity fromRequestToEntity(MultipartFile file, DocumentRequest request) {

        DocumentEntity documentEntity = new DocumentEntity();
        documentEntity.setFileName(file.getOriginalFilename());
        documentEntity.setDescription(request.getDescription());
        documentEntity.setSize(BigDecimal.valueOf(file.getSize()));
        documentEntity.setType(file.getContentType());
        documentEntity.setFileNameInBucket(MinioHelper.createFileNameInBucket(file));
        return documentEntity;
    }
}
