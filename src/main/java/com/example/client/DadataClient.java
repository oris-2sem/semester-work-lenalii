package com.example.client;



import com.example.dto.response.CityResponse;
import com.example.exception.CityInputIsNotValidException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class DadataClient {

    private final RestTemplate restTemplate;

    public void checkCityIsExist(String city) {


        String url = "https://cleaner.dadata.ru/api/v1/clean/address";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", "Token 3976aca7ef5fcf51dc64fad419e4c70dfa0fef82");
        headers.add("X-Secret", "4582c98f0cd414330537de214cf5b418f4e01ef3");
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<String> entity = new HttpEntity<String>("[\"" + city + "\"]", headers);

        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        ResponseEntity<List<CityResponse>> cityResponses = restTemplate.exchange(url, HttpMethod.POST, entity, new ParameterizedTypeReference<List<CityResponse>>() {
        });
        if (Objects.requireNonNull(cityResponses.getBody()).get(0).getCity() == null) {
            throw new CityInputIsNotValidException();

        }
    }
}
