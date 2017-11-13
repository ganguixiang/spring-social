package com.lese.spring.social.wechat.connect;

import com.lese.spring.social.wechat.api.WeChat;
import com.lese.spring.social.wechat.api.WeChatProfile;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;

/**
 * Created by ganguixiang on 2017/11/11.
 */
public class WeChatAdapter implements ApiAdapter<WeChat> {

    private String openId;

    public WeChatAdapter() {
    }

    public WeChatAdapter(String openId) {
        this.openId = openId;
    }

    @Override
    public boolean test(WeChat api) {
        try {
            api.userOperations().getUserProfile(this.openId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void setConnectionValues(WeChat api, ConnectionValues values) {
        WeChatProfile profile = api.userOperations().getUserProfile(this.openId);
        values.setProviderUserId(profile.getOpenid());
        values.setDisplayName(profile.getNickname());
        values.setImageUrl(profile.getHeadimgurl());
    }

    @Override
    public UserProfile fetchUserProfile(WeChat api) {
        WeChatProfile profile = api.userOperations().getUserProfile(this.openId);
        return new UserProfile(profile.getOpenid(), null, null, null, null, profile.getNickname());
    }

    @Override
    public void updateStatus(WeChat api, String message) {
        // do noting
    }
}
