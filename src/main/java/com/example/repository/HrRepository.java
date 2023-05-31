package com.example.repository;


import com.example.model.constant.Status;
import com.example.model.entity.HrEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface HrRepository extends JpaRepository<HrEntity, UUID> {

    Page<HrEntity> findAllByStatus(Status status, Pageable pageable);

    Optional<HrEntity> findByIdAndStatus(UUID id, Status status);

    @Query("select h from HrEntity h where h.company.name like :companyName and h.status=:status")
    Page<HrEntity> findAllByStatusAndCompanyNameLike(@Param("status") Status status, @Param("companyName")String companyName, Pageable pageable);
}
