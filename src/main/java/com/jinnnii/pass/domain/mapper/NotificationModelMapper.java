package com.jinnnii.pass.domain.mapper;

import com.jinnnii.pass.domain.EntranceEntity;
import com.jinnnii.pass.domain.NotificationEntity;
import com.jinnnii.pass.domain.constant.NotificationEvent;
import com.jinnnii.pass.dto.response.EntranceDto;
import com.jinnnii.pass.util.LocalDateTimeUtils;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NotificationModelMapper {
    NotificationModelMapper INSTANCE = Mappers.getMapper(NotificationModelMapper.class);

    @Mapping(target = "uuid" , source = "entranceEntity.passEntity.userEntity.uuid")
    NotificationEntity toNotificationEntity(EntranceEntity entranceEntity, NotificationEvent event);


    @BeforeMapping
    default void notificationWithText(EntranceEntity entranceEntity, NotificationEvent event,
                                      @MappingTarget NotificationEntity notificationEntity){
        EntranceDto entranceDto = EntranceDtoMapper.INSTANCE.toEntranceDto(entranceEntity);
        String text = switch (event){
            case BEFORE_EXPIRED -> beforeExpiredText(entranceDto);
            case AFTER_ENTERED -> afterEnteredText(entranceDto);
        };
        notificationEntity.setText(text);
    }

    default String beforeExpiredText(EntranceDto entrance){
        return String.format("""
                매장명 : %s
                                
                좌석 번호 : %s
                
                이용만료 10분 전입니다.
                연장하실 고객께서는 늦어도 5분 전까지 연장 결제 후 이용 부탁드립니다.
                """,
                entrance.placeName()
                ,  entrance.seatName());
    }
    default String afterEnteredText(EntranceDto entrance){
        return String.format("""
        매장명 : %s
                        
        결제 내역 : %s / %s 원
                        
        좌석 번호 : %s
        이용 만료 : %s

        매장 입장 시 아래 [QR 코드] 를 출입문 스캐너에 읽혀주세요.
                        
        * 주의 사항
        - 개인 전열기 사용 금지
        - 외부 음식 반입 금지
        - 등록된 사용자의 대리 사용 금지
        - 퇴실 시 키오스크에서 퇴실 처리를 해주세요

        기타 불편한 사항은 언제든지 메시지로 문의 주시길 바랍니다.
        찾아주셔서 감사합니다.
        """
                , entrance.placeName()
                , entrance.packageName()
                , entrance.price()
                , entrance.seatName()
                , entrance.remainingTime()==null
                        ? LocalDateTimeUtils.format(entrance.endedAt())
                        : LocalDateTimeUtils.format(entrance.remainingTime()));
    }

}