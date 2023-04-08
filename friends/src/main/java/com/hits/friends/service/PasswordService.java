package com.hits.user.service;

public interface PasswordService {

    String toHash(String password);

    Boolean comparison(String password, String hash);
}
