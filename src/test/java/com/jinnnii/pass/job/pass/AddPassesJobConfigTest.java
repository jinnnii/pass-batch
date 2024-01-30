package com.jinnnii.pass.job.pass;

import com.jinnnii.pass.config.TestBatchConfig;
import com.jinnnii.pass.domain.*;
import com.jinnnii.pass.domain.constant.ActiveStatus;
import com.jinnnii.pass.domain.constant.BulkPassStatus;
import com.jinnnii.pass.domain.constant.PackageType;
import com.jinnnii.pass.domain.constant.RoleType;
import com.jinnnii.pass.repository.*;
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

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBatchTest
@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(classes = {AddPassesJobConfig.class, TestBatchConfig.class, AddPassesTasklet.class})
class AddPassesJobConfigTest {
    @Autowired private JobLauncherTestUtils jobLauncherTestUtils;
    @Autowired private UserRepository userRepository;
    @Autowired private PlaceRepository placeRepository;
    @Autowired private PackageRepository packageRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private UserGroupRepository userGroupEntityRepository;
    @Autowired
    private BulkPassRepository bulkPassRepository;

    @Test
    void test_addPassesJob() throws Exception{
        //given
        addBulkPassEntity();
        //when
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();
        JobInstance jobInstance = jobExecution.getJobInstance();
        //then
        assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());
        assertEquals("addPassesJob", jobInstance.getJobName());


    }

    private void addBulkPassEntity(){
        // [*] group (<- user), [*] package, status, period, startedAt, endedAt
        final LocalDateTime now = LocalDateTime.now();
        final LocalDateTime tomorrow = now.plusDays(1).with(LocalTime.MIDNIGHT);

        GroupEntity groupEntity = getSavedTestGroup(5);
        PackageEntity packageEntity = getSavedTestPackage();

        BulkPassEntity bulkPass = BulkPassEntity.of(groupEntity, packageEntity, BulkPassStatus.READY, LocalTime.of(4,0), now, tomorrow);
        bulkPassRepository.save(bulkPass);
    }

    private GroupEntity getSavedTestGroup(int size){
        GroupEntity groupEntity = GroupEntity.of("오전반", "9:00-12:00 스터디 그룹");
        groupRepository.save(groupEntity);

        for (int idx=0 ; idx<size ; idx++) {
            UserEntity userEntity = getSavedTestUser(idx);
            userGroupEntityRepository.save(UserGroupEntity.of(userEntity, groupEntity));
        }

        return groupEntity;
    }

    private UserEntity getSavedTestUser(int idx){
        UserEntity user = UserEntity.of("A0000" + idx, "A0000" + idx, RoleType.USER, ActiveStatus.ACTIVE);
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