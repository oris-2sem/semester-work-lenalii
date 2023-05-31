package com.example.service.impl;


import com.example.dto.request.RejectVacancyRequest;
import com.example.dto.response.DeveloperResponse;
import com.example.dto.response.RejectedReply;
import com.example.dto.response.VacancyReplyResponse;
import com.example.dto.response.VacancyResponse;
import com.example.exception.DeveloperNotFoundException;
import com.example.exception.VacancyNotFoundException;
import com.example.exception.VacancyReplyAlreadyExistException;
import com.example.mapper.DeveloperMapper;
import com.example.mapper.VacancyMapper;
import com.example.model.entity.DeveloperEntity;
import com.example.model.entity.VacancyEntity;
import com.example.repository.DeveloperRepository;
import com.example.repository.VacancyRepository;
import com.example.service.VacancyReplyService;
import com.example.util.RequestParamUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VacancyReplyServiceImpl implements VacancyReplyService {

    private final VacancyRepository vacancyRepository;

    private final RequestParamUtil requestParamUtil;

    private final DeveloperRepository developerRepository;

    private final VacancyMapper vacancyMapper;

    private final DeveloperMapper developerMapper;


    @Transactional
    @Override
    public void replyToVacancy(UUID vacancyId, UUID developerId) {

        Optional<VacancyEntity> vacancy = vacancyRepository.findByVacancyIdAndDeveloperId(vacancyId, developerId);
        if(vacancy.isPresent()){
            throw new VacancyReplyAlreadyExistException();
        }

        VacancyEntity vacancyEntity = vacancyRepository.
                findById(vacancyId)
                .orElseThrow(VacancyNotFoundException::new);

        DeveloperEntity developer = developerRepository.findById(developerId).orElseThrow(DeveloperNotFoundException::new);
        vacancyEntity.getDevelopers().add(developer);

        developer.getVacancies().add(vacancyEntity);
        vacancyRepository.save(vacancyEntity);
        developerRepository.save(developer);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<VacancyResponse> getAllRepliesByDeveloper(UUID developerId, Integer size, Integer number) {

        PageRequest pageRequest = requestParamUtil.getPageRequest(size, number);

        DeveloperEntity developer = developerRepository.findById(developerId).orElseThrow(DeveloperNotFoundException::new);
        Page<VacancyEntity> vacancies = vacancyRepository.findAllByDevelopersContaining(developer, pageRequest);

        return vacancies.map(vacancyMapper::fromEntityToResponse);
    }

    @Transactional
    @Override
    public RejectedReply rejectVacancyReply(RejectVacancyRequest request, UUID hrId) {

        DeveloperEntity developer = developerRepository.findById(request.getDeveloperId()).orElseThrow(DeveloperNotFoundException::new);
        VacancyEntity vacancyEntity = vacancyRepository.findByIdAndHr_Id(request.getVacancyId(), hrId).orElseThrow(VacancyNotFoundException::new);

        vacancyEntity.getDevelopers().remove(developer);
        vacancyRepository.save(vacancyEntity);

        developer.getVacancies().remove(vacancyEntity);
        developerRepository.save(developer);

        return RejectedReply.builder()
                .vacancyId(vacancyEntity.getId())
                .developerId(developer.getId())
                .build();
    }

    @Transactional
    @Override
    public UUID deleteVacancyReply(UUID vacancyId, UUID developerId) {
        VacancyEntity vacancyEntity = vacancyRepository.findByVacancyIdAndDeveloperId(vacancyId, developerId).orElseThrow(VacancyNotFoundException::new);
        DeveloperEntity developer = developerRepository.findById(developerId).orElseThrow(DeveloperNotFoundException::new);
        vacancyEntity.getDevelopers().remove(developer);
        vacancyRepository.save(vacancyEntity);

        developer.getVacancies().remove(vacancyEntity);
        developerRepository.save(developer);
        return vacancyId;
    }

    @Override
    public Page<VacancyReplyResponse> getAllReplies(UUID hrId, Integer size, Integer number) {

        Sort sort = Sort.by(Sort.Direction.DESC, "createdDate");
        PageRequest pageRequest = requestParamUtil.getPageRequest(size, number);
        Page<VacancyEntity> vacancies = vacancyRepository.findVacancyEntitiesByHrAndDevelopersIsNotEmpty(hrId, pageRequest.withSort(sort));


        return vacancies.map(vacancy -> {
            VacancyResponse vacancyResponse = vacancyMapper.fromEntityToResponse(vacancy);

            Set<DeveloperResponse> developers = vacancy.getDevelopers().stream().map(developerMapper::fromEntityToResponse).collect(Collectors.toSet());

            return VacancyReplyResponse.builder().vacancyResponse(vacancyResponse).developers(developers).build();
        });

    }
}
