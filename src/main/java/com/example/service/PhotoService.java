package com.example.service;

import com.example.dto.request.PhotoRequest;
import com.example.dto.response.PhotoResponse;
import com.example.model.entity.PhotoEntity;

import java.util.UUID;

public interface PhotoService {

    PhotoResponse uploadPhoto(PhotoRequest request, UUID account);

    UUID delete(UUID account);

    PhotoResponse getPhotoResponse(PhotoEntity photo);
}
