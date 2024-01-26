package com.jinnnii.pass.domain.converter;

import com.jinnnii.pass.domain.constant.PackageType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class PackageTypeConverter implements AttributeConverter<PackageType, String> {
    @Override
    public String convertToDatabaseColumn(PackageType attribute) {
        return attribute.name();
    }

    @Override
    public PackageType convertToEntityAttribute(String dbData) {
        return PackageType.valueOf(dbData);
    }
}
