package com.jinnnii.pass.domain.converter;

import com.jinnnii.pass.domain.constant.RoleType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class RoleTypeConverter implements AttributeConverter<RoleType, String> {
    @Override
    public String convertToDatabaseColumn(RoleType attribute) {
        return attribute.name();
    }

    @Override
    public RoleType convertToEntityAttribute(String dbData) {
        return RoleType.valueOf(dbData);
    }
}
