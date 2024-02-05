package com.jinnnii.pass.domain.mapper;

import com.jinnnii.pass.domain.EntranceEntity;
import com.jinnnii.pass.dto.response.EntranceDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EntranceDtoMapper {
    EntranceDtoMapper INSTANCE = Mappers.getMapper(EntranceDtoMapper.class);
    @Mapping(target = "placeName", source = "entranceEntity.seatEntity.placeEntity.placeName")
    @Mapping(target = "packageName", source = "entranceEntity.passEntity.packageEntity.packageName")
    @Mapping(target = "price", source = "entranceEntity.passEntity.packageEntity.price")
    @Mapping(target = "seatName", source = "entranceEntity.seatEntity.seatName")
    @Mapping(target = "remainingTime", source = "entranceEntity.passEntity.remainingTime")
    @Mapping(target = "endedAt", source = "entranceEntity.passEntity.endedAt")
    EntranceDto toEntranceDto(EntranceEntity entranceEntity);
}
