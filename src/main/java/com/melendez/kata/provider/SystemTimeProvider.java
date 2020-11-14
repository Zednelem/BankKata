package com.melendez.kata.provider;

import org.springframework.stereotype.Service;

import java.time.Instant;


@Service
public class SystemTimeProvider implements TimeProvider{

    @Override
    public Instant getDate() {
        return Instant.now();
    }
}
