package com.jinnnii.pass.domain.converter;

import com.jinnnii.pass.domain.constant.ActiveStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class ActiveStatusConverter implements AttributeConverter<ActiveStatus, String> {

    @Override
    public String convertToDatabaseColumn(ActiveStatus attribute) {
        return attribute.name();
    }

    @Override
    public ActiveStatus convertToEntityAttribute(String dbData) {
        return ActiveStatus.valueOf(dbData);
    }
}
