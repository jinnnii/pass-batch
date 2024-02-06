package com.jinnnii.pass.job.notification;

import com.jinnnii.pass.adapter.KakaoTalkAdapter;
import com.jinnnii.pass.adapter.KakaoTalkMessageRequest;
import com.jinnnii.pass.domain.NotificationEntity;
import com.jinnnii.pass.domain.constant.NotificationEvent;
import com.jinnnii.pass.repository.NotificationRepository;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.integration.async.AsyncItemProcessor;
import org.springframework.batch.integration.async.AsyncItemWriter;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.Future;

@Configuration
public class SendAdNotificationJobConfig {
    private final int CHUNK_SIZE = 5;
    private final JobRepository jobRepository;
    private final EntityManagerFactory entityManagerFactory;
    private final PlatformTransactionManager transactionManager;
    private final SendAdNotifiactionTasklet sendAdNotifiactionTasklet;
    private final NotificationRepository notificationRepository;
    private final KakaoTalkAdapter kakaoTalkAdapter;

    public SendAdNotificationJobConfig(JobRepository jobRepository, EntityManagerFactory entityManagerFactory, PlatformTransactionManager transactionManager, SendAdNotifiactionTasklet sendAdNotifiactionTasklet, NotificationRepository notificationRepository, KakaoTalkAdapter kakaoTalkAdapter) {
        this.jobRepository = jobRepository;
        this.entityManagerFactory = entityManagerFactory;
        this.transactionManager = transactionManager;
        this.sendAdNotifiactionTasklet = sendAdNotifiactionTasklet;
        this.notificationRepository = notificationRepository;
        this.kakaoTalkAdapter = kakaoTalkAdapter;
    }

    /**
     * todo 관리자가 총괄하는 플레이스에 광고/정보성 알림 발송 작업
     */
    @Bean
    public Job sendAddNotificationJob(){
        return new JobBuilder("sendAdNotificationJob",jobRepository)
                .start(addAdNotificationStep())
                .next(sendAdNotificationStep())
                .build();
    }

    /**
     * todo : [STEP 1.] 플레이스에 한번이라도 이용권을 등록한 적 있는 회원들을 대상으로 광고/정보성 알림 생성
     * 1. 발급 전 상태이며, 예약 일자에 도달한 벌크 알림 조회
     * 2. 벌크 알림 대상 플레이스에 등록된 적 있으며 만료일자가 1년 이내거나 만료되지 않은 대상 이용권 리스트 조회
     * 3. 해당 이용권 리스트의 사용자 조회
     * 4. 해당 사용자의 UUID 와 벌크 알림 메시지로 발송할 알림 정보 생성
     */
    @Bean
    public Step addAdNotificationStep(){
        return new StepBuilder("addAdNotificationStep", jobRepository)
                .tasklet(sendAdNotifiactionTasklet,transactionManager)
                .build();
    }


    /**
     * todo : [STEP 2.] 사용자에게 알림 일괄 전송 (Async)
     * 1. 알림 이벤트가 CUSTOM 이며, 발송 전 상태의 알림 조회
     * 2. 카카오톡 알림 일괄 전송
     * 3. 알림 전송 여부 및 전송 일자 업데이트
     */
    @Bean
    public Step sendAdNotificationStep(){
        return new StepBuilder("sendAdNotificationStep", jobRepository)
                .<NotificationEntity, Future<NotificationEntity>>chunk(CHUNK_SIZE, transactionManager)
                .reader(sendAdNotificationItemReader())
                .processor(asyncSendAdNotificationItemProcessor())
                .writer(asyncSendAdNotificationItemWriter())
                .build();
    }



    @Bean
    public JpaCursorItemReader<NotificationEntity> sendAdNotificationItemReader(){
        return new JpaCursorItemReaderBuilder<NotificationEntity>()
                .name("sendAdNotificationItemReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("select n from NotificationEntity n where event = :event and sent = :sent")
                .parameterValues(Map.of("event", NotificationEvent.CUSTOM, "sent", false))
                .build();
    }

    @Bean
    public ItemProcessor<NotificationEntity, NotificationEntity> sendAdNotificationItemProcessor(){
        return notification -> {
            notification.setSent(true);
            notification.setSentAt(LocalDateTime.now());
            return notification;
        };
    }

    @Bean
    public AsyncItemProcessor<NotificationEntity, NotificationEntity> asyncSendAdNotificationItemProcessor(){
        AsyncItemProcessor<NotificationEntity, NotificationEntity> itemProcessor = new AsyncItemProcessor<>();
        itemProcessor.setDelegate(sendAdNotificationItemProcessor());
        itemProcessor.setTaskExecutor(new SimpleAsyncTaskExecutor());
        return itemProcessor;
    }

    @Bean
    public ItemWriter<NotificationEntity> sendAdNotificationItemWriter(){
        return chunk -> chunk.getItems().forEach(notification -> {
            kakaoTalkAdapter.sendKakaoTalkMessage(
                    KakaoTalkMessageRequest.from(notification.getUuid(), notification.getText())
            );
            notification.setSent(true);
            notification.setSentAt(LocalDateTime.now());

            notificationRepository.save(notification);
        });
    }

    @Bean
    public AsyncItemWriter<NotificationEntity> asyncSendAdNotificationItemWriter(){
        AsyncItemWriter<NotificationEntity> itemWriter = new AsyncItemWriter<>();
        itemWriter.setDelegate(sendAdNotificationItemWriter());
        return itemWriter;
    }
}

