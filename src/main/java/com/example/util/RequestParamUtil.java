package com.example.util;


import com.example.config.PageConfigurationProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RequestParamUtil {

    private final PageConfigurationProperties pageConfigurationProperties;

    public Integer handlePageSize(Integer size) {

        return size != null ? size : pageConfigurationProperties.getSize();
    }

    public Integer handlePageNumber(Integer number) {

        return number != null ? number : pageConfigurationProperties.getNumber();
    }

    public PageRequest getPageRequest(Integer size, Integer number){

        number = handlePageNumber(number);
        size = handlePageSize(size);
        return PageRequest.of(number, size);
    }
}
