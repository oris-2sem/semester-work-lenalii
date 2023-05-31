package com.example.service;

import com.example.dto.request.RejectVacancyRequest;
import com.example.dto.response.RejectedReply;
import com.example.dto.response.VacancyReplyResponse;
import com.example.dto.response.VacancyResponse;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface VacancyReplyService {

    void replyToVacancy(UUID vacancyId, UUID developerId);

    Page<VacancyResponse> getAllRepliesByDeveloper(UUID developerId, Integer size, Integer number);

    RejectedReply rejectVacancyReply(RejectVacancyRequest id, UUID hrId);

    UUID deleteVacancyReply(UUID id, UUID developerId);

    Page<VacancyReplyResponse> getAllReplies(UUID id, Integer size, Integer number);
}
