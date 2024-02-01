package com.jinnnii.pass.config;
import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "kakaotalk")
public record KakaoTalkConfig(String host, String token){ }

