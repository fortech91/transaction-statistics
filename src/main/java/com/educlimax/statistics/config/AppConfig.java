package com.educlimax.statistics.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

/**
 * @author FortunatusE
 * @date 12/8/2018
 */

@Configuration
public class AppConfig {


    @Bean
    public Clock clock(){
        return Clock.systemUTC();
    }
}
