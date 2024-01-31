package com.jinnnii.pass.domain.mapper;

import com.jinnnii.pass.domain.BulkPassEntity;
import com.jinnnii.pass.domain.PassEntity;
import com.jinnnii.pass.domain.UserEntity;
import com.jinnnii.pass.domain.constant.ActiveStatus;
import com.jinnnii.pass.domain.constant.BulkPassStatus;
import com.jinnnii.pass.domain.constant.PassStatus;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

// 일치 하지 않은 필드 무시
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PassModelMapper {
    PassModelMapper INSTANCE = Mappers.getMapper(PassModelMapper.class);
    @Mapping(target = "status", qualifiedByName = "defaultStatus")
    @Mapping(target = "remainingPeriod", source = "bulkPassEntity.period")
    PassEntity toPassEntity(BulkPassEntity bulkPassEntity);

    @Named("defaultStatus")
    default PassStatus status(BulkPassStatus status){
        return PassStatus.READY;
    }
}
