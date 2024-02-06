package com.jinnnii.pass.domain.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jinnnii.pass.adapter.KakaoTalkMessageRequest;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.SneakyThrows;

import java.util.Map;

@Converter
public class TemplateObjectConverter implements AttributeConverter<KakaoTalkMessageRequest.TemplateObject, String> {
    private static final ObjectMapper mapper = new ObjectMapper();

    @SneakyThrows
    @Override
    public String convertToDatabaseColumn(KakaoTalkMessageRequest.TemplateObject attribute) {
        return mapper.writeValueAsString(attribute);
    }

    @SneakyThrows
    @Override
    public KakaoTalkMessageRequest.TemplateObject convertToEntityAttribute(String dbData) {
        Map<String, Object> convertValue = mapper.readValue(dbData, Map.class);
        if (! convertValue.containsKey("object_type") ) return null;

        String objectType = String.valueOf(convertValue.get("object_type"));
        return switch (objectType){
            case "text" -> mapper.convertValue(convertValue, KakaoTalkMessageRequest.TextObject.class);
            case "feed" -> mapper.convertValue(convertValue, KakaoTalkMessageRequest.FeedObject.class);
            default -> null;
        };
    }
}
