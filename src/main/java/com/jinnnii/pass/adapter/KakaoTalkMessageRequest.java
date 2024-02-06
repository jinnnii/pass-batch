package com.jinnnii.pass.adapter;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;

import java.util.List;
import java.util.Map;

public record KakaoTalkMessageRequest (
        // 친구 목록으로 가져온 사용자 UUID (최대 5개)
        @JsonProperty("receiver_uuids") List<String> receiverUuids,
        // 메시지 구성 요소를 담은 객체 -> 텍스트
        @JsonProperty("template_object") TemplateObject templateObject
){

    public static KakaoTalkMessageRequest from(String uuid, TemplateObject templateObject){
        return KakaoTalkMessageRequest.from(uuid, templateObject);
    }

    public interface TemplateObject {
        String getObjectType();

        record Link(
                @JsonProperty("web_url") String webUrl,
                @JsonProperty("mobile_web_url") String mobileWebUrl
        ){
            public Link(String url) {
                this(url, url);
            }
        }
    }

    public record TextObject(
            @JsonProperty("object_type") String objectType, //템플릿 종류
            String text, //메시지 정보 (최대 200자)
            Link link, // 콘텐츠 클릭 시 이동할 링크 정보
            @JsonProperty("button_title") String buttonTitle // 버튼 명 타이틀 정보 (default=자세히보기)

    )implements TemplateObject{
        public static TextObject from(String text, String url, String buttonTitle) {
            return new TextObject("text", text, new Link(url), buttonTitle);
        }
        @Override public String getObjectType() {return this.objectType;}
    }

    public record FeedObject(
            @JsonProperty("object_type") String objectType, //템플릿 종류
            ContentObject content

    )implements TemplateObject {
        public FeedObject(ContentObject content) {
            this("feed", content);
        }

        public static FeedObject from(String title, String imageUrl, String description, String url) {
            return new FeedObject(new ContentObject(title, imageUrl, description, new Link(url)));
        }
        @Override public String getObjectType() {return this.objectType;}

        public record ContentObject(
                String title, //타이틀
                @JsonProperty("image_url") String imageUrl, //이미지 URL
                String description, // 설명
                Link link //콘텐츠 클릭 시 이동할 링크 정보
        ) {

        }
    }

}
