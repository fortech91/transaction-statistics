package com.n26.statistics.service.implementation;

import com.n26.statistics.dto.TransactionDTO;
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

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;



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
    static class ServiceProviderServiceConfig{

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

        HttpStatus httpStatus = statisticsService.addTransaction(transactionDto);
        assertThat(httpStatus).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    public void givenTransactionDetails_whenTimeStampIsFutureDate_thenReturn422Status() throws Exception {

        TransactionDTO transactionDto = new TransactionDTO();
        transactionDto.setAmount("456.76");
        transactionDto.setTimeStamp("2018-07-17T10:59:51.312Z");

        HttpStatus httpStatus = statisticsService.addTransaction(transactionDto);
        assertThat(httpStatus).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
    }


    @Test
    public void givenTransactionDetails_whenTimeWithin60Secs_thenReturn201Status() throws Exception {

        TransactionDTO transactionDto = new TransactionDTO();
        transactionDto.setAmount("456.76");
        transactionDto.setTimeStamp("2018-07-17T09:59:01.312Z");

        HttpStatus httpStatus = statisticsService.addTransaction(transactionDto);
        assertThat(httpStatus).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void getStatistics() throws Exception {
    }

    @Test
    public void deleteTransactions() throws Exception {
    }

}