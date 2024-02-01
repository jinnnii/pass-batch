package com.jinnnii.pass.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record KakaoTalkMessageResponse (
        // 메시지 전송에 성공한 사용자 uuid 리스트
        @JsonProperty("successful_receiver_uuids")List<String> successfulReceiverUuids
        ){
}
