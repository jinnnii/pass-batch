package com.jinnnii.pass.domain.converter;

import com.jinnnii.pass.domain.constant.BulkPassStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class BulkPassStatusConverter implements AttributeConverter<BulkPassStatus, String> {
    @Override
    public String convertToDatabaseColumn(BulkPassStatus attribute) {
        return attribute.name();
    }

    @Override
    public BulkPassStatus convertToEntityAttribute(String dbData) {
        return BulkPassStatus.valueOf(dbData);
    }
}
