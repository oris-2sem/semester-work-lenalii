package com.example.repository;

public interface BlacklistRepository {

    void save(String token);

    boolean exist(String token);
}
