package me.quiz_together.root.support;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;
import me.quiz_together.root.model.firebase.FcmContainer;

@Slf4j
@Component
public class FcmRestTemplate extends RestTemplate {
    private static final String FCM_URL = "https://fcm.googleapis.com/fcm/send";

    @Value("${fcm.apiKey}")
    private String apiKey;

    public <T> T postForMessage(FcmContainer fcmContainer, Class<T> responseType) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json;charset=UTF-8");
        httpHeaders.add("Authorization", "key = " + apiKey);

        HttpEntity<FcmContainer> httpEntity = new HttpEntity<>(fcmContainer, httpHeaders);

        log.debug("fcm httpEntity : [{}]", httpEntity);

        ResponseEntity<T> response = exchange(FCM_URL, HttpMethod.POST, httpEntity, responseType);

        log.debug("fcm request status code : [{}]", response.getStatusCode());
        return response.getBody();
    }

}
