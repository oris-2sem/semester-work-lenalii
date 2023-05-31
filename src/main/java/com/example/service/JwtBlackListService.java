package com.example.service;

public interface JwtBlackListService {

    void add(String token);

    boolean exist(String jwt);
}
