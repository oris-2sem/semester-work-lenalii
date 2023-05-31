package com.example.repository;

import com.example.model.constant.Status;
import com.example.model.entity.AccountEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, UUID> {

    Optional<AccountEntity> findByEmail(String email);

    List<AccountEntity> findAllByStatus(Status status);

    Page<AccountEntity> findAllByStatus(Status status, Pageable pageable);

    Page<AccountEntity> findByFirstNameLikeOrLastNameLike(String firstName, String lastName, Pageable pageable);

    Page<AccountEntity> findByLastNameLike(String lastName, Pageable pageable);

    Page<AccountEntity> findByFirstNameLike(String firstName, Pageable pageable);
}
