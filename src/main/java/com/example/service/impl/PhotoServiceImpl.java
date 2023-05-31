package com.example.service.impl;

import com.example.config.MinioConfigurationProperties;
import com.example.dto.request.PhotoRequest;
import com.example.dto.response.PhotoResponse;
import com.example.exception.FileNotFoundException;
import com.example.exception.UserNotFoundException;
import com.example.helper.MinioHelper;
import com.example.mapper.PhotoMapper;
import com.example.model.entity.AccountEntity;
import com.example.model.entity.PhotoEntity;
import com.example.repository.AccountRepository;
import com.example.repository.PhotoRepository;
import com.example.service.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class PhotoServiceImpl implements PhotoService {

    private final MinioHelper minioHelper;

    private final MinioConfigurationProperties properties;

    private final PhotoRepository photoRepository;

    private final PhotoMapper photoMapper;

    private final AccountRepository accountRepository;

    @Transactional
    @Override
    public PhotoResponse uploadPhoto(PhotoRequest request, UUID accountId) {

        AccountEntity account = accountRepository.findById(accountId).orElseThrow(UserNotFoundException::new);

        minioHelper.createBucketIfNotExist(properties.getBucketNamePhotos());

        PhotoEntity photoEntity;
        if (account.getPhoto() != null) {

            photoEntity = account.getPhoto();
            photoRepository.delete(photoEntity);
            minioHelper.removeFromMinio(photoEntity.getId(), photoEntity.getFileNameInBucket(), properties.getBucketNamePhotos());
        }

        photoEntity = new PhotoEntity();
        photoEntity.setFileName(request.getFile().getOriginalFilename());
        photoEntity.setAccount(account);
        photoEntity.setSize(BigDecimal.valueOf(request.getFile().getSize()));
        photoEntity.setType(request.getFile().getContentType());
        photoEntity.setFileNameInBucket(MinioHelper.createFileNameInBucket(request.getFile()));

        PhotoEntity savedPhoto = photoRepository.save(photoEntity);
        account.setPhoto(savedPhoto);
        accountRepository.save(account);

        minioHelper.uploadFileToMinIOServer(request.getFile(), savedPhoto.getFileNameInBucket(),
                savedPhoto.getType(), savedPhoto.getFileName(), properties.getBucketNamePhotos());

        PhotoResponse photoResponse = photoMapper.fromEntityToResponse(savedPhoto);
        photoResponse.setLink(minioHelper.getFileLink(properties.getBucketNamePhotos(), savedPhoto.getFileNameInBucket()));
        return photoResponse;
    }

    @Transactional
    @Override
    public UUID delete(UUID accountId) {

        PhotoEntity photo = photoRepository.findByAccount_Id(accountId)
                .orElseThrow(FileNotFoundException::new);

        AccountEntity account = photo.getAccount();
        account.setPhoto(null);
        photoRepository.delete(photo);

        minioHelper.removeFromMinio(photo.getId(), photo.getFileNameInBucket(), properties.getBucketNamePhotos());
        return photo.getId();
    }

    @Transactional(readOnly = true)
    @Override
    public PhotoResponse getPhotoResponse(PhotoEntity photo) {

        PhotoResponse photoResponse = photoMapper.fromEntityToResponse(photo);
        photoResponse.setLink(minioHelper.getFileLink(properties.getBucketNamePhotos(), photo.getFileNameInBucket()));
        return photoResponse;
    }
}
