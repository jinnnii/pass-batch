package com.jinnnii.pass.job.notification;

import com.jinnnii.pass.domain.BulkNotificationEntity;
import com.jinnnii.pass.domain.NotificationEntity;
import com.jinnnii.pass.domain.PlaceEntity;
import com.jinnnii.pass.domain.UserEntity;
import com.jinnnii.pass.domain.constant.BulkStatus;
import com.jinnnii.pass.domain.mapper.NotificationModelMapper;
import com.jinnnii.pass.repository.BulkNotificationRepository;
import com.jinnnii.pass.repository.NotificationRepository;
import com.jinnnii.pass.repository.PassRepository;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Component
public class SendAdNotifiactionTasklet implements Tasklet {
    private final BulkNotificationRepository bulkNotificationEntityRepository;
    private final PassRepository passRepository;
    private final NotificationRepository notificationRepository;

    public SendAdNotifiactionTasklet(BulkNotificationRepository bulkNotificationEntityRepository, PassRepository passRepository, NotificationRepository notificationRepository) {
        this.bulkNotificationEntityRepository = bulkNotificationEntityRepository;
        this.passRepository = passRepository;
        this.notificationRepository = notificationRepository;
    }

    /**
     * todo : 플레이스에 한번이라도 이용권을 등록한 적 있는 회원들을 대상으로 광고/정보성 알림 생성
     * 1. 발급 전 상태이며, 예약 일자에 도달한 벌크 알림 조회
     * 2. 벌크 알림 대상 플레이스에 등록된 적 있으며 만료일자가 1년 이내거나 만료되지 않은 대상 이용권 리스트 조회
     * 3. 해당 이용권 리스트의 사용자 조회
     * 4. 해당 사용자의 UUID 와 벌크 알림 메시지로 발송할 알림 정보 생성
     * 5. 벌크 알림 상태를 발급 완료 상태로 업데이트
     */
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        List<BulkNotificationEntity> bulkNotificationList = bulkNotificationEntityRepository
                .findByStatusAndBookedAtLessThan(BulkStatus.READY, LocalDateTime.now());

        for(BulkNotificationEntity bulkNotification : bulkNotificationList){
            PlaceEntity placeEntity = bulkNotification.getPlaceEntity();

            Set<UserEntity> userEntities = passRepository.findValidUserByPlaceId(placeEntity.getPlaceId(), LocalDateTime.now().minusYears(1));

            List<NotificationEntity> notificationList = userEntities.stream().map(
                    user -> NotificationModelMapper.INSTANCE.toNotificationEntity(bulkNotification, user)).toList();


            notificationRepository.saveAll(notificationList);

            bulkNotification.setStatus(BulkStatus.COMPLETED);
            bulkNotificationEntityRepository.save(bulkNotification);
        }

        return RepeatStatus.FINISHED;
    }
}
