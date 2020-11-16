package com.melendez.kata.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.melendez.kata.web.rest.TestUtil;

public class BankStatementTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BankStatement.class);
        BankStatement bankStatement1 = new BankStatement();
        bankStatement1.setId(1L);
        BankStatement bankStatement2 = new BankStatement();
        bankStatement2.setId(bankStatement1.getId());
        assertThat(bankStatement1).isEqualTo(bankStatement2);
        bankStatement2.setId(2L);
        assertThat(bankStatement1).isNotEqualTo(bankStatement2);
        bankStatement1.setId(null);
        assertThat(bankStatement1).isNotEqualTo(bankStatement2);
    }
}
