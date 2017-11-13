package com.lese.spring.social.qq.api.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lese.spring.social.qq.api.QQProfile;
import com.lese.spring.social.qq.api.UserOperations;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

/**
 * 用户操作实现
 * Created by ganguixiang on 2017/11/10.
 */
public class UserOperationsImpl implements UserOperations {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserOperationsImpl.class);

    private String openId;

    private String appId;

    private String accessToken;

    private RestTemplate restTemplate;

    private ObjectMapper objectMapper = new ObjectMapper();

    private static final String GET_OPEN_ID = "https://graph.qq.com/oauth2.0/me?access_token=%s";

    private static final String GET_USER_INFO = "https://graph.qq.com/user/get_user_info?access_token=%s&oauth_consumer_key=%s&openid=%s";

    public UserOperationsImpl(QQTemplate api) {
        this.restTemplate = api.getRestTemplate();
        this.accessToken = api.getAccessToken();
        this.appId = api.getAppId();
        // 获取openId
        String url = String.format(GET_OPEN_ID, this.accessToken);
        String result = restTemplate.getForObject(url, String.class);
        // callback( {"client_id":"101386962","openid":"9CB5BEC430AE5C1A75E7B89FE41C1B98"} )
        LOGGER.info(result);
        // 获取openid
        this.openId = StringUtils.substringBetween(result, "\"openid\":\"", "\"}");
    }

    @Override
    public QQProfile getUserProfile() {
        // 获取用户基本信息
        String url = String.format(GET_USER_INFO, this.accessToken, this.appId, this.openId);
        String result = restTemplate.getForObject(url, String.class);
        /**
         * {
             "ret": 0,
             "msg": "",
             "is_lost":0,
             "nickname": "shit",
             "gender": "男",
             "province": "",
             "city": "",
             "year": "1994",
             "figureurl": "http:\/\/qzapp.qlogo.cn\/qzapp\/101386962\/9CB5BEC430AE5C1A75E7B89FE41C1B98\/30",
             "figureurl_1": "http:\/\/qzapp.qlogo.cn\/qzapp\/101386962\/9CB5BEC430AE5C1A75E7B89FE41C1B98\/50",
             "figureurl_2": "http:\/\/qzapp.qlogo.cn\/qzapp\/101386962\/9CB5BEC430AE5C1A75E7B89FE41C1B98\/100",
             "figureurl_qq_1": "http:\/\/q.qlogo.cn\/qqapp\/101386962\/9CB5BEC430AE5C1A75E7B89FE41C1B98\/40",
             "figureurl_qq_2": "http:\/\/q.qlogo.cn\/qqapp\/101386962\/9CB5BEC430AE5C1A75E7B89FE41C1B98\/100",
             "is_yellow_vip": "0",
             "vip": "0",
             "yellow_vip_level": "0",
             "level": "0",
             "is_yellow_year_vip": "0"
             }
         */
        LOGGER.info(result);
        try {
            QQProfile profile =objectMapper.readValue(result, QQProfile.class);
            profile.setOpenId(this.openId);
            return profile;
        } catch (IOException e) {
            throw new RuntimeException("获取用户信息失败", e);
        }
    }

}
