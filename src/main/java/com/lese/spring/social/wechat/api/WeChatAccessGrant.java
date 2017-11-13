package com.lese.spring.social.wechat.api;

import org.springframework.social.oauth2.AccessGrant;

/**
 * 微信AccessGrant，比AccessGrant多了两个字段，openId和unionId，因为在获取access_token的时候会把这两个字段与access_token一起返回
 * Created by ganguixiang on 2017/11/11.
 */
public class WeChatAccessGrant extends AccessGrant {
    private String openId;
    private String unionId;

    public WeChatAccessGrant() {
        super("");
    }

    public WeChatAccessGrant(String accessToken, String scope, String refreshToken, Long expiresIn) {
        super(accessToken, scope, refreshToken, expiresIn);
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }
}
