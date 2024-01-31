package com.jinnnii.pass.config;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Optional;

@Configuration
@EnableJpaAuditing
@EnableAutoConfiguration
@EnableBatchProcessing
@EnableTransactionManagement
@EnableJpaRepositories("com.jinnnii.pass.repository")
@EntityScan("com.jinnnii.pass.domain")
public class TestBatchConfig {
    @Bean
    public AuditorAware<String> auditorAware(){
        return ()-> Optional.of("jinnnii");
    }
}
