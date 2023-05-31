package com.example.repository;

import com.example.model.entity.DeveloperEntity;
import com.example.model.entity.SpecializationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SpecializationRepository extends JpaRepository<SpecializationEntity, UUID> {

    Optional<SpecializationEntity> findAllByNameAndDeveloper(String name, DeveloperEntity developer);
}
