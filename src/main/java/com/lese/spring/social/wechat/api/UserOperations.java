package com.lese.spring.social.wechat.api;

/**
 * Created by ganguixiang on 2017/11/11.
 */
public interface UserOperations {

    WeChatProfile getUserProfile(String openId);

}
