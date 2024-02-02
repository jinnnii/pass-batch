package com.jinnnii.pass.domain.mapper;

import com.jinnnii.pass.domain.*;
import com.jinnnii.pass.domain.constant.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class NotificationModelMapperTest {
    @Test
    void test_mappingNotificationFromEntrance() {
        EntranceEntity entranceEntity = addEntranceEntities(1).get(0);

        NotificationEntity notification = NotificationModelMapper.INSTANCE.toNotificationEntity(entranceEntity, NotificationEvent.AFTER_ENTERED);

        assertEquals("000000001", notification.getUuid());
        assertEquals(false, notification.getSent());

        log.info("NotificationModelMapperTest [ text ]: {}", notification.getText());
    }

    private List<EntranceEntity> addEntranceEntities(int size){
        final LocalDateTime now = LocalDateTime.now();

        PackageEntity packageEntity = getTestPackage();
        List<SeatEntity> seatEntityList = getTestSeatList(packageEntity.getPlaceEntity(), size);

        List<EntranceEntity> entranceEntityList = new ArrayList<>();

        for (int i =0; i<size; ++i){
            UserEntity userEntity = getTestUser(i);
            PassEntity pass = PassEntity.of(
                    userEntity, packageEntity,
                    PassStatus.PROGRESSED, now.minusDays(60), now.minusMinutes(10), LocalTime.of(4, 0));

            EntranceEntity entrance = EntranceEntity.of(pass, seatEntityList.get(i), ActiveStatus.ACTIVE, LocalDateTime.now());
            entranceEntityList.add(entrance);
        }
        return entranceEntityList;
    }

    private UserEntity getTestUser(int idx){
        UserEntity user = UserEntity.of("A0000"+idx, "A0000"+idx, RoleType.USER, ActiveStatus.ACTIVE);
        user.setMeta(Map.of("uuid", "000000001"));
        return user;
    }

    private UserEntity getTestManager(){
        return UserEntity.of("manager", "manager1234", RoleType.MANAGER, ActiveStatus.ACTIVE);
    }

    private PlaceEntity getSavedTestPlace(){
        UserEntity userEntity = getTestManager();
        PlaceEntity placeEntity = PlaceEntity.of("스터디 카페 강원 부곡점", userEntity, ActiveStatus.ACTIVE);
        return placeEntity;
    }

    private List<SeatEntity> getTestSeatList(PlaceEntity placeEntity, int size){
        List<SeatEntity> seatEntityList = new ArrayList<>();
        for (int i =0; i<size; ++i){
            SeatEntity seatEntity = SeatEntity.of(placeEntity, "A0"+i,  SeatType.LAPTOP, i,i);
            seatEntityList.add(seatEntity);
        }
        return seatEntityList;
    }

    private PackageEntity getTestPackage(){
        PlaceEntity placeEntity = getSavedTestPlace();
        PackageEntity packageEntity = PackageEntity.of(placeEntity, "당일 4시간", 6000, 4, PackageType.DAY);
        return packageEntity;
    }
}