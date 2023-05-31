package com.example.repository;

import com.example.model.entity.ConfirmationTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationTokenEntity, UUID> {

    Optional<ConfirmationTokenEntity> findByToken(UUID token);

    List<ConfirmationTokenEntity> findConfirmationTokenEntitiesByExpiredDateLessThan(OffsetDateTime offsetDateTime);
}
