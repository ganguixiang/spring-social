package com.lese.spring.social.wechat.api.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lese.spring.social.wechat.api.UserOperations;
import com.lese.spring.social.wechat.api.WeChatProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

/**
 * 用户操作实现类
 * Created by ganguixiang on 2017/11/11.
 */
public class UserOperationsImpl implements UserOperations {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserOperationsImpl.class);

    private String openId;

    private String appId;

    private String accessToken;

    private RestTemplate restTemplate;

    private ObjectMapper objectMapper = new ObjectMapper();

    private static final String GET_USER_INFO = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s";

    public UserOperationsImpl(WeChatTemplate api) {
        this.appId = api.getAppId();
        this.accessToken = api.getAccessToken();
        this.restTemplate = api.getRestTemplate();
    }

    @Override
    public WeChatProfile getUserProfile(String openId) {
        // https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s 获取用户信息
        // 获取用户信息
        String url = String.format(GET_USER_INFO, this.accessToken, openId);
        String result = restTemplate.getForObject(url, String.class);
        LOGGER.info(result);
        try {
            WeChatProfile profile =objectMapper.readValue(result, WeChatProfile.class);
            return profile;
        } catch (IOException e) {
            throw new RuntimeException("获取用户信息失败", e);
        }
    }
}
