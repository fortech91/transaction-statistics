package com.n26.statistics.repository.implementation;

import com.educlimax.statistics.model.Transaction;
import com.educlimax.statistics.repository.TransactionRepository;
import com.educlimax.statistics.repository.implementation.TransactionRepositoryImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;


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
            return new TransactionRepositoryImpl(Clock.fixed(Instant.parse("2018-07-17T09:59:51.312Z"), ZoneOffset.UTC));
        }
    }

    @Before
    public void setUp(){
       transactionRepository.deleteAllTransactions();
    }

    @Test
    public void givenNewTransactions_thenSave() throws Exception {

        Transaction transaction1 = new Transaction(new BigDecimal("256.76"), Instant.parse("2018-07-17T09:59:01.312Z"));
        Transaction savedTransaction1 = transactionRepository.save(transaction1);
        assertThat(savedTransaction1.getId()).isNotNull().isEqualTo(0);

        Transaction transaction2 = new Transaction(new BigDecimal("276.76"), Instant.parse("2018-07-17T09:59:01.312Z"));
        Transaction savedTransaction2 = transactionRepository.save(transaction2);
        assertThat(savedTransaction2.getId()).isNotNull().isEqualTo(1);

    }

    @Test
    public void whenCalledForTransactions_thenReturnTransactionsWithinLast60Secs() throws Exception {


        Transaction transaction1 = new Transaction(new BigDecimal("756.76"), Instant.parse("2018-07-17T09:59:01.312Z"));
        Transaction transaction2 = new Transaction(new BigDecimal("976.76"), Instant.parse("2018-07-17T09:59:11.312Z"));
        Transaction transaction3 = new Transaction(new BigDecimal("876.76"), Instant.parse("2018-07-17T09:59:21.312Z"));

        transactionRepository.save(transaction1);
        transactionRepository.save(transaction2);
        transactionRepository.save(transaction3);

        List<Transaction> transactions = transactionRepository.getTransactions();
        assertThat(transactions).isNotEmpty().hasSize(3);

    }


    @Test
    public void whenCalledToDeleteTransactions_thenRemoveTransactionReferences() throws Exception {

        Transaction transaction1 = new Transaction(new BigDecimal("796.76"), Instant.parse("2018-07-17T09:59:21.312Z"));
        Transaction transaction2 = new Transaction(new BigDecimal("376.76"), Instant.parse("2018-07-17T09:59:28.312Z"));
        Transaction transaction3 = new Transaction(new BigDecimal("976.76"), Instant.parse("2018-07-17T09:59:31.312Z"));

        transactionRepository.save(transaction1);
        transactionRepository.save(transaction2);
        transactionRepository.save(transaction3);

        transactionRepository.deleteAllTransactions();
        List<Transaction> transactions = transactionRepository.getTransactions();
        assertThat(transactions).isEmpty();

    }

}
