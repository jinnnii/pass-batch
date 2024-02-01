package com.jinnnii.pass.domain.converter;

import com.jinnnii.pass.domain.constant.NotificationEvent;
import com.jinnnii.pass.domain.constant.PackageType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class NotificationEventConverter implements AttributeConverter<NotificationEvent, String> {
    @Override
    public String convertToDatabaseColumn(NotificationEvent attribute) {
        return attribute.name();
    }

    @Override
    public NotificationEvent convertToEntityAttribute(String dbData) {
        return NotificationEvent.valueOf(dbData);
    }
}
