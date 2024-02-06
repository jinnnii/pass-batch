package com.jinnnii.pass.job.pass;

import com.jinnnii.pass.config.TestBatchConfig;
import com.jinnnii.pass.domain.PackageEntity;
import com.jinnnii.pass.domain.PassEntity;
import com.jinnnii.pass.domain.PlaceEntity;
import com.jinnnii.pass.domain.UserEntity;
import com.jinnnii.pass.domain.constant.ActiveStatus;
import com.jinnnii.pass.domain.constant.PackageType;
import com.jinnnii.pass.domain.constant.PassStatus;
import com.jinnnii.pass.domain.constant.RoleType;
import com.jinnnii.pass.repository.PackageRepository;
import com.jinnnii.pass.repository.PassRepository;
import com.jinnnii.pass.repository.PlaceRepository;
import com.jinnnii.pass.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBatchTest
@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(classes = {ExpiredPassesJobConfig.class, TestBatchConfig.class})
class ExpiredPassesJobConfigTest {
    @Autowired private JobLauncherTestUtils jobLauncherTestUtils;
    @Autowired private PassRepository passRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private PackageRepository packageRepository;
    @Autowired private PlaceRepository placeRepository;

    @Test
    void test_expiredPassesStep() throws Exception{
        //given
        addPassEntities(10);

        //when
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();
        JobInstance jobInstance = jobExecution.getJobInstance();

        //then
        assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());
        assertEquals("expiredPassesJob", jobInstance.getJobName());
    }


    private void addPassEntities(int size){
        final LocalDateTime now = LocalDateTime.now();

        List<PassEntity> passEntityList = new ArrayList<>();

        PackageEntity packageEntity = getSavedTestPackage();
        for (int i =0; i<size; ++i){
            UserEntity userEntity = getSavedTestUser(i);
            PassEntity pass = PassEntity.of(
                    userEntity, packageEntity, packageEntity.getPlaceEntity(),
                    PassStatus.PROGRESSED, now.minusDays(60), now.minusMinutes(1),
                    LocalTime.of(4,0));
            passEntityList.add(pass);
        }
        passRepository.saveAll(passEntityList);

    }

    private UserEntity getSavedTestUser(int idx){
        UserEntity user = UserEntity.of("A0000"+idx, "A0000"+idx, RoleType.USER, ActiveStatus.ACTIVE);
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