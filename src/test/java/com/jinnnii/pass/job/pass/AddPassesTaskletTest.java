package com.jinnnii.pass.job.pass;

import com.jinnnii.pass.domain.*;
import com.jinnnii.pass.domain.constant.*;
import com.jinnnii.pass.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class AddPassesTaskletTest {
    @Mock private StepContribution stepContribution;
    @Mock private ChunkContext chunkContext;
    @Mock private BulkPassRepository bulkPassRepository;
    @Mock private PassRepository passRepository;
    @InjectMocks private AddPassesTasklet addPassesTasklet;

    @Test
    void test_execute() throws Exception {
        //given
        final LocalDateTime now = LocalDateTime.now();
        final LocalDateTime tomorrow = now.plusDays(1).with(LocalTime.MIDNIGHT);
        final int size =5;
        final LocalTime period = LocalTime.of(4,0);
        final Long packageId = 9999L;

        GroupEntity groupEntity = getTestGroup(size);
        PackageEntity packageEntity = getTestPackage(packageId);

        BulkPassEntity bulkPass = BulkPassEntity.of(groupEntity, packageEntity, BulkPassStatus.READY, period, now, tomorrow);

        //when
        when(bulkPassRepository.findByStatusAndStartedAtGreaterThan(eq(BulkPassStatus.READY), any())).thenReturn(List.of(bulkPass));
        RepeatStatus repeatStatus = addPassesTasklet.execute(stepContribution, chunkContext);

        //then
        assertEquals(RepeatStatus.FINISHED, repeatStatus);

        ArgumentCaptor<List> passEntitiesCaptor = ArgumentCaptor.forClass(List.class);
        verify(passRepository, times(1)).saveAll(passEntitiesCaptor.capture());

        final List<PassEntity> passEntityList = passEntitiesCaptor.getValue();

        assertEquals(size, passEntityList.size());

        final PassEntity passEntity = passEntityList.get(0);
        assertEquals("A00000", passEntity.getUserEntity().getUserId());
        assertEquals(packageId, passEntity.getPackageEntity().getPackageId());
        assertEquals(PassStatus.READY, passEntity.getStatus());
        assertEquals(period, passEntity.getRemainingTime());
    }


    private GroupEntity getTestGroup(int size) throws NoSuchFieldException, IllegalAccessException {
        GroupEntity groupEntity = GroupEntity.of("오전반", "9:00-12:00 스터디 그룹");
        Field field = GroupEntity.class.getDeclaredField("userGroups");
        field.setAccessible(true);

        Set<UserGroupEntity> userGroupEntitySet = new LinkedHashSet<>();
        for (int idx=0 ; idx<size ; idx++) {
            UserGroupEntity userGroupEntity = UserGroupEntity.of(getTestUser(idx), groupEntity);
            userGroupEntitySet.add(userGroupEntity);
        }
        field.set(groupEntity, userGroupEntitySet);

        return groupEntity;
    }

    private UserEntity getTestUser(int idx){
        return UserEntity.of("A0000" + idx, "A0000" + idx, RoleType.USER, ActiveStatus.ACTIVE);
    }

    private UserEntity getTestManager(){
        return UserEntity.of("manager", "manager1234", RoleType.MANAGER, ActiveStatus.ACTIVE);
    }

    private PlaceEntity getSavedTestPlace(){
        UserEntity userEntity = getTestManager();
        return  PlaceEntity.of("스터디 카페 강원 부곡점", userEntity, ActiveStatus.ACTIVE);
    }

    private PackageEntity getTestPackage(Long packageId) throws NoSuchFieldException, IllegalAccessException {
        PlaceEntity placeEntity = getSavedTestPlace();
        PackageEntity packageEntity = PackageEntity.of(placeEntity, "당일 4시간", 6000, 4, PackageType.DAY);
        Field field = PackageEntity.class.getDeclaredField("packageId");
        field.setAccessible(true);
        field.set(packageEntity, packageId);

        return packageEntity;
    }
}