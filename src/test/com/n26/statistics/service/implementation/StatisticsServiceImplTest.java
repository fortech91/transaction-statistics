package com.n26.statistics.service.implementation;

import com.n26.statistics.dto.Response;
import com.n26.statistics.dto.TransactionDTO;
import com.n26.statistics.dto.TransactionStatisticsDTO;
import com.n26.statistics.model.Transaction;
import com.n26.statistics.repository.TransactionRepository;
import com.n26.statistics.service.StatisticsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * @author FortunatusE
 * @date 12/8/2018
 */

@RunWith(SpringRunner.class)
public class StatisticsServiceImplTest {

    private static TransactionRepository transactionRepository;

    @Autowired
    private StatisticsService statisticsService;


    @TestConfiguration
    static class StatisticsServiceConfig{

        @Bean
        StatisticsService statisticsService(){
            transactionRepository = mock(TransactionRepository.class);
            return new StatisticsServiceImpl(transactionRepository, Clock.fixed(Instant.parse("2018-07-17T09:59:51.312Z"), ZoneOffset.UTC));
        }
    }


    @Test
    public void givenTransactionDetails_whenOlderThan60Secs_thenReturn204Status() throws Exception {

        TransactionDTO transactionDto = new TransactionDTO();
        transactionDto.setAmount("456.76");
        transactionDto.setTimeStamp("2018-07-17T08:59:51.312Z");

        Response response = statisticsService.addTransaction(transactionDto);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    public void givenTransactionDetails_whenTimeIsInFuture_thenReturn422Status() throws Exception {

        TransactionDTO transactionDto = new TransactionDTO();
        transactionDto.setAmount("456.76");
        transactionDto.setTimeStamp("2018-07-17T10:59:51.312Z");

        Response response = statisticsService.addTransaction(transactionDto);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @Test
    public void givenTransactionDetails_whenUnparseable_thenReturn422Status() throws Exception {

        TransactionDTO transactionDto = new TransactionDTO();
        transactionDto.setAmount("456.76h");
        transactionDto.setTimeStamp("2018-07-17T10:59:51.312Z");

        Response response = statisticsService.addTransaction(transactionDto);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
    }


    @Test
    public void givenTransactionDetails_whenTimeWithin60Secs_thenReturn201Status() throws Exception {

        TransactionDTO transactionDto = new TransactionDTO();
        transactionDto.setAmount("456.76");
        transactionDto.setTimeStamp("2018-07-17T09:59:01.312Z");

        Response response = statisticsService.addTransaction(transactionDto);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void givenTransactions_whenWithinLast60Secs_thenReturnStatistics() throws Exception {

        Transaction transaction1 = new Transaction(new BigDecimal("256.76"), Instant.parse("2018-07-17T09:59:01.312Z"));
        Transaction transaction2 = new Transaction(new BigDecimal("346.57"), Instant.parse("2018-07-17T09:59:11.312Z"));
        Transaction transaction3 = new Transaction(new BigDecimal("456.76"), Instant.parse("2018-07-17T09:59:21.312Z"));
        Transaction transaction4 = new Transaction(new BigDecimal("576.53"), Instant.parse("2018-07-17T09:59:27.312Z"));
        Transaction transaction5 = new Transaction(new BigDecimal("156.76"), Instant.parse("2018-07-17T09:59:31.312Z"));


        List<Transaction> transactions = Arrays.asList(transaction1, transaction2, transaction3, transaction4, transaction5);

        when(transactionRepository.getTransactions()).thenReturn(transactions);

        TransactionStatisticsDTO statistics = (TransactionStatisticsDTO)statisticsService.getStatistics().getBody();

        assertThat(statistics.getSum()).isEqualTo("1793.38");
        assertThat(statistics.getAvg()).isEqualTo("358.68");
        assertThat(statistics.getMax()).isEqualTo("576.53");
        assertThat(statistics.getMin()).isEqualTo("156.76");
        assertThat(statistics.getCount()).isEqualTo(5);

    }

    @Test
    public void deleteTransactions() throws Exception {

        Transaction transaction1 = new Transaction(new BigDecimal("256.76"), Instant.parse("2018-07-17T09:59:01.312Z"));
        Transaction transaction2 = new Transaction(new BigDecimal("366.57"), Instant.parse("2018-07-17T09:59:11.312Z"));
        Transaction transaction3 = new Transaction(new BigDecimal("456.76"), Instant.parse("2018-07-17T09:59:21.312Z"));
        Transaction transaction4 = new Transaction(new BigDecimal("536.53"), Instant.parse("2018-07-17T09:59:27.312Z"));
        Transaction transaction5 = new Transaction(new BigDecimal("156.76"), Instant.parse("2018-07-17T09:59:31.312Z"));

        List<Transaction> transactions = Arrays.asList(transaction1, transaction2, transaction3, transaction4, transaction5);

        when(transactionRepository.getTransactions()).thenReturn(transactions);

        Response response = statisticsService.deleteTransactions();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT);
    }

}