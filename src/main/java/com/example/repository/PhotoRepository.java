package com.example.repository;

import com.example.model.entity.PhotoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PhotoRepository extends JpaRepository<PhotoEntity, UUID> {

    Optional<PhotoEntity> findByAccount_Id(UUID accountId);
}
