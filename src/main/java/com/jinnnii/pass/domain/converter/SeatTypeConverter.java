package com.jinnnii.pass.domain.converter;

import com.jinnnii.pass.domain.constant.SeatType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class SeatTypeConverter implements AttributeConverter<SeatType, String> {
    @Override
    public String convertToDatabaseColumn(SeatType attribute) {
        return attribute.name();
    }

    @Override
    public SeatType convertToEntityAttribute(String dbData) {
        return SeatType.valueOf(dbData);
    }
}
