package com.example.repository;


import com.example.model.constant.Status;
import com.example.model.entity.CompanyEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface CompanyRepository extends JpaRepository<CompanyEntity, UUID> {

    Page<CompanyEntity> findAllByStatus(Status status, Pageable pageable);

    Optional<CompanyEntity> findByName(String name);

    Optional<CompanyEntity> findByNameAndStatus(String name, Status status);

    Page<CompanyEntity> findByStatusAndNameLike(Status status, String name, Pageable pageable);
}
