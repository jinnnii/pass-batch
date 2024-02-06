package com.jinnnii.pass.adapter;

import com.jinnnii.pass.config.KakaoTalkConfig;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class KakaoTalkAdapter {
    private final WebClient webClient;

    public KakaoTalkAdapter(KakaoTalkConfig config) {
        webClient = WebClient.builder()
                .baseUrl(config.getHost())
                .defaultHeaders(h->{
                    h.setBearerAuth(config.getUuid());
                    h.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                })
                .build();
    }

    /**
     * [메시지 전송]
     * @param request
     * @return
     */
    public boolean sendKakaoTalkMessage(KakaoTalkMessageRequest request){
        KakaoTalkMessageResponse response = webClient.post().uri("/v1/api/talk/friends/message/default/send")
                .body(BodyInserters.fromValue(request))
                .retrieve()//decoding
                .bodyToMono(KakaoTalkMessageResponse.class)
                .block();

        if (response == null || response.successfulReceiverUuids()==null) return false;
        return response.successfulReceiverUuids().size() > 0;
    }

}
