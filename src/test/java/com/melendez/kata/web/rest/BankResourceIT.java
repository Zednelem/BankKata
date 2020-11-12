package com.melendez.kata.web.rest;

import com.melendez.kata.KataApp;
import com.melendez.kata.service.BankService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
/**
 * Test class for the BankResource REST controller.
 *
 * @see BankResource
 */
@SpringBootTest(classes = KataApp.class)
class BankResourceIT {

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
        MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);

    @Mock
    private BankService bankService;
    private MockMvc restMockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        BankResource bankResource = new BankResource(bankService);
        restMockMvc = MockMvcBuilders
            .standaloneSetup(bankResource)
            .build();
    }

    /**
     * Test depositMoney
     */
    @Test
    void testDepositMoney() throws Exception {
        when(bankService.depositMoney(1000.05)).thenReturn("OPERATION_MOCKED_ID");

        restMockMvc.perform(
            post("/api/bank/deposit-money")
                .contentType(APPLICATION_JSON_UTF8).content("1000.05"))
            .andExpect(status().isCreated()).andExpect(MockMvcResultMatchers.content().string("\"OPERATION_MOCKED_ID\""));
    }

    /**
     * Test withdrawMoney
     */
    @Test
    public void testWithdrawMoney() throws Exception {
        restMockMvc.perform(post("/api/bank/withdraw-money"))
            .andExpect(status().isOk());
    }

    /**
     * Test getOperationList
     */
    @Test
    public void testGetOperationList() throws Exception {
        restMockMvc.perform(get("/api/bank/get-operation-list"))
            .andExpect(status().isOk());
    }
}
