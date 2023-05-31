package com.example.repository;

import com.example.model.entity.DeveloperEntity;
import com.example.model.entity.DocumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DocumentRepository extends JpaRepository<DocumentEntity, UUID> {

    List<DocumentEntity> findAllByDeveloper(DeveloperEntity developer);

    Optional<DocumentEntity> findByIdAndAndDeveloper_Id(UUID id, UUID developerId);
}
