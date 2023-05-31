package com.example.service.impl;

import com.example.dto.request.ListIdsRequest;
import com.example.dto.request.ListTagsRequest;
import com.example.dto.response.TagResponse;
import com.example.exception.TagNotExistException;
import com.example.exception.VacancyNotFoundException;
import com.example.mapper.TagMapper;
import com.example.model.entity.TagEntity;
import com.example.model.entity.VacancyEntity;
import com.example.repository.TagRepository;
import com.example.repository.VacancyRepository;
import com.example.service.CompanyService;
import com.example.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    private final CompanyService companyService;

    private final VacancyRepository vacancyRepository;

    private final TagMapper tagMapper;

    @Transactional
    @Override
    public List<TagResponse> addList(ListTagsRequest listTagsRequest, UUID hrId) {

        VacancyEntity vacancyEntity = vacancyRepository
                .findById(listTagsRequest.getVacancyId())
                .orElseThrow(VacancyNotFoundException::new);

        companyService.checkHrCompany(hrId, vacancyEntity.getCompany().getId());

        List<TagEntity> tagsEntities = tagMapper.fromStringToEntity(listTagsRequest.getTags())
                .stream()
                .map(tagEntity -> {
                    tagEntity.setTag(tagEntity.getTag().toLowerCase(Locale.ROOT));
                    tagEntity.setVacancy(vacancyEntity);
                    tagRepository.save(tagEntity);
                    return tagEntity;
                }).collect(Collectors.toList());

        vacancyEntity.getTags().addAll(tagsEntities);
        vacancyRepository.save(vacancyEntity);
        return tagMapper.fromEntityToResponse(tagsEntities);
    }

    @Transactional
    @Override
    public void deleteList(ListIdsRequest list, UUID hrId) {

        VacancyEntity vacancyEntity = vacancyRepository
                .findById(list.getVacancyId())
                .orElseThrow(VacancyNotFoundException::new);

        companyService.checkHrCompany(hrId, vacancyEntity.getCompany().getId());

        List<UUID> listVacancyTags = vacancyEntity.getTags()
                .stream()
                .map(TagEntity::getId)
                .toList();

        list.getIds().forEach(id -> {
            if(!listVacancyTags.contains(id)){
                throw new TagNotExistException(id);
            }
            TagEntity tag = tagRepository.getReferenceById(id);

            tagRepository.delete(tag);
            vacancyEntity.getTags().remove(tag);
        });
        vacancyRepository.save(vacancyEntity);
    }
}
