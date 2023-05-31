package com.example.service.impl;


import com.example.dto.request.CompanyRequest;
import com.example.dto.request.UpdateCompanyRequest;
import com.example.dto.response.CompanyResponse;
import com.example.exception.CompaniesNotMatchException;
import com.example.exception.CompanyAlreadyExistException;
import com.example.exception.CompanyNotFoundException;
import com.example.exception.HrNotFoundException;
import com.example.mapper.CompanyMapper;
import com.example.model.constant.Status;
import com.example.model.entity.CompanyEntity;
import com.example.model.entity.HrEntity;
import com.example.repository.CompanyRepository;
import com.example.repository.HrRepository;
import com.example.service.CompanyService;
import com.example.util.RequestParamUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;

    private final RequestParamUtil requestParamUtil;

    private final CompanyMapper companyMapper;

    private final HrRepository hrRepository;

    @Transactional(readOnly = true)
    @Override
    public Page<CompanyResponse> getCompanyWhereStatus(Status status, Integer size, Integer number) {

        PageRequest pageRequest = requestParamUtil.getPageRequest(size, number);
        Page<CompanyEntity> companies = companyRepository.findAllByStatus(status, pageRequest);
        return companies.map(companyMapper::fromEntityToResponse);
    }

    @Transactional
    @Override
    public CompanyResponse updateStatus(UUID id, Status status) {

        CompanyEntity companyEntity = companyRepository.findById(id)
                .orElseThrow(CompanyNotFoundException::new);
        companyEntity.setStatus(status);

        return companyMapper.fromEntityToResponse(companyRepository.save(companyEntity));
    }

    @Transactional
    @Override
    public CompanyResponse save(CompanyRequest companyRequest) {

        checkCompanyWithThisNameIsNotExist(companyRequest);
        CompanyEntity companyEntity = companyMapper.fromRequestToEntity(companyRequest);
        companyEntity.setStatus(Status.MODERATED);
        CompanyEntity savedEntity = companyRepository.save(companyEntity);

        return companyMapper.fromEntityToResponse(savedEntity);
    }

    private void checkCompanyWithThisNameIsNotExist(CompanyRequest companyRequest) {

        Optional<CompanyEntity> company = companyRepository
                .findByNameAndStatus(companyRequest.getName(), Status.CONFIRMED);
        if (company.isEmpty()) {
            company = companyRepository
                    .findByNameAndStatus(companyRequest.getName(), Status.MODERATED);
        }
        if (company.isPresent()) {
            throw new CompanyAlreadyExistException();
        }
    }

    @Transactional
    @Override
    public CompanyResponse update(UpdateCompanyRequest updateCompanyRequest) {


        CompanyEntity companyEntity = companyRepository.findById(updateCompanyRequest.getId())
                .orElseThrow(CompanyNotFoundException::new);

        companyMapper.update(updateCompanyRequest, companyEntity);

        return companyMapper.fromEntityToResponse(companyRepository.save(companyEntity));

    }

    @Transactional
    @Override
    public UUID delete(UUID id) {

        CompanyEntity companyEntity = companyRepository.findById(id)
                .orElseThrow(CompanyNotFoundException::new);

        companyEntity.setStatus(Status.DELETED);
        companyRepository.save(companyEntity);

        return companyEntity.getId();
    }

    @Transactional(readOnly = true)
    @Override
    public CompanyResponse getById(UUID id) {

        CompanyEntity companyEntity = companyRepository.findById(id)
                .orElseThrow(CompanyNotFoundException::new);
        return companyMapper.fromEntityToResponse(companyEntity);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<CompanyResponse> getAll(Integer size, Integer number) {

        Sort sort = Sort.by(Sort.Direction.DESC, "createdDate");
        PageRequest pageRequest = requestParamUtil.getPageRequest(size, number);
        Page<CompanyEntity> companies = companyRepository.findAll(pageRequest.withSort(sort));
        return companies.map(companyMapper::fromEntityToResponse);
    }

    @Transactional(readOnly = true)
    @Override
    public void checkHrCompany(UUID hrId, UUID companyId) {

        HrEntity hrEntity = hrRepository.findById(hrId).orElseThrow(HrNotFoundException::new);

        if (!companyId.equals(hrEntity.getCompany().getId())) {
            throw new CompaniesNotMatchException(companyId, hrId);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Page<CompanyResponse> getConfirmedCompaniesByName(String query, Integer size, Integer number) {

        Sort sort = Sort.by(Sort.Direction.DESC, "createdDate");
        PageRequest pageRequest = requestParamUtil.getPageRequest(size, number);
        Page<CompanyEntity> companies = companyRepository.findByStatusAndNameLike(Status.CONFIRMED, query+'%',pageRequest.withSort(sort));
        return companies.map(companyMapper::fromEntityToResponse);
    }
}
