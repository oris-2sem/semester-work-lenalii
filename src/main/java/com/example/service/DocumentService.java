package com.example.service;

import com.example.dto.request.DocumentRequest;
import com.example.dto.response.DocumentResponse;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.UUID;

public interface DocumentService {

    DocumentResponse uploadFile(MultipartFile file, DocumentRequest request, UUID developerId);

    void downloadDocument(UUID documentId, HttpServletResponse response);

    UUID delete(UUID id, UUID developerId);

    List<DocumentResponse> getDocumentsByDeveloper(UUID developerId);
}
