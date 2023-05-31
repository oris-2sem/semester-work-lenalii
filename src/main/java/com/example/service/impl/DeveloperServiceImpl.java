package com.example.service.impl;

import com.example.client.DadataClient;
import com.example.dto.request.CreateDeveloperRequest;
import com.example.dto.request.UpdateDeveloperRequest;
import com.example.dto.response.DeveloperResponse;
import com.example.dto.response.PhotoResponse;
import com.example.exception.DeveloperAlreadyExistException;
import com.example.exception.DeveloperNotFoundException;
import com.example.exception.UserNotFoundException;
import com.example.mapper.DeveloperMapper;
import com.example.model.constant.DeveloperState;
import com.example.model.constant.Role;
import com.example.model.constant.Status;
import com.example.model.entity.AccountEntity;
import com.example.model.entity.DeveloperEntity;
import com.example.repository.DeveloperRepository;
import com.example.service.AccountService;
import com.example.service.DeveloperService;
import com.example.service.PhotoService;
import com.example.util.RequestParamUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class DeveloperServiceImpl implements DeveloperService {

    private final DeveloperRepository developerRepository;

    private final DeveloperMapper developerMapper;

    private final AccountService accountService;

    private final RequestParamUtil requestParamUtil;

    private final PhotoService photoService;

    private final DadataClient checkCityClient;

    @Override
    public DeveloperResponse convertToDeveloperResponseWithPhoto(DeveloperEntity developer){

        DeveloperResponse developerResponse = developerMapper.fromEntityToResponse(developer);
        if(developer.getPhoto()!=null){
            PhotoResponse photo = photoService.getPhotoResponse(developer.getPhoto());
            developerResponse.setPhoto(photo);
        }
        return developerResponse;
    }
    @Transactional
    @Override
    public DeveloperResponse save(CreateDeveloperRequest request) {

        checkCityClient.checkCityIsExist(request.getCity());

        accountService.checkAccountWithSuchEmailIsNotExist(request.getEmail());
        checkDeveloperWithThisUsernameIsNotExist(request.getUsername());

        DeveloperEntity developerEntity = developerMapper.fromRequestToEntity(request);
        developerEntity.setState(DeveloperState.ACTIVE);
        developerEntity.setRole(Role.ROLE_DEVELOPER);

        AccountEntity account = accountService.save(developerEntity);
        developerEntity.setId(account.getId());
        DeveloperEntity developer = developerRepository.save(developerEntity);

        return convertToDeveloperResponseWithPhoto(developer);
    }

    @Transactional
    @Override
    public DeveloperResponse update(UUID id, UpdateDeveloperRequest request) {

        DeveloperEntity developerEntity = developerRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);

        developerMapper.updateDeveloperEntity(request, developerEntity);
        DeveloperEntity developer = developerRepository.save(developerEntity);

        return convertToDeveloperResponseWithPhoto(developer);
    }

    private void checkDeveloperWithThisUsernameIsNotExist(String username) {

        Optional<DeveloperEntity> developerEntity = developerRepository.findByUsername(username);
        if (developerEntity.isPresent()) {
            throw new DeveloperAlreadyExistException();
        }
    }

    @Transactional
    @Override
    public UUID delete(UUID id) {

        DeveloperEntity developer = developerRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
        developer.setState(DeveloperState.NOT_ACTIVE);
        developer.setStatus(Status.DELETED);
        developerRepository.save(developer);


        return developer.getId();
    }

    @Transactional(readOnly = true)
    @Override
    public DeveloperResponse findById(UUID id) {

        DeveloperEntity developer = developerRepository
                .findById(id)
                .orElseThrow(DeveloperNotFoundException::new);

        return convertToDeveloperResponseWithPhoto(developer);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<DeveloperResponse> findAllByStateAndStatus(Integer size, Integer number, DeveloperState state, Status status) {

        PageRequest pageRequest = requestParamUtil.getPageRequest(size, number);
        Page<DeveloperEntity> entities = developerRepository.findAllByStateAndStatus(state, status, pageRequest);

        return entities.map(this::convertToDeveloperResponseWithPhoto);
    }

    @Transactional(readOnly = true)
    @Override
    public DeveloperResponse findByIdAndStatus(UUID id, Status status) {

        DeveloperEntity developer = developerRepository.findByIdAndStatus(id, status)
                .orElseThrow(DeveloperNotFoundException::new);

        return convertToDeveloperResponseWithPhoto(developer);
    }

    @Override
    public Page<DeveloperResponse> findDevelopersThatAddDocuments(Integer size, Integer number) {

        PageRequest pageRequest = requestParamUtil.getPageRequest(size, number);
        Page<DeveloperEntity> developers = developerRepository.findDevelopersThatAddDocuments(pageRequest);

        return developers.map(this::convertToDeveloperResponseWithPhoto);
    }
}
