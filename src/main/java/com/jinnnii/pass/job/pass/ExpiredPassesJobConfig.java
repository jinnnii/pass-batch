package com.jinnnii.pass.job.pass;

import com.jinnnii.pass.domain.PassEntity;
import com.jinnnii.pass.domain.constant.EnterStatus;
import com.jinnnii.pass.domain.constant.PassStatus;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDateTime;
import java.util.Map;

@Configuration
public class ExpiredPassesJobConfig {
    private final int CHUNK_SIZE = 5;

    private final EntityManagerFactory entityManagerFactory;

    public ExpiredPassesJobConfig(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Bean
    public Job expiredPassesJob(JobRepository jobRepository, Step step){
        return new JobBuilder("expiredPassesJob", jobRepository)
                .start(step)
                .build();
    }

    @Bean
    public Step expiredPassesStep(JobRepository jobRepository, PlatformTransactionManager transactionManager){
        return new StepBuilder("expiredPassesStep", jobRepository)
                .<PassEntity, PassEntity>chunk(CHUNK_SIZE, transactionManager)
                .reader(expiredPassesItemReader())
                .processor(expiredPassesItemProcessor())
                .writer(expiredPassesItemWriter())
                .build();
    }

    @Bean
    @StepScope
    public JpaCursorItemReader<PassEntity> expiredPassesItemReader() {
        // 만료 상태가 아니며 종료 일시가 현재 시점 보다 이전인 경우 만료 대상
        return new JpaCursorItemReaderBuilder<PassEntity>()
                .name("expiredPassesItemReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("select p from PassEntity p where p.status != :status and p.endedAt<= :endedAt")
                .parameterValues(Map.of("status", PassStatus.EXPIRED, "endedAt", LocalDateTime.now()))
                .build();
    }

    @Bean
    public ItemProcessor<PassEntity,PassEntity> expiredPassesItemProcessor() {
        return passEntity ->{
            passEntity.setStatus(PassStatus.EXPIRED);
            passEntity.setExpiredAt(LocalDateTime.now());
            return passEntity;
        };
    }
    @Bean
    public ItemWriter<PassEntity> expiredPassesItemWriter() {
        return new JpaItemWriterBuilder<PassEntity>()
                .entityManagerFactory(entityManagerFactory)
                .build();
    }



}
