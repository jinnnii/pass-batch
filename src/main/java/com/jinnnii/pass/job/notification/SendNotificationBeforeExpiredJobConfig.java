package com.jinnnii.pass.job.notification;

import com.jinnnii.pass.domain.EntranceEntity;
import com.jinnnii.pass.domain.NotificationEntity;
import com.jinnnii.pass.domain.constant.ActiveStatus;
import com.jinnnii.pass.domain.constant.NotificationEvent;
import com.jinnnii.pass.domain.mapper.NotificationModelMapper;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.item.support.SynchronizedItemStreamReader;
import org.springframework.batch.item.support.builder.SynchronizedItemStreamReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;

@Configuration
public class SendNotificationBeforeExpiredJobConfig {
    private final int CHUNK_SIZE = 5;
    public final PlatformTransactionManager transactionManager;
    private final JobRepository jobRepository;
    private final EntityManagerFactory entityManagerFactory;
    private final SendNotificationItemWriter sendNotificationItemWriter;

    public SendNotificationBeforeExpiredJobConfig(PlatformTransactionManager transactionManager, JobRepository jobRepository, EntityManagerFactory entityManagerFactory, SendNotificationItemWriter sendNotificationItemWriter) {
        this.transactionManager = transactionManager;
        this.jobRepository = jobRepository;
        this.entityManagerFactory = entityManagerFactory;
        this.sendNotificationItemWriter = sendNotificationItemWriter;
    }

    @Bean
    public Job addNotificationBeforeExpiredJob(){
        return new JobBuilder("addNotificationBeforeExpiredJob", jobRepository)
                .start(addNotificationBeforeExpiredStep())
                .next(sendNotificationBeforeExpiredStep())
                .build();
    }

    /**
     * [STEP 1.] 만료 되기 10분 전인 입장권 조회 및 알림 생성
     * @return
     */
    @Bean
    public Step  addNotificationBeforeExpiredStep(){
        return new StepBuilder("addNotificationBeforeExpiredStep", jobRepository)
                .<EntranceEntity, NotificationEntity>chunk(CHUNK_SIZE, transactionManager)
                .reader(addNotificationItemReader())
                .processor(addNotificationItemProcessor())
                .writer(addNotificationItemWriter())
                .build();
    }

    @Bean
    public JpaPagingItemReader<EntranceEntity> addNotificationItemReader(){
        // todo 현재 입장 상태이며 남은 시간이 10분 이내 거나 종료 일시가 10분 전인 입장권 조회
        return new JpaPagingItemReaderBuilder<EntranceEntity>()
                .name("addNotificationItemReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(CHUNK_SIZE)
                .queryString("select e from EntranceEntity e " +
                        "join fetch e.passEntity p " +
                        "where e.status = :status " +
                        "and (p.remainingTime <= :remainingTime" +
                        "  or p.endedAt <= :endedAt)")
                .parameterValues(Map.of(
                        "status", ActiveStatus.ACTIVE,
                        "remainingTime", LocalTime.of(0 ,10),
                        "endedAt", LocalDateTime.now().plusMinutes(10)
                ))
                .build();
    }

    @Bean
    public ItemProcessor<EntranceEntity, NotificationEntity> addNotificationItemProcessor(){
        return entrance -> NotificationModelMapper.INSTANCE.toNotificationEntity(entrance, NotificationEvent.BEFORE_EXPIRED);
    }

    @Bean
    public JpaItemWriter<NotificationEntity> addNotificationItemWriter(){
        return new JpaItemWriterBuilder<NotificationEntity>()
                .entityManagerFactory(entityManagerFactory)
                .build();
    }


    /**
     * [STEP 2.] '만료 전' 이벤트 및 미발송 상태의 알림 발송
     * @return
     */
    @Bean
    public Step  sendNotificationBeforeExpiredStep(){
        return new StepBuilder("sendNotificationBeforeExpiredStep", jobRepository)
                .<NotificationEntity, NotificationEntity>chunk(CHUNK_SIZE, transactionManager)
                .reader(sendNotificationItemReader())
                .writer(sendNotificationItemWriter)
                .taskExecutor(new SimpleAsyncTaskExecutor())
                .build();
    }

    @Bean
    public SynchronizedItemStreamReader<NotificationEntity> sendNotificationItemReader(){
        // todo 현재 이벤트가 만료 전이며 발송 여부가 미발송인 알람 조회
        JpaCursorItemReader<NotificationEntity> itemReader = new JpaCursorItemReaderBuilder<NotificationEntity>()
                .name("sendNotificationItemReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("select n from NotificationEntity n where n.event = :event and n.sent= :sent")
                .parameterValues(Map.of("event", NotificationEvent.BEFORE_EXPIRED, "sent", false))
                .build();

        return new SynchronizedItemStreamReaderBuilder<NotificationEntity>()
                .delegate(itemReader)
                .build();
    }
}
