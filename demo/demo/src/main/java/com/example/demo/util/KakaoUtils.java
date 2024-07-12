package com.example.demo.util;

import com.example.demo.util.CustomKakaoException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
@Slf4j
public class KakaoUtils {
    @Value("${kakao.url}")
    private String url;

    @Value("${kakao.client.id}")
    private String clientId;

    @Value("${kakao.redirect.uri}")
    private String redirectURI;

    @Value("${kakao.response.type}")
    private String responseType;

    private final WebClient webClient1;

    private final WebClient webClient2;

    public KakaoUtils(WebClient.Builder webClientBuilder,
                      @Value("${accessTokenUrl}") String accessTokenUrl,
                      @Value("${kakao.user.info.url}") String userInfoURL) {
        log.info("accessTokenUrl={}", accessTokenUrl);
        this.webClient1 = webClientBuilder.baseUrl(accessTokenUrl).build();
        this.webClient2 = webClientBuilder.baseUrl(userInfoURL).build();
    }

    // 인가 코드 URL 생성
    public String getKakaoURI() {
        log.info("url={}", url);
        return url + "?client_id=" + clientId + "&redirect_uri=" + redirectURI + "&response_type=" + responseType;
    }

    // 액세스 토큰 요청 및 처리
    public String getKakaoAccessToken(String authorizationCode) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "authorization_code");
        formData.add("client_id", clientId);
        formData.add("redirect_uri", redirectURI);
        formData.add("code", authorizationCode);

        String accessToken = webClient1.post()
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> (String) response.get("access_token"))
                .block();

        if (accessToken != null) {
            return accessToken;
        } else {
            throw new CustomKakaoException("엑세스토큰을 가져오는데 실패했습니다.");
        }
    }

    //카카오 유저 정보 가져오기(nickname)
    public String getKakaoUserInfo(String accessToken) {
        try {
            Map properties = webClient2.get()
                    .header("Authorization", "Bearer " + accessToken)
                    .header("Content-type", "application/x-www-form-urlencoded;charset=utf-8")
                    .retrieve()
                    .bodyToMono(LinkedHashMap.class)
                    .map(response -> (Map) response.get("properties"))
                    .block();

            if (properties == null) {
                throw new CustomKakaoException("Failed to retrieve user properties.");
            }

            String nickname = (String) properties.get("nickname");

            if (nickname == null || nickname.isEmpty()) {
                throw new CustomKakaoException("Nickname is not available.");
            }

            return nickname;
        } catch (WebClientResponseException e) {
            log.error("Error fetching Kakao user info: {}", e.getMessage());
            if (e.getStatusCode().value() == 401) {
                throw new CustomKakaoException("Unauthorized: Invalid or expired access token.");
            } else {
                throw new CustomKakaoException("Error fetching Kakao user info.");
            }
        }
    }
}
