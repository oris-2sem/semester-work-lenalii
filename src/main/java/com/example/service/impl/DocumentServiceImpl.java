package com.example.service.impl;

import com.example.config.MinioConfigurationProperties;
import com.example.converter.DocumentConverter;
import com.example.dto.request.DocumentRequest;
import com.example.dto.response.DocumentResponse;
import com.example.exception.DeveloperNotFoundException;
import com.example.exception.FileNotFoundException;
import com.example.exception.FileNotLoadedException;
import com.example.helper.MinioHelper;
import com.example.mapper.DocumentMapper;
import com.example.model.entity.DeveloperEntity;
import com.example.model.entity.DocumentEntity;
import com.example.repository.DeveloperRepository;
import com.example.repository.DocumentRepository;
import com.example.service.DocumentService;
import com.example.util.RequestParamUtil;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.errors.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.example.util.constants.Constants.LOCALHOST_8080;


@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentServiceImpl implements DocumentService {

    private final MinioClient minioClient;

    private final RequestParamUtil requestParamUtil;

    private final DocumentRepository documentRepository;

    private final DeveloperRepository developerRepository;

    private final DocumentMapper documentMapper;

    private final MinioConfigurationProperties properties;

    private final MinioHelper minioHelper;

    private String createDownloadUrl(UUID documentId) {

        return LOCALHOST_8080.concat("/document/download/").concat(documentId.toString());
    }

    @Transactional
    @Override
    public UUID delete(UUID id, UUID developerId) {

        DocumentEntity document = documentRepository.findByIdAndAndDeveloper_Id(id, developerId)
                .orElseThrow(() -> new FileNotFoundException(id));

        DeveloperEntity developer = document.getDeveloper();
        developer.getDocuments().remove(document);
        documentRepository.delete(document);

        minioHelper.removeFromMinio(id, document.getFileNameInBucket(), properties.getBucketNameDocuments());
        return document.getId();
    }

    @Transactional
    @Override
    public DocumentResponse uploadFile(MultipartFile file, DocumentRequest request, UUID developerId) {

        minioHelper.createBucketIfNotExist(properties.getBucketNameDocuments());

        DeveloperEntity developer = developerRepository.findById(developerId)
                .orElseThrow(DeveloperNotFoundException::new);

        DocumentEntity documentEntity = DocumentConverter.fromRequestToEntity(file, request);
        documentEntity.setDeveloper(developer);

        DocumentEntity savedEntity = documentRepository.save(documentEntity);
        developer.getDocuments().add(documentEntity);
        developerRepository.save(developer);

        minioHelper.uploadFileToMinIOServer(file, savedEntity.getFileNameInBucket(),
                savedEntity.getType(), savedEntity.getFileName(), properties.getBucketNameDocuments());

        DocumentResponse documentResponse = documentMapper.fromEntityToResponse(documentEntity);
        documentResponse.setDownloadUrl(createDownloadUrl(savedEntity.getId()));
        return documentResponse;
    }

    @Transactional(readOnly = true)
    @Override
    public List<DocumentResponse> getDocumentsByDeveloper(UUID developerId) {

        DeveloperEntity developer = developerRepository.findById(developerId).orElseThrow(DeveloperNotFoundException::new);

        List<DocumentEntity> page = documentRepository.findAllByDeveloper(developer);

        return documentMapper.fromEntityToResponse(page).stream().map(documentResponse-> {
            documentResponse.setDownloadUrl(createDownloadUrl(documentResponse.getId()));
            return documentResponse;
        }).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void downloadDocument(UUID documentId, HttpServletResponse response) {

        DocumentEntity documentEntity = documentRepository.findById(documentId)
                .orElseThrow(() -> new FileNotFoundException(documentId));

        InputStream inputStream = null;
        try {
            inputStream = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(properties.getBucketNameDocuments())
                    .object(documentEntity.getFileNameInBucket())
                    .length(documentEntity.getSize().longValue())
                    .build());

            response.setHeader("Content-Disposition", "attachment;filename="
                    + URLEncoder.encode(documentEntity.getFileName(), StandardCharsets.UTF_8));
            response.setCharacterEncoding("UTF-8");
            IOUtils.copy(inputStream, response.getOutputStream());
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException | InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException |
                XmlParserException e) {
            throw new FileNotLoadedException(documentEntity.getFileName());
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
