package com.example.repository;


import com.example.model.entity.CompanyEntity;
import com.example.model.entity.DeveloperEntity;
import com.example.model.entity.VacancyEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VacancyRepository extends JpaRepository<VacancyEntity, UUID>, JpaSpecificationExecutor<VacancyEntity> {

    Page<VacancyEntity> findVacancyEntitiesByCompany(CompanyEntity company, Pageable pageable);

    Page<VacancyEntity> findVacancyEntitiesByHrId(UUID hrId, Pageable pageable);

    @Query("select v from VacancyEntity v where v.hr.id=:hrId and size(v.developers)>0")
    Page<VacancyEntity> findVacancyEntitiesByHrAndDevelopersIsNotEmpty(@Param("hrId") UUID hrId, Pageable pageable);

    List<VacancyEntity> findAll(Specification<VacancyEntity> specification, Sort sort);

    Page<VacancyEntity> findAllByDevelopersContaining(DeveloperEntity developer, Pageable pageable);

    Optional<VacancyEntity> findByIdAndHr_Id(UUID vacancyId, UUID hrId);

    @Query("SELECT v from VacancyEntity v JOIN v.developers d WHERE d.id=:developerId and v.id=:vacancyId")
    Optional<VacancyEntity> findByVacancyIdAndDeveloperId(@Param("vacancyId") UUID vacancyId, @Param("developerId") UUID developerId);
}
