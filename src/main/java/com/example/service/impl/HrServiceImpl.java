package com.example.service.impl;


import com.example.dto.request.CreateHrRequest;
import com.example.dto.request.UpdateHrRequest;
import com.example.dto.response.HrResponse;
import com.example.dto.response.PhotoResponse;
import com.example.exception.CompanyNotFoundException;
import com.example.exception.HrNotFoundException;
import com.example.exception.UserNotFoundException;
import com.example.mapper.HrMapper;
import com.example.model.constant.Role;
import com.example.model.constant.Status;
import com.example.model.entity.AccountEntity;
import com.example.model.entity.CompanyEntity;
import com.example.model.entity.HrEntity;
import com.example.repository.CompanyRepository;
import com.example.repository.HrRepository;
import com.example.service.AccountService;
import com.example.service.ConfirmationTokenService;
import com.example.service.HrService;
import com.example.service.PhotoService;
import com.example.util.RequestParamUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class HrServiceImpl implements HrService {

    private final HrRepository hrRepository;

    private final HrMapper hrMapper;

    private final PasswordEncoder passwordEncoder;

    private final AccountService accountService;

    private final RequestParamUtil requestParamUtil;

    private final ConfirmationTokenService confirmationTokenService;

    private final CompanyRepository companyRepository;

    private final PhotoService photoService;


    public HrResponse convertToHrResponseWithPhotoResponse(HrEntity hrEntity, HrResponse response){

        if(hrEntity.getPhoto()!=null){
            PhotoResponse photo = photoService.getPhotoResponse(hrEntity.getPhoto());
            response.setPhoto(photo);

        }
        return response;
    }

    @Transactional
    @Override
    public HrResponse save(CreateHrRequest request) {

        accountService.checkAccountWithSuchEmailIsNotExist(request.getEmail());

        CompanyEntity companyEntity = companyRepository.findByName(request.getCompanyName()).orElseThrow(CompanyNotFoundException::new);

        HrEntity hrEntity = hrMapper.fromRequestToEntity(request);
        hrEntity.setRole(Role.ROLE_HR);
        hrEntity.setStatus(Status.NOT_CONFIRMED);
        hrEntity.setHashPassword(passwordEncoder.encode(request.getPassword()));
        hrEntity.setCompany(companyEntity);

        AccountEntity account = accountService.save(hrEntity);
        hrEntity.setId(account.getId());
        HrEntity savedEntity = hrRepository.save(hrEntity);
        companyEntity.getHrs().add(hrEntity);

        HrResponse hrResponse = hrMapper.fromEntityToResponse(savedEntity);
        return convertToHrResponseWithPhotoResponse(hrEntity, hrResponse);
    }

    @Transactional
    @Override
    public HrResponse update(UUID id, UpdateHrRequest request) {

        HrEntity hrEntity = hrRepository.findById(id).orElseThrow(UserNotFoundException::new);
        hrMapper.updateHrEntity(request, hrEntity);
        HrEntity savedEntity = hrRepository.save(hrEntity);

        HrResponse hrResponse = hrMapper.fromEntityToResponse(savedEntity);

        return convertToHrResponseWithPhotoResponse(hrEntity, hrResponse);
    }

    @Transactional(readOnly = true)
    @Override
    public HrResponse getByIdAndStatus(UUID id, Status status) {

        HrEntity hrEntity = hrRepository.findByIdAndStatus(id, status)
                .orElseThrow(HrNotFoundException::new);

        HrResponse hrResponse = hrMapper.fromEntityToResponse(hrEntity);
        return convertToHrResponseWithPhotoResponse(hrEntity, hrResponse);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<HrResponse> getAllByStatus(Status status, Integer size, Integer number) {

        PageRequest pageRequest = requestParamUtil.getPageRequest(size, number);
        Page<HrEntity> entities = hrRepository.findAllByStatus(status, pageRequest);

        return entities.map(entity->{
            HrResponse hrResponse = hrMapper.fromEntityToResponse(entity);
            return convertToHrResponseWithPhotoResponse(entity, hrResponse);
        });
    }

    @Transactional(readOnly = true)
    @Override
    public Page<HrResponse> getAllByStatusAndCompany(String companyName, Status status, Integer size, Integer number) {

        PageRequest pageRequest = requestParamUtil.getPageRequest(size, number);
        Page<HrEntity> entities = hrRepository.findAllByStatusAndCompanyNameLike(status, "%"+companyName+"%", pageRequest);

        return entities.map(entity->{
            HrResponse hrResponse = hrMapper.fromEntityToResponse(entity);
            return convertToHrResponseWithPhotoResponse(entity, hrResponse);
        });
    }

    @Transactional(readOnly = true)
    @Override
    public HrResponse getById(UUID id) {
        HrEntity hrEntity = hrRepository.findById(id)
                .orElseThrow(HrNotFoundException::new);

        HrResponse hrResponse = hrMapper.fromEntityToResponse(hrEntity);
        return convertToHrResponseWithPhotoResponse(hrEntity, hrResponse);
    }
}
