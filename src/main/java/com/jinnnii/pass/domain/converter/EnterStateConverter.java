package com.jinnnii.pass.domain.converter;

import com.jinnnii.pass.domain.constant.EnterStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class EnterStateConverter implements AttributeConverter<EnterStatus, String> {
    @Override
    public String convertToDatabaseColumn(EnterStatus attribute) {
        return attribute.name();
    }

    @Override
    public EnterStatus convertToEntityAttribute(String dbData) {
        return EnterStatus.valueOf(dbData);
    }
}
