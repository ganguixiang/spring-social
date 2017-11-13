package com.lese.spring.social.wechat.api.impl;

import com.lese.spring.social.wechat.api.UserOperations;
import com.lese.spring.social.wechat.api.WeChat;
import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;

/**
 * Created by ganguixiang on 2017/11/11.
 */
public class WeChatTemplate extends AbstractOAuth2ApiBinding implements WeChat {

    private UserOperations userOperations;

    private String accessToken;

    private String appId;

    public WeChatTemplate(String accessToken, String appId) {
        this.accessToken = accessToken;
        this.appId = appId;
        initialize();
    }

    public void initialize() {
        this.userOperations = new UserOperationsImpl(this);
    }


    @Override
    public UserOperations userOperations() {
        return this.userOperations;
    }

    public UserOperations getUserOperations() {
        return userOperations;
    }

    public void setUserOperations(UserOperations userOperations) {
        this.userOperations = userOperations;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }
}
