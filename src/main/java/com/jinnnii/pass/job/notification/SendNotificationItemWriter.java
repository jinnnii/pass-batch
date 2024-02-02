package com.jinnnii.pass.job.notification;

import com.jinnnii.pass.adapter.KakaoTalkAdapter;
import com.jinnnii.pass.domain.NotificationEntity;
import com.jinnnii.pass.repository.NotificationRepository;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class SendNotificationItemWriter implements ItemWriter<NotificationEntity> {
    private final KakaoTalkAdapter kakaoTalkAdapter;
    private final NotificationRepository notificationRepository;

    public SendNotificationItemWriter(KakaoTalkAdapter kakaoTalkAdapter, NotificationRepository notificationRepository) {
        this.kakaoTalkAdapter = kakaoTalkAdapter;
        this.notificationRepository = notificationRepository;
    }

    @Override
    public void write(Chunk<? extends NotificationEntity> chunk) throws Exception {
        for (NotificationEntity notification : chunk.getItems()){
            boolean succeed = kakaoTalkAdapter.sendKakaoTalkMessage(notification.getUuid(), notification.getText());

            if(! succeed) return;

            notification.setSent(true);
            notification.setSentAt(LocalDateTime.now());
            notificationRepository.save(notification);
        }


    }
}
