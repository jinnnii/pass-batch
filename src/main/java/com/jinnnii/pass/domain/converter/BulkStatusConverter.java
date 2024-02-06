package com.jinnnii.pass.domain.converter;

import com.jinnnii.pass.domain.constant.BulkStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class BulkStatusConverter implements AttributeConverter<BulkStatus, String> {
    @Override
    public String convertToDatabaseColumn(BulkStatus attribute) {
        return attribute.name();
    }

    @Override
    public BulkStatus convertToEntityAttribute(String dbData) {
        return BulkStatus.valueOf(dbData);
    }
}
