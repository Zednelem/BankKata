package com.melendez.kata.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class BankStatementMapperTest {

    private BankStatementMapper bankStatementMapper;

    @BeforeEach
    public void setUp() {
        bankStatementMapper = new BankStatementMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(bankStatementMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(bankStatementMapper.fromId(null)).isNull();
    }
}
