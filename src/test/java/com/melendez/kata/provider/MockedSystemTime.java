package com.melendez.kata.provider;

import java.time.Instant;
import java.util.Date;

public class MockedSystemTime implements TimeProvider {
    public static final Instant DEFAULT_NOW_DATE = Instant.ofEpochMilli(0);
    @Override
    public Instant getDate() {
        return DEFAULT_NOW_DATE;
    }
}
