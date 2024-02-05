package com.jinnnii.pass.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;

public record KakaoTalkMessageRequest (
        // 친구 목록으로 가져온 사용자 UUID (최대 5개)
        @JsonProperty("receiver_uuids") List<String> receiverUuids,
        // 메시지 구성 요소를 담은 객체 -> 텍스트
        @JsonProperty("template_object") TemplateObject templateObject
){
    public static KakaoTalkMessageRequest from(String uuid, String text, String url, String buttonTitle) {
        TemplateObject templateObject =
                new TemplateObject("text", text, TemplateObject.Link.from(url), buttonTitle);
        List<String> receiverUuids = Collections.singletonList(uuid);

        return new KakaoTalkMessageRequest(receiverUuids, templateObject);
    }

    public static KakaoTalkMessageRequest from(String uuid, String text){
        return KakaoTalkMessageRequest.from(uuid, text, null, null);
    }

    public record TemplateObject(
            @JsonProperty("object_type") String objectType, //템플릿 종료
            String text, //메시지 정보 (최대 200자)
            Link link, // 콘텐츠 클릭 시 이동할 링크 정보
            @JsonProperty("button_title") String buttonTitle // 버튼 명 타이틀 정보 (default=자세히보기)

    ){
        public record Link(
                @JsonProperty("web_url") String webUrl,
                @JsonProperty("mobile_web_url") String mobileWebUrl
        ){
            public static Link from(String url) {
                return new Link(url, url);
            }
        }
    }

}
