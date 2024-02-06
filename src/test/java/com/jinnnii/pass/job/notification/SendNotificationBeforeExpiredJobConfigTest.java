package com.jinnnii.pass.job.notification;

import com.jinnnii.pass.adapter.KakaoTalkAdapter;
import com.jinnnii.pass.config.KakaoTalkConfig;
import com.jinnnii.pass.config.TestBatchConfig;
import com.jinnnii.pass.domain.*;
import com.jinnnii.pass.domain.constant.*;
import com.jinnnii.pass.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBatchTest
@SpringBootTest
@ContextConfiguration(classes = {TestBatchConfig.class, KakaoTalkConfig.class, KakaoTalkAdapter.class, SendNotificationItemWriter.class, SendNotificationBeforeExpiredJobConfig.class})
class SendNotificationBeforeExpiredJobConfigTest {

    @Autowired private JobLauncherTestUtils jobLauncherTestUtils;
    @Autowired private EntranceRepository entranceRepository;
    @Autowired private PassRepository passRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private PackageRepository packageRepository;
    @Autowired private PlaceRepository placeRepository;
    @Autowired private SeatRepository seatRepository;

    @Test
    void test_addNotification() throws Exception {
        //given
        addEntranceEntities(5);

        // when
        JobExecution jobExecution = jobLauncherTestUtils.launchStep("addNotificationBeforeExpiredStep");

        // then
        assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());
    }



    private void addEntranceEntities(int size){
        final LocalDateTime now = LocalDateTime.now();

        PackageEntity packageEntity = getSavedTestPackage();
        List<SeatEntity> seatEntityList = getSavedTestSeat(packageEntity.getPlaceEntity(), size);

        for (int i =0; i<size; ++i){
            UserEntity userEntity = getSavedTestUser(i);
            PassEntity pass = PassEntity.of(
                    userEntity, packageEntity, packageEntity.getPlaceEntity(),
                    PassStatus.PROGRESSED, now.minusDays(60), now.minusMinutes(10), LocalTime.of(4,0));
            passRepository.save(pass);

            EntranceEntity entrance = EntranceEntity.of(pass, seatEntityList.get(i), ActiveStatus.ACTIVE, LocalDateTime.now());
            entranceRepository.save(entrance);
        }

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

    private List<SeatEntity> getSavedTestSeat(PlaceEntity placeEntity, int size){
        List<SeatEntity> seatEntityList = new ArrayList<>();
        for (int i =0; i<size; ++i){
            SeatEntity seatEntity = SeatEntity.of(placeEntity, "A0"+i,  SeatType.LAPTOP, i,i);
            seatEntityList.add(seatEntity);
        }
        seatRepository.saveAll(seatEntityList);
        return seatEntityList;
    }

    private PackageEntity getSavedTestPackage(){
        PlaceEntity placeEntity = getSavedTestPlace();
        PackageEntity packageEntity = PackageEntity.of(placeEntity, "당일 4시간", 6000, 4, PackageType.DAY);
        packageRepository.save(packageEntity);
        return packageEntity;
    }
}