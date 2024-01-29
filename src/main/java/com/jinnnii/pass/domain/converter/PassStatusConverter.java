package com.jinnnii.pass.domain.converter;

import com.jinnnii.pass.domain.constant.PassStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class PassStatusConverter implements AttributeConverter<PassStatus, String> {
    @Override
    public String convertToDatabaseColumn(PassStatus attribute) {
        return attribute.name();
    }

    @Override
    public PassStatus convertToEntityAttribute(String dbData) {
        return PassStatus.valueOf(dbData);
    }
}
