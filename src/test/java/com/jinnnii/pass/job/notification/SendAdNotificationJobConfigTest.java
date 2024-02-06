package com.jinnnii.pass.job.notification;

import com.jinnnii.pass.adapter.KakaoTalkAdapter;
import com.jinnnii.pass.config.KakaoTalkConfig;
import com.jinnnii.pass.config.TestBatchConfig;
import com.jinnnii.pass.domain.*;
import com.jinnnii.pass.domain.constant.*;
import com.jinnnii.pass.adapter.KakaoTalkMessageRequest;
import com.jinnnii.pass.repository.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.*;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBatchTest
@SpringBootTest
@ContextConfiguration(classes = {
        TestBatchConfig.class,
        KakaoTalkConfig.class,
        KakaoTalkAdapter.class,
        SendAdNotifiactionTasklet.class,
        SendAdNotificationJobConfig.class})
class SendAdNotificationJobConfigTest {
    @Autowired private JobLauncherTestUtils jobLauncherTestUtils;
    @Autowired private PassRepository passRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private PackageRepository packageRepository;
    @Autowired private PlaceRepository placeRepository;
    @Autowired private BulkNotificationRepository bulkNotificationEntityRepository;

    @Test
    @DisplayName("광고/정보성 알림 발송 - 사용자 조회 및 알림 생성")
    void test_addAdNotificationStep() {
        //given
        addBulkNotification(10);

        //when
        JobExecution jobExecution = jobLauncherTestUtils.launchStep("addAdNotificationStep");

        //then
        assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());
    }

    private void addBulkNotification(int passSize){
        PlaceEntity placeEntity = getSavedTestPassEntities(passSize).get(0).getPlaceEntity();

        KakaoTalkMessageRequest.TextObject textObject = KakaoTalkMessageRequest.TextObject.from("hello world", "url", "button title");
        BulkNotificationEntity bulkNotification = BulkNotificationEntity.of(placeEntity, BulkStatus.READY, textObject, LocalDateTime.now().minusMinutes(1));
        bulkNotificationEntityRepository.save(bulkNotification);
    }

    private  List<PassEntity> getSavedTestPassEntities(int size){
        final LocalDateTime now = LocalDateTime.now();

        List<PassEntity> passEntityList = new ArrayList<>();

        PackageEntity packageEntity = getSavedTestPackage();
        for (int i =0; i<size; ++i){
            UserEntity userEntity = getSavedTestUser(i);
            PassEntity pass = PassEntity.of(
                    userEntity, packageEntity, packageEntity.getPlaceEntity(),
                    PassStatus.EXPIRED, now.minusMonths(10), now.minusMonths(i* 3L),
                    LocalTime.of(0,0));
            pass.setExpiredAt(now.minusMonths(i* 3L));
            passEntityList.add(pass);
        }
        passRepository.saveAll(passEntityList);

        return passEntityList;
    }


    private UserEntity getSavedTestUser(int idx){
        UserEntity user = UserEntity.of("A0000"+idx, "A0000"+idx, RoleType.USER, ActiveStatus.ACTIVE);
        user.setMeta(Map.of("uuid", "0000000"+idx));
        userRepository.save(user);
        return user;
    }

    private UserEntity getTestManager(){
        return UserEntity.of("manager", "manager1234", RoleType.MANAGER, ActiveStatus.ACTIVE);
    }

    private PlaceEntity getSavedTestPlace(){
        UserEntity userEntity = getTestManager();
        userRepository.save(userEntity);
        PlaceEntity placeEntity = PlaceEntity.of("스터디 카페 강원 부곡점", userEntity, ActiveStatus.ACTIVE);
        placeRepository.save(placeEntity);

        return placeEntity;
    }

    private PackageEntity getSavedTestPackage(){
        PlaceEntity placeEntity = getSavedTestPlace();
        PackageEntity packageEntity = PackageEntity.of(placeEntity, "당일 4시간", 6000, 4, PackageType.DAY);
        packageRepository.save(packageEntity);
        return packageEntity;
    }
}