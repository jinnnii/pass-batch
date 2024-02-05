package com.jinnnii.pass.config;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.stereotype.Component;


@ConfigurationProperties(prefix = "kakaotalk")
@Component
@Getter
@Setter
public class KakaoTalkConfig{
    private String host;
    private String uuid;
}

