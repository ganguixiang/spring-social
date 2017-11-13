package com.lese.spring.social.wechat.connect;

import com.lese.spring.social.wechat.api.WeChat;
import com.lese.spring.social.wechat.api.impl.WeChatTemplate;
import org.springframework.social.oauth2.AbstractOAuth2ServiceProvider;
import org.springframework.social.oauth2.OAuth2Operations;
import org.springframework.social.oauth2.OAuth2Template;

/**
 * Created by ganguixiang on 2017/11/11.
 */
public class WeChatServiceProvider extends AbstractOAuth2ServiceProvider<WeChat> {

    private String appId;

    public WeChatServiceProvider(String clientId, String clientSecret) {
        // https://open.weixin.qq.com/connect/qrconnect?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect
        // https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code
        super(new WeChatOAuth2Template(clientId, clientSecret, "https://open.weixin.qq.com/connect/qrconnect", "https://api.weixin.qq.com/sns/oauth2/access_token"));
        this.appId = clientId;
    }

    @Override
    public WeChat getApi(String accessToken) {
        return new WeChatTemplate(accessToken, appId);
    }
}
