package com.n26.statistics.repository.implementation;

import com.n26.statistics.model.Transaction;
import com.n26.statistics.repository.TransactionRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.Assert.*;

/**
 * @author FortunatusE
 * @date 12/8/2018
 */

@RunWith(SpringRunner.class)
public class TransactionRepositoryImplTest {


    @Autowired
    private TransactionRepository transactionRepository;

    @TestConfiguration
    static class TransactionRepositoryConfig{

        @Bean
        TransactionRepository transactionRepository(){
            return new TransactionRepositoryImpl();
        }
    }

    @Test
    public void givenNewTransaction_thenSave() throws Exception {

        Transaction transaction1 = new Transaction(new BigDecimal("256.76"), Instant.parse("2018-07-17T09:59:01.312Z"));
        Transaction savedTransaction1 = transactionRepository.save(transaction1);
        assertThat(savedTransaction1.getId()).isNotNull().isEqualTo(0);

        Transaction transaction2 = new Transaction(new BigDecimal("276.76"), Instant.parse("2018-07-17T09:59:01.312Z"));
        Transaction savedTransaction2 = transactionRepository.save(transaction2);
        assertThat(savedTransaction2.getId()).isNotNull().isEqualTo(1);

    }

    @Test
    public void getTransactions() throws Exception {
    }

    @Test
    public void getTransactionStore() throws Exception {
    }

    @Test
    public void deleteAllTransactions() throws Exception {
    }

}