package com.example.service.impl;

import com.example.dto.request.UpdateVacancyRequest;
import com.example.dto.request.VacancyFilter;
import com.example.dto.request.VacancyRequest;
import com.example.dto.response.VacancyResponse;
import com.example.exception.BadRequestException;
import com.example.exception.CompanyNotFoundException;
import com.example.exception.MoreElementsInListException;
import com.example.exception.VacancyNotFoundException;
import com.example.mapper.VacancyMapper;
import com.example.model.entity.CompanyEntity;
import com.example.model.entity.HrEntity;
import com.example.model.entity.VacancyEntity;
import com.example.repository.*;
import com.example.service.VacancyService;
import com.example.util.RequestParamUtil;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VacancyServiceImpl implements VacancyService {

    private final VacancyRepository vacancyRepository;

    private final TagRepository tagRepository;

    private final RequestParamUtil requestParamUtil;

    private final VacancyMapper vacancyMapper;

    private final CompanyRepository companyRepository;

    private final HrRepository hrRepository;

    @Transactional
    @Override
    public VacancyResponse save(VacancyRequest vacancyRequest, UUID hrId) {

        if (vacancyRequest.getTags() != null && vacancyRequest.getTags().size() > 50) {
            throw new MoreElementsInListException();
        }

        HrEntity hrEntity = hrRepository.getReferenceById(hrId);
        VacancyEntity vacancyEntity = vacancyMapper.fromRequestToEntity(vacancyRequest);
        vacancyEntity.setHr(hrEntity);
        vacancyEntity.setCompany(hrEntity.getCompany());

        setDefaultVacancyStatus(vacancyEntity, vacancyRequest);

        VacancyEntity savedEntity = vacancyRepository.save(vacancyEntity);
        saveTagsForVacancy(savedEntity);

        return vacancyMapper.fromEntityToResponse(savedEntity);
    }

    private void setDefaultVacancyStatus(VacancyEntity vacancyEntity, VacancyRequest vacancyRequest) {

        if (vacancyRequest.getStatus() != null) {
            try {
                vacancyEntity.setStatus(VacancyEntity.Status.valueOf(vacancyRequest.getStatus()));
            } catch (IllegalArgumentException e) {
                throw new BadRequestException("Entered invalid data");
            }
        } else {
            vacancyEntity.setStatus(VacancyEntity.Status.ACTIVE);
        }
    }

    private void saveTagsForVacancy(VacancyEntity savedEntity) {

        if (savedEntity.getTags() != null) {
            savedEntity.getTags().forEach(tagEntity -> {
                tagEntity.setVacancy(savedEntity);
                tagRepository.save(tagEntity);
            });
        }
    }

    @Transactional
    @Override
    public VacancyResponse update(UpdateVacancyRequest request, UUID hrId) {

        VacancyEntity vacancyEntity = vacancyRepository
                .findByIdAndHr_Id(request.getId(), hrId)
                .orElseThrow(VacancyNotFoundException::new);

        vacancyMapper.update(request, vacancyEntity);
        return vacancyMapper.fromEntityToResponse(vacancyRepository.save(vacancyEntity));
    }

    @Transactional
    @Override
    public UUID delete(UUID id, UUID hrId) {

        VacancyEntity vacancyEntity = vacancyRepository.findByIdAndHr_Id(id, hrId).orElseThrow(VacancyNotFoundException::new);
        vacancyRepository.delete(vacancyEntity);

        return vacancyEntity.getId();
    }

    @Transactional(readOnly = true)
    @Override
    public VacancyResponse getById(UUID id) {

        VacancyEntity vacancyEntity = vacancyRepository.findById(id).orElseThrow(VacancyNotFoundException::new);
        return vacancyMapper.fromEntityToResponse(vacancyEntity);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<VacancyResponse> getAll(Integer size, Integer number) {

        PageRequest pageRequest = requestParamUtil.getPageRequest(size, number);
        Page<VacancyEntity> vacancies = vacancyRepository.findAll(pageRequest);

        return vacancies.map(vacancyMapper::fromEntityToResponse);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<VacancyResponse> getByCompany(UUID companyId, Integer size, Integer number) {

        CompanyEntity companyEntity = companyRepository.findById(companyId).orElseThrow(CompanyNotFoundException::new);
        PageRequest pageRequest = requestParamUtil.getPageRequest(size, number);
        Page<VacancyEntity> vacancies = vacancyRepository.findVacancyEntitiesByCompany(companyEntity, pageRequest);

        return vacancies.map(vacancyMapper::fromEntityToResponse);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<VacancyResponse> getByHr(UUID hrId, Integer size, Integer number) {

        Sort sort = Sort.by(Sort.Direction.DESC, "createdDate");
        PageRequest pageRequest = requestParamUtil.getPageRequest(size, number);
        Page<VacancyEntity> vacancies = vacancyRepository.findVacancyEntitiesByHrId(hrId, pageRequest.withSort(sort));

        return vacancies.map(vacancyMapper::fromEntityToResponse);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<VacancyResponse> searchByFilter(VacancyFilter filter, Integer size, Integer number) {

        Specification<VacancyEntity> specification = Specification.where(byCity(filter.getCity()))
                .and(byName(filter.getName()))
                .and(bySalary(filter.getMinSalary(), filter.getMaxSalary()));

        Sort sort = createSort(filter);
        PageRequest pageRequest = requestParamUtil.getPageRequest(size, number);

        Page<VacancyEntity> page = vacancyRepository.findAll(specification, pageRequest.withSort(sort));


        return page.map(vacancyMapper::fromEntityToResponse);
    }

    private Sort createSort(VacancyFilter filter) {

        Sort.Direction sortType;
        if (filter.getSortType() == null) {
            sortType = Sort.Direction.DESC;

        } else {
            sortType = Sort.Direction.fromString(filter.getSortType());
        }
        String sortBy = filter.getSortBy();
        if (filter.getSortBy() == null) {
            sortBy = "createdDate";
        }
        return Sort.by(sortType, sortBy);
    }

    public Specification<VacancyEntity> byCity(String city) {

        return (root, query, criterianBuilder) -> {
            if (!city.isBlank()) {
                return criterianBuilder.equal(root.get("city"), city);
            }
            return null;
        };
    }

    public Specification<VacancyEntity> byName(String name) {

        return (root, query, criteriaBuilder) -> {
            if (!name.isBlank()) {
                return criteriaBuilder.like(root.get("name"), '%' + name + '%');
            }
            return null;
        };
    }

    public Specification<VacancyEntity> bySalary(String minSalary, String maxSalary) {

        return (root, query, criteriaBuilder) -> {
            Specification<VacancyEntity> specification = Specification.where(null);

            if (!minSalary.isBlank()) {
                specification = specification.and((Specification<VacancyEntity>)
                        (root1, query1, criteriaBuilder1) ->
                                criteriaBuilder1.greaterThanOrEqualTo(root1.get("salary"), minSalary));

            }
            if (!maxSalary.isBlank()) {
                specification = specification.and((Specification<VacancyEntity>)
                        (root12, query12, criteriaBuilder12) ->
                                criteriaBuilder12.lessThanOrEqualTo(root12.get("salary"), maxSalary));
            }
            return specification.toPredicate(root, query, criteriaBuilder);
        };
    }
}
