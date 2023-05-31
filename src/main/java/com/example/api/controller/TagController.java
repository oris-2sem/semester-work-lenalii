package com.example.api.controller;

import com.example.api.TagApi;
import com.example.dto.request.ListIdsRequest;
import com.example.dto.request.ListTagsRequest;
import com.example.dto.response.ListResponse;
import com.example.dto.response.MessageResponse;
import com.example.dto.response.TagResponse;
import com.example.security.details.UserDetailsImpl;
import com.example.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class TagController implements TagApi {

    private final TagService tagService;

    @Override
    public ListResponse<TagResponse> addTagToVacancy(ListTagsRequest list, UserDetailsImpl userDetails) {

        List<TagResponse> data = tagService.addList(list, userDetails.getAccount().getId());

        return ListResponse.<TagResponse>builder()
                .size(data.size())
                .data(data)
                .build();

    }

    @Override
    public MessageResponse deleteTagsFromVacancy(ListIdsRequest list, UserDetailsImpl userDetails) {

        tagService.deleteList(list, userDetails.getAccount().getId());

        return MessageResponse.builder()
                .message("Tags successfully deleted from vacancy")
                .build();
    }
}
