package com.melendez.kata.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.melendez.kata.web.rest.TestUtil;

public class BankStatementDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BankStatementDTO.class);
        BankStatementDTO bankStatementDTO1 = new BankStatementDTO();
        bankStatementDTO1.setId(1L);
        BankStatementDTO bankStatementDTO2 = new BankStatementDTO();
        assertThat(bankStatementDTO1).isNotEqualTo(bankStatementDTO2);
        bankStatementDTO2.setId(bankStatementDTO1.getId());
        assertThat(bankStatementDTO1).isEqualTo(bankStatementDTO2);
        bankStatementDTO2.setId(2L);
        assertThat(bankStatementDTO1).isNotEqualTo(bankStatementDTO2);
        bankStatementDTO1.setId(null);
        assertThat(bankStatementDTO1).isNotEqualTo(bankStatementDTO2);
    }
}
