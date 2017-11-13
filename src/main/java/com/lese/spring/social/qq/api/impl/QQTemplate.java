package com.lese.spring.social.qq.api.impl;

import com.lese.spring.social.qq.api.QQ;
import com.lese.spring.social.qq.api.UserOperations;
import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;

/**
 * qq api实现
 * Created by ganguixiang on 2017/11/10.
 */
public class QQTemplate extends AbstractOAuth2ApiBinding implements QQ {

    private UserOperations userOperations;

    private String appId;

    private String accessToken;


    public QQTemplate(String accessToken, String appId) {
        super(accessToken);
        this.appId = appId;
        this.accessToken = accessToken;
        initialize();
    }

    /**
     * 初始化operations
     */
    private void initialize() {
        this.userOperations = new UserOperationsImpl(this);
    }

    @Override
    public UserOperations userOperations() {
        return this.userOperations;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
