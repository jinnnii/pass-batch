package com.jinnnii.pass.repository;

import com.jinnnii.pass.domain.PackageEntity;
import com.jinnnii.pass.domain.PlaceEntity;
import com.jinnnii.pass.domain.UserEntity;
import com.jinnnii.pass.domain.constant.ActiveStatus;
import com.jinnnii.pass.domain.constant.PackageType;
import com.jinnnii.pass.domain.constant.RoleType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@DisplayName("JPA 연결 테스트")
@DataJpaTest
class PackageRepositoryTest {

    @Autowired private PackageRepository packageRepository;
    @Autowired private PlaceRepository placeRepository;
    @Autowired private UserRepository userRepository;

    @EnableJpaAuditing
    @TestConfiguration
    public static class TestJpaConfig{
        @Bean
        public AuditorAware<String> auditorAware(){
            return ()-> Optional.of("jinnnii");
        }
    }

    @DisplayName("패키지 생성")
    @Test
    public void test_package_save(){
        //given
        PackageEntity packageEntity = getTestPackage();

        //when
        packageRepository.save(packageEntity);

        //then
        assertNotNull(packageEntity.getPackageId());
    }

    @DisplayName("패키지 리스트 조회")
    @Test
    public void test_package_select_list_by_createdAt(){
        //given
        LocalDateTime dateTime = LocalDateTime.now().minusMinutes(1);

        PackageEntity packageEntity1 = getTestPackage();
        PackageEntity packageEntity2 = getTestPackage(packageEntity1.getPlaceEntity());

        packageRepository.save(packageEntity1);
        packageRepository.save(packageEntity2);

        //when
        final List<PackageEntity> packageEntityList = packageRepository.findByCreatedAtAfter(dateTime,
                PageRequest.of(0,2, Sort.by("packageId").descending()));

        //then
        assertEquals(2, packageEntityList.size());
        assertEquals(packageEntity2.getPackageId(), packageEntityList.get(0).getPackageId());
    }
    @DisplayName("패키지 기간 수정")
    @Test
    void test_package_update_period() {
        //given
        PackageEntity packageEntity = getTestPackage();
        packageRepository.save(packageEntity);

        //when
        packageEntity.setPeriod(24);
        final PackageEntity updatedPackage = packageRepository.findById(packageEntity.getPackageId()).get();

        //then
        assertEquals(24, updatedPackage.getPeriod());
    }
    @DisplayName("패키지 기간 및 가격 수정")
    @Test
    void test_package_update_period_and_price() {
        //given
        PackageEntity packageEntity = getTestPackage();
        packageRepository.save(packageEntity);
        //when
        int updatedCount = packageRepository.updatePeriodAndPrice(packageEntity.getPackageId(), 24, 12000);
        final PackageEntity updatedPackage = packageRepository.findById(packageEntity.getPackageId()).get();

        //then
        assertEquals(1, updatedCount);
        assertEquals(24, updatedPackage.getPeriod());
        assertEquals(12000, updatedPackage.getPrice());
    }
    @DisplayName("패키지 삭제")
    @Test
    void test_package_delete() {
        //given
        PackageEntity packageEntity = getTestPackage();
        packageRepository.save(packageEntity);
        //when
        packageRepository.deleteById(packageEntity.getPackageId());

        //then
        assertTrue(packageRepository.findById(packageEntity.getPackageId()).isEmpty());
    }

    private UserEntity getTestUser(){
        return UserEntity.of("jinnnii", "jinnnii1234", RoleType.USER, ActiveStatus.ACTIVE);
    }

    private PlaceEntity getTestPlace(){
        UserEntity userEntity = getTestUser();
        userRepository.save(userEntity);
        return PlaceEntity.of("스터디 카페 강원 부곡점", userEntity, ActiveStatus.ACTIVE);
    }

    private PackageEntity getTestPackage(PlaceEntity placeEntity){
        return PackageEntity.of(placeEntity, "당일 5시간", 6000, 4, PackageType.DAY);
    }

    private PackageEntity getTestPackage(){
        PlaceEntity placeEntity = getTestPlace();
        placeRepository.save(placeEntity);

        return PackageEntity.of(placeEntity, "당일 4시간", 6000, 4, PackageType.DAY);
    }
}
