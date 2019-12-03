package com.ryuntech.authorization.service;

import com.ryuntech.common.constant.enums.CommonEnums;
import com.ryuntech.common.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class LoginService {
    @Value("${spring.auth.client_id}")
    String clientId;

    @Value("${spring.auth.client_secret}")
    String clientSecret;

    @Value("${spring.auth.grant_type}")
    String grantType;

    @Autowired
    LoadBalancerClient loadBalancerClient;

    @Autowired
    RestTemplate restTemplate;

    public Result login(String username, String password) {
        // 申请令牌
        ServiceInstance serviceInstance = loadBalancerClient.choose("ryuntech-su-cloud-gateway");
        URI uri = serviceInstance.getUri();
        String authUri = uri + "/api/auth/oauth/token";
        //authUri = authUri.replace("http", "https");
        authUri = "https://wx.ryuntech.com:9999/api/auth/oauth/token";

        LinkedMultiValueMap<String, String> header = new LinkedMultiValueMap<>();
        String httpBasic = getHttpBasic(clientId, clientSecret);
        header.add("Authorization", httpBasic);

        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("username", username);
        body.add("password", password);
        body.add("grant_type", grantType);

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(body, header);

        restTemplate.setErrorHandler(new CustomErrorHandler());
        ResponseEntity<Map> response = restTemplate.exchange(authUri, HttpMethod.POST, httpEntity, Map.class);
        if (response.getStatusCode() != HttpStatus.OK) {
            LinkedHashMap hashMap = (LinkedHashMap) response.getBody();
            Object errorMsg = hashMap.get("error_description");
            return new Result(CommonEnums.LOGIN_ERROR, String.valueOf(errorMsg));
        }

        return new Result(response.getBody().get("access_token"));
    }

    private String getHttpBasic(String client, String secret) {
        String s = client + ":" + secret;
        byte[] encode = Base64Utils.encode(s.getBytes());
        return "Basic " + new String(encode);
    }
}
