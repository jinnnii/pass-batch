package com.jinnnii.pass.adapter;

import com.jinnnii.pass.config.KakaoTalkConfig;
import com.jinnnii.pass.dto.request.KakaoTalkMessageRequest;
import com.jinnnii.pass.dto.response.KakaoTalkMessageResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class KakaoTalkAdapter {
    private final WebClient webClient;

    public KakaoTalkAdapter(KakaoTalkConfig config) {
        webClient = WebClient.builder()
                .baseUrl(config.host())
                .defaultHeaders(h->{
                    h.setBearerAuth(config.token());
                    h.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                })
                .build();
    }

    /**
     * [메시지 전송] 이용권 만료 10분 전 알림 전송
     * @param uuid
     * @param text
     * @return
     */
    public boolean sendKakaoTalkMessage(final String uuid, final String text){
        KakaoTalkMessageResponse response = webClient.post().uri("/v1/api/talk/friends/message/default/send")
                .body(BodyInserters.fromValue(KakaoTalkMessageRequest.from(uuid, text)))
                .retrieve()//decoding
                .bodyToMono(KakaoTalkMessageResponse.class)
                .block();

        if (response == null || response.successfulReceiverUuids()==null) return false;
        return response.successfulReceiverUuids().size() > 0;
    }
}
