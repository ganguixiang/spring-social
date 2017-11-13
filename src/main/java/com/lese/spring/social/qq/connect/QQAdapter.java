package com.lese.spring.social.qq.connect;

import com.lese.spring.social.qq.api.QQ;
import com.lese.spring.social.qq.api.QQProfile;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;

/**
 * QQ适配器
 * Created by ganguixiang on 2017/11/10.
 */
public class QQAdapter implements ApiAdapter<QQ> {


    /**
     * 检测api是否可用
     * @param qq
     * @return
     */
    @Override
    public boolean test(QQ qq) {
        try {
            qq.userOperations().getUserProfile();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 设置ConnectionValues的一些属性
     * @param qq
     * @param values
     */
    @Override
    public void setConnectionValues(QQ qq, ConnectionValues values) {
        QQProfile profile = qq.userOperations().getUserProfile();
        // 这里将openId作为providerUserId，因为openId在一个应用中是唯一的
        values.setProviderUserId(profile.getOpenId());
        values.setDisplayName(profile.getNickname());
        values.setImageUrl(profile.getFigureurl_qq_1());
    }

    /**
     * 映射QQProfile到UserProfile
     * @param qq
     * @return
     */
    @Override
    public UserProfile fetchUserProfile(QQ qq) {
        QQProfile profile = qq.userOperations().getUserProfile();
        return new UserProfile(profile.getOpenId(), null, null, null, null, profile.getNickname());
    }

    /**
     * 更新状态
     * @param api
     * @param message
     */
    @Override
    public void updateStatus(QQ api, String message) {
        // do nothing
    }
}
