package com.example.repository;

import com.example.model.constant.DeveloperState;
import com.example.model.constant.Status;
import com.example.model.entity.DeveloperEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DeveloperRepository extends JpaRepository<DeveloperEntity, UUID> {

    Optional<DeveloperEntity> findByIdAndStatus(UUID id, Status status);

    Page<DeveloperEntity> findAllByStateAndStatus(DeveloperState state, Status status, Pageable pageable);

    Optional<DeveloperEntity> findByUsername(String username);

    @Query("SELECT d from DeveloperEntity d JOIN d.vacancies v  WHERE v.id = :vacancyId")
    Page<DeveloperEntity> findAllDevelopersByVacancyId(@Param("vacancyId") UUID vacancyId, Pageable pageable);

    @Query("SELECT d FROM DeveloperEntity d WHERE EXISTS (SELECT  doc FROM DocumentEntity doc WHERE doc.developer.id = d.id)")
    Page<DeveloperEntity> findDevelopersThatAddDocuments(Pageable pageable);
}
