package com.example.service;

import com.example.dto.request.ListIdsRequest;
import com.example.dto.request.ListTagsRequest;
import com.example.dto.response.TagResponse;

import java.util.List;
import java.util.UUID;

public interface TagService {

    List<TagResponse> addList(ListTagsRequest list, UUID hrId);

    void deleteList(ListIdsRequest list, UUID hrId);
}
