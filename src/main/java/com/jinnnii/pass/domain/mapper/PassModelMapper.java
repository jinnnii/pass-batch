package com.jinnnii.pass.domain.mapper;

import com.jinnnii.pass.domain.BulkPassEntity;
import com.jinnnii.pass.domain.PassEntity;
import com.jinnnii.pass.domain.constant.BulkStatus;
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
    @Mapping(target = "remainingTime", source = "bulkPassEntity.time")
    PassEntity toPassEntity(BulkPassEntity bulkPassEntity);

    @Named("defaultStatus")
    default PassStatus status(BulkStatus status){
        return PassStatus.READY;
    }
}
